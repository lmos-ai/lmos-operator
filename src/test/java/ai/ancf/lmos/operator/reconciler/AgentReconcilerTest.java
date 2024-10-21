/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.reconciler;

import ai.ancf.lmos.operator.service.AgentDeploymentStatusService;
import ai.ancf.lmos.operator.service.AgentServiceQuery;
import ai.ancf.lmos.operator.service.KubernetesResourceManager;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.javaoperatorsdk.operator.api.reconciler.DeleteControl;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import io.javaoperatorsdk.operator.springboot.starter.test.EnableMockOperator;
import org.eclipse.ditto.wot.model.ThingDescription;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@EnableMockOperator
@Import(AgentReconciler.class)
public class AgentReconcilerTest {

    @MockBean
    private AgentDeploymentStatusService agentDeploymentStatusService;
    
    @MockBean
    private AgentServiceQuery agentServiceQuery;

    @MockBean
    private KubernetesResourceManager kubernetesResourceManager;

    private Deployment deployment;
    private AgentReconciler agentReconciler;

    @BeforeEach
    public void setUp() {
        // Initialize a mock deployment with necessary metadata and status
        deployment = new Deployment();
        ObjectMeta meta = new ObjectMeta();
        meta.setName("test-deployment");
        meta.setNamespace("test-namespace");
        deployment.setMetadata(meta);

        agentReconciler = new AgentReconciler(agentServiceQuery, agentDeploymentStatusService, kubernetesResourceManager);
    }

    @Test
    public void testReconcile_whenDeploymentNotReady() {
        // Pre-conditions
        when(agentDeploymentStatusService.isDeploymentReady(any(Deployment.class))).thenReturn(false);

        // Steps & Expected Result
        UpdateControl<Deployment> updateControl = agentReconciler.reconcile(deployment, null);
        verify(agentDeploymentStatusService).isDeploymentReady(deployment);
        verify(kubernetesResourceManager, never()).createOrUpdateAgentResource(any(), any());
        Assertions.assertTrue(updateControl.isNoUpdate());
        Assertions.assertEquals(updateControl.getScheduleDelay().get(), 10000);
    }

    @Test
    public void testReconcile_whenDeploymentReady() {
        // Pre-conditions
        when(agentDeploymentStatusService.isDeploymentReady(any(Deployment.class))).thenReturn(true);
        when(kubernetesResourceManager.getServiceUrl(any(Deployment.class))).thenReturn("http://example.com");
        when(agentServiceQuery.queryAgentService(anyString())).thenReturn(ThingDescription.newBuilder().build());

        // Steps & Expected Result
        UpdateControl<Deployment> updateControl = agentReconciler.reconcile(deployment, null);
        verify(agentDeploymentStatusService).isDeploymentReady(deployment);
        verify(kubernetesResourceManager).getServiceUrl(deployment);
        verify(agentServiceQuery).queryAgentService("http://example.com");
        verify(kubernetesResourceManager).createOrUpdateAgentResource(any(), eq(deployment));
        Assertions.assertTrue(updateControl.isNoUpdate());
        Assertions.assertFalse(updateControl.getScheduleDelay().isPresent());
    }

    @Test
    public void testReconcile_onQueryFailure() {
        // Pre-conditions
        when(agentDeploymentStatusService.isDeploymentReady(any(Deployment.class))).thenReturn(true);
        when(kubernetesResourceManager.getServiceUrl(any(Deployment.class))).thenReturn("http://example.com");
        when(agentServiceQuery.queryAgentService(anyString())).thenThrow(new RuntimeException("Service query failed"));

        // Steps & Expected Result
        UpdateControl<Deployment> updateControl = agentReconciler.reconcile(deployment, null);
        verify(agentDeploymentStatusService).isDeploymentReady(deployment);
        verify(kubernetesResourceManager).getServiceUrl(deployment);
        verify(agentServiceQuery).queryAgentService("http://example.com");
        verify(kubernetesResourceManager, never()).createOrUpdateAgentResource(any(), any());
        Assertions.assertTrue(updateControl.isNoUpdate());
        Assertions.assertEquals(updateControl.getScheduleDelay().get(), 10000);
    }

    @Test
    public void testCleanup_deletesAgentResource() {
        // Steps & Expected Result
        DeleteControl deleteControl = agentReconciler.cleanup(deployment, null);
        verify(kubernetesResourceManager).deleteAgentResource(deployment);
        assert Objects.equals(deleteControl.isRemoveFinalizer(), true);
    }
}
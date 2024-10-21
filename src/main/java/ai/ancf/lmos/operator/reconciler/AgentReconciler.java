/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.reconciler;

import ai.ancf.lmos.operator.service.AgentDeploymentStatusService;
import ai.ancf.lmos.operator.service.AgentServiceQuery;
import ai.ancf.lmos.operator.service.KubernetesResourceManager;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.javaoperatorsdk.operator.api.reconciler.*;
import org.eclipse.ditto.wot.model.ThingDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Reconciles Deployment resources by watching associated Pods and registering Services.
 */
@Component
@ControllerConfiguration(labelSelector = "wot-agent=true")
public class AgentReconciler implements Reconciler<Deployment>, Cleaner<Deployment> {

    private static final Logger LOG = LoggerFactory.getLogger(AgentReconciler.class);

    private final AgentServiceQuery agentServiceQuery;
    private final AgentDeploymentStatusService agentDeploymentStatusService;
    private final KubernetesResourceManager kubernetesResourceManager;

    public AgentReconciler(AgentServiceQuery agentServiceQuery, AgentDeploymentStatusService agentDeploymentStatusService, KubernetesResourceManager kubernetesResourceManager) {
        this.agentServiceQuery = agentServiceQuery;
        this.agentDeploymentStatusService = agentDeploymentStatusService;
        this.kubernetesResourceManager = kubernetesResourceManager;
    }

    @Override
    public UpdateControl<Deployment> reconcile(Deployment deployment, Context context) {

        boolean isDeploymentReady = agentDeploymentStatusService.isDeploymentReady(deployment);

        LOG.info("is Deployment {} ready: {}", deployment.getMetadata().getName(), isDeploymentReady);

        if(isDeploymentReady) {
            try {
                String serviceUrl = kubernetesResourceManager.getServiceUrl(deployment);
                ThingDescription thingDescription = agentServiceQuery.queryAgentService(serviceUrl);
                    kubernetesResourceManager.createOrUpdateAgentResource(thingDescription, deployment);
                    return UpdateControl.noUpdate();
            } catch (Exception e) {
                LOG.error("Error processing td for deployment: {}", deployment.getMetadata().getName(), e);
                return UpdateControl.<Deployment>noUpdate().rescheduleAfter(10, TimeUnit.SECONDS);
            }
        }

        return UpdateControl.<Deployment>noUpdate().rescheduleAfter(10, TimeUnit.SECONDS);
    }

    @Override
    public DeleteControl cleanup(Deployment deployment, Context<Deployment> context) {
        LOG.info("Trigger AgentResource Deletion for deployment: {}", deployment.getMetadata().getName());
        kubernetesResourceManager.deleteAgentResource(deployment);
        return DeleteControl.defaultDelete();
    }
}
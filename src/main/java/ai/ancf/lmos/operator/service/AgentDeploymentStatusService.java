/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.service;

import io.fabric8.kubernetes.api.model.apps.Deployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@org.springframework.stereotype.Service
public class AgentDeploymentStatusService {

    private static final Logger LOG = LoggerFactory.getLogger(AgentDeploymentStatusService.class);

    public boolean isDeploymentReady(Deployment deployment) {
        String deploymentName = deployment.getMetadata().getName();
        String deploymentNamespace = deployment.getMetadata().getNamespace();
        Integer replicas = deployment.getStatus().getReplicas();
        Integer availableReplicas = deployment.getStatus().getAvailableReplicas();
        Integer desiredReplicas = deployment.getSpec().getReplicas();

        LOG.info(
            "Reconciling Deployment: {} in namespace: {}, Replicas, availableReplicas: {}, desiredReplicas: {}",
            deploymentName, deploymentNamespace, availableReplicas, desiredReplicas);

        return (replicas != null && availableReplicas != null && replicas.equals(desiredReplicas) && availableReplicas.equals(desiredReplicas));
    }
}
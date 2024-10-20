/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.service;

import ai.ancf.lmos.operator.resources.agent.AgentResource;
import ai.ancf.lmos.operator.resources.agent.AgentSpec;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.eclipse.ditto.wot.model.ThingDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Service
public class KubernetesResourceManager {

    private static final Logger LOG = LoggerFactory.getLogger(KubernetesResourceManager.class);

    private final KubernetesClient kubernetesClient;

    public KubernetesResourceManager(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    public void createOrUpdateAgentResource(ThingDescription thingDescription, Deployment deployment) {
        AgentResource agentResource = new AgentResource();
        agentResource.setMetadata(new ObjectMetaBuilder()
            .withName(deployment.getMetadata().getName())
            .withNamespace(deployment.getMetadata().getNamespace())
            .build());

        AgentSpec spec = new AgentSpec();
        spec.setDescription(thingDescription.getDescription().get()
                .toString());
        spec.setThingDescription(thingDescription.toJsonString());

        agentResource.setSpec(spec);

        AgentResource agentResourceCreated = kubernetesClient.resources(AgentResource.class)
                .inNamespace(deployment.getMetadata().getNamespace())
                .withName(deployment.getMetadata().getName())
                .createOrReplace(agentResource);

        LOG.info("AgentResource {} created/updated for deployment: {}", agentResourceCreated.getFullResourceName(), deployment.getMetadata().getName());
    }

    public Service findService(Deployment deployment) {
        Map<String, String> selectorLabels = deployment.getSpec().getSelector().getMatchLabels();
        String deploymentNamespace = deployment.getMetadata().getNamespace();
        ServiceList serviceList = kubernetesClient.services()
                .inNamespace(deploymentNamespace)
                .withLabels(selectorLabels)
                .list();
        if (serviceList.getItems().size() != 1) {
            LOG.error("Expected exactly one service, but got {}, {}", serviceList.getItems().size(), serviceList.getItems());
            throw new IllegalStateException("Expected exactly one service, but got " + serviceList.getItems().size());
        }
        return serviceList
                .getItems()
                .getFirst();
    }

    public String getBaseServiceUrl(Service service) {
        ServicePort servicePort = service.getSpec().getPorts().getFirst();
        String port = servicePort.getPort().toString();
        boolean isHttps = servicePort.getPort() == 443 || "https".equalsIgnoreCase(servicePort.getName());
        String protocol = isHttps ? "https://" : "http://";
        String url = protocol + service.getMetadata().getName() + ":" + port;
        LOG.info("Service URL is: {}", url);
        return url;
    }

    public String getServiceUrl(Deployment deployment) {
        String agentPath = deployment.getMetadata().getAnnotations().getOrDefault("wot.w3.org/td-endpoint", ".well-known/wot");
        String baseServiceUrl = getBaseServiceUrl(findService(deployment));
        if (agentPath.startsWith("/")) {
            return baseServiceUrl + agentPath;
        } else {
            return baseServiceUrl + "/" + agentPath;
        }
    }

    public void deleteAgentResource(Deployment deployment) {
        List<StatusDetails> deleteStatus = kubernetesClient.resources(AgentResource.class)
                .inNamespace(deployment.getMetadata().getNamespace())
                .withName(deployment.getMetadata().getName())
                .delete();
        LOG.info("AgentResource {} deleted for deployment: {}", deleteStatus, deployment.getMetadata().getName());
    }
}
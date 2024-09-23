/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.server.routing.impl;

import io.fabric8.kubernetes.client.KubernetesClient;
import ai.ancf.lmos.operator.resources.channel.ChannelResource;
import ai.ancf.lmos.operator.resources.routing.ChannelRoutingResource;
import ai.ancf.lmos.operator.server.routing.CustomResourcesService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class DefaultCustomResourcesService implements CustomResourcesService {

    private final KubernetesClient client;

    public DefaultCustomResourcesService(KubernetesClient client) {
        this.client = client;
    }
    @Override
    public Optional<ChannelRoutingResource> getRouting(String tenant, String channel, String subset) {
        Map<String, String> labelSelectors = Map.of(
                "tenant", tenant,
                "channel", channel,
                "subset", subset
        );

        var channelRoutingResources = client.resources(ChannelRoutingResource.class).withLabels(labelSelectors).list();

        if(channelRoutingResources.getItems().isEmpty()){
           return Optional.empty();
        }else {
            ChannelRoutingResource channelRoutingResource = channelRoutingResources.getItems().get(0);
            channelRoutingResource.getMetadata().setOwnerReferences(null);
            channelRoutingResource.getMetadata().setManagedFields(null);
            return Optional.of(channelRoutingResource);
        }
    }

    @Override
    public List<ChannelResource> getChannels(String tenant, String subset) {
        Map<String, String> labelSelectors = Map.of(
                "tenant", tenant,
                "subset", subset
        );

        var channelResources = client.resources(ChannelResource.class).withLabels(labelSelectors).list();
        channelResources.getItems().forEach(channelResource -> channelResource.getMetadata().setManagedFields(null));
        return channelResources.getItems();
    }

    @Override
    public Optional<ChannelResource> getChannel(String tenant, String channel, String subset) {
        Map<String, String> labelSelectors = Map.of(
                "tenant", tenant,
                "channel", channel,
                "subset", subset
        );

        var channelResources = client.resources(ChannelResource.class).withLabels(labelSelectors).list();

        if(channelResources.getItems().isEmpty()){
            return Optional.empty();
        }else {
            ChannelResource channelResource = channelResources.getItems().get(0);
            channelResource.getMetadata().setManagedFields(null);
            return Optional.of(channelResource);
        }
    }
}

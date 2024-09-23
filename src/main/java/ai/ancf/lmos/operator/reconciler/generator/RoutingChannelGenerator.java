/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.reconciler.generator;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import ai.ancf.lmos.operator.resolver.Wire;
import ai.ancf.lmos.operator.resources.agent.AgentResource;
import ai.ancf.lmos.operator.resources.channel.ChannelResource;
import ai.ancf.lmos.operator.resources.routing.CapabilityGroup;
import ai.ancf.lmos.operator.resources.routing.ChannelRoutingCapability;
import ai.ancf.lmos.operator.resources.routing.ChannelRoutingResource;
import ai.ancf.lmos.operator.resources.routing.ChannelRoutingSpec;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static ai.ancf.lmos.operator.resources.Labels.*;

public class RoutingChannelGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(RoutingChannelGenerator.class);

    public static ChannelRoutingResource createChannelRoutingResource(ChannelResource channelResource, Set<Wire<AgentResource>> wiredCapabilities) {
        var channelRoutingResource = createChannelRoutingResource(channelResource);
        var groupedWires = groupWiresByProvider(wiredCapabilities);
        var channelRoutingSpec = channelRoutingResource.getSpec();
        var capabilityGroups = new HashSet<CapabilityGroup>();
        groupedWires.forEach((agent, wires) -> {
            var capabilityGroup = new CapabilityGroup();
            capabilityGroup.setName(agent.getMetadata().getName());
            capabilityGroup.setDescription(agent.getSpec().getDescription());

            var capabilities = new HashSet<ChannelRoutingCapability>();
            wires.forEach(wire -> {
                var capabilityRoutingInfo = new ChannelRoutingCapability(wire);
                capabilities.add(capabilityRoutingInfo);
            });
            capabilityGroup.setCapabilities(capabilities);
            capabilityGroups.add(capabilityGroup);
        });

        channelRoutingSpec.setCapabilityGroups(capabilityGroups);
        channelRoutingResource.addOwnerReference(channelResource);
        return channelRoutingResource;
    }

    @NotNull
    public static ChannelRoutingResource createChannelRoutingResource(ChannelResource channelResource) {
        var metadata = channelResource.getMetadata();
        var channelResourceName = metadata.getName();
        Map<String, String> labels = metadata.getLabels();
        LOG.debug("Channel labels:" + labels);
        var channel = labels.get(CHANNEL);
        var tenant = labels.get(TENANT);
        var version = labels.get(VERSION);
        var subset = labels.get(SUBSET);
        var channelRoutingResource = new ChannelRoutingResource();
        var channelRoutingResourceMetadata = new ObjectMeta();
        channelRoutingResourceMetadata.setName(channelResourceName);
        channelRoutingResourceMetadata.setNamespace(metadata.getNamespace());
        channelRoutingResourceMetadata.setLabels(Map.of(CHANNEL, channel, TENANT, tenant, VERSION, version, SUBSET, subset));
        channelRoutingResource.setMetadata(channelRoutingResourceMetadata);
        var channelRoutingSpec = new ChannelRoutingSpec();
        channelRoutingResource.setSpec(channelRoutingSpec);
        return channelRoutingResource;
    }

    /**
     * Groups the wires by their provider (AgentResource).
     *
     * @param wiredCapabilities the set of wires to be grouped
     * @return a map where the key is the AgentResource and the value is a set of wires belonging to that AgentResource
     */
    protected static Map<AgentResource, Set<Wire<AgentResource>>> groupWiresByProvider(Set<Wire<AgentResource>> wiredCapabilities) {
        return wiredCapabilities.stream()
                .collect(Collectors.groupingBy(
                        Wire::getProvider,
                        Collectors.toSet()
                ));
    }


}

/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.data;

import ai.ancf.lmos.operator.resolver.ResolveStrategy;
import ai.ancf.lmos.operator.resolver.Wire;
import ai.ancf.lmos.operator.resolver.impl.AgentWire;
import ai.ancf.lmos.operator.resources.agent.AgentResource;
import ai.ancf.lmos.operator.resources.agent.AgentSpec;
import ai.ancf.lmos.operator.resources.agent.ProvidedCapability;
import ai.ancf.lmos.operator.resources.channel.ChannelResource;
import ai.ancf.lmos.operator.resources.channel.ChannelSpec;
import ai.ancf.lmos.operator.resources.channel.RequiredCapability;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class TestDataGenerator {

    @NotNull
    public static AgentResource createAgentResource(String version) {
        AgentResource agentResource = new AgentResource();
        AgentSpec agentSpec = new AgentSpec("description", Set.of("tenant1", "tenant2"), Set.of("channel1", "channel2"),
                Set.of(new ProvidedCapability("Capability1", version, "Capability description")));
        agentResource.setSpec(agentSpec);
        return agentResource;
    }

    @NotNull
    public static AgentResource createAgentResourceWithMultipleCapabilities(String version1, String version2) {
        AgentResource agentResource = new AgentResource();
        AgentSpec agentSpec = new AgentSpec("description", Set.of("tenant1", "tenant2"), Set.of("channel1", "channel2"),
                Set.of(
                        new ProvidedCapability("Capability1", version1, "Capability1 description"),
                        new ProvidedCapability("Capability2", version2, "Capability2 description")
                ));
        agentResource.setSpec(agentSpec);
        return agentResource;
    }
    @NotNull
    public static ChannelResource createTestChannelResource(String version) {
        ChannelResource channelResource = new ChannelResource();
        ChannelSpec channelSpec = new ChannelSpec();

        Set<RequiredCapability> requiredCapabilities = new HashSet<>();
        requiredCapabilities.add(new RequiredCapability("Capability1", version, ResolveStrategy.HIGHEST));

        channelSpec.setRequiredCapabilities(requiredCapabilities);
        channelResource.setSpec(channelSpec);

        return channelResource;
    }

    @NotNull
    public static ChannelResource createTestChannelResourceWithMultipleCapabilities(String version1, String version2, ResolveStrategy strategy) {
        ChannelResource channelResource = new ChannelResource();
        ChannelSpec channelSpec = new ChannelSpec();

        Set<RequiredCapability> requiredCapabilities = new HashSet<>();
        requiredCapabilities.add(new RequiredCapability("Capability1", version1, strategy));
        requiredCapabilities.add(new RequiredCapability("Capability2", version2, strategy));

        channelSpec.setRequiredCapabilities(requiredCapabilities);
        channelResource.setSpec(channelSpec);

        return channelResource;
    }

    @NotNull
    public static RequiredCapability createRequiredCapability() {
        return new RequiredCapability("capability1", ">=1.0.0");
    }

    @NotNull
    public static ProvidedCapability createProvidedCapability() {
        return new ProvidedCapability("capability1", "1.2.0", "description");
    }

    @NotNull
    public static Wire<AgentResource> createAgentWire() {
        return new AgentWire(createRequiredCapability(), createProvidedCapability(), createAgentResource("1.1.0"));
    }
}

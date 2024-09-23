/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.reconciler.generator;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import ai.ancf.lmos.operator.resolver.Wire;
import ai.ancf.lmos.operator.resolver.impl.AgentWire;
import ai.ancf.lmos.operator.resources.agent.AgentResource;
import ai.ancf.lmos.operator.resources.agent.AgentSpec;
import ai.ancf.lmos.operator.resources.agent.ProvidedCapability;
import ai.ancf.lmos.operator.resources.channel.ChannelResource;
import ai.ancf.lmos.operator.resources.channel.RequiredCapability;
import ai.ancf.lmos.operator.resources.routing.CapabilityGroup;
import ai.ancf.lmos.operator.resources.routing.ChannelRoutingResource;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static ai.ancf.lmos.operator.reconciler.generator.RoutingChannelGenerator.createChannelRoutingResource;
import static ai.ancf.lmos.operator.reconciler.generator.RoutingChannelGenerator.groupWiresByProvider;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class ChannelRoutingGeneratorTest {


    private static final String CHANNEL = "channel";
    private static final String TENANT = "tenant";
    private static final String VERSION = "version";
    private static final String SUBSET = "subset";

    private Set<Wire<AgentResource>> wiredCapabilities;
    private AgentResource agent1;
    private AgentResource agent2;

    @BeforeEach
    void setUp() {
        wiredCapabilities = new HashSet<>();

        agent1 = createAgentResource("Agent 1");
        agent2 = createAgentResource("Agent 2");

        wiredCapabilities.add(new AgentWire(new RequiredCapability("capability1", "1.0.0"), new ProvidedCapability("capability1", "1.0.0", "bla"), agent1));
        wiredCapabilities.add(new AgentWire(new RequiredCapability("capability2", "1.0.0"), new ProvidedCapability("capability2", "1.0.0", "bla"), agent1));
        wiredCapabilities.add(new AgentWire(new RequiredCapability("capability3", "1.0.0"), new ProvidedCapability("capability3", "1.0.0", "bla"), agent2));
    }

    @Test
    public void testGroupWiresByProvider(){
        var groupedWires = groupWiresByProvider(wiredCapabilities);

        assertThat(groupedWires).hasSize(2);

        assertThat(groupedWires)
                .hasSize(2)
                .containsOnlyKeys(agent1, agent2);

        assertThat(groupedWires.get(agent1))
                .hasSize(2)
                .extracting(wire -> wire.getProvidedCapability().getName())
                .containsExactlyInAnyOrder("capability1", "capability2");

        assertThat(groupedWires.get(agent2))
                .hasSize(1)
                .extracting(wire -> wire.getProvidedCapability().getName())
                .containsExactly("capability3");
    }

    @Test
    void testCreateChannelRoutingResource() {
        ObjectMeta metadata = new ObjectMeta();
        metadata.setName("testChannel");
        metadata.setNamespace("testNamespace");
        metadata.setLabels(Map.of(
                CHANNEL, "testChannel",
                TENANT, "testTenant",
                VERSION, "1.0",
                SUBSET, "testSubset"
        ));
        metadata.setUid("testUid");

        ChannelResource channelResource = new ChannelResource();
        channelResource.setMetadata(metadata);

        ChannelRoutingResource channelRoutingResource = createChannelRoutingResource(channelResource, wiredCapabilities);

        assertThat(channelRoutingResource).isNotNull();
        assertThat(channelRoutingResource.getMetadata()).isNotNull();
        assertThat(channelRoutingResource.getMetadata().getName()).isEqualTo(metadata.getName());
        assertThat(channelRoutingResource.getMetadata().getNamespace()).isEqualTo(metadata.getNamespace());
        assertThat(channelRoutingResource.getMetadata().getLabels())
                .containsEntry(CHANNEL, "testChannel")
                .containsEntry(TENANT, "testTenant")
                .containsEntry(VERSION, "1.0")
                .containsEntry(SUBSET, "testSubset");
        assertThat(channelRoutingResource.hasOwnerReferenceFor(channelResource)).isTrue();

        Set<CapabilityGroup> capabilityGroups = channelRoutingResource.getSpec().getCapabilityGroups();

        assertThat(capabilityGroups).hasSize(2);

        assertThat(capabilityGroups)
                .hasSize(2)
                .extracting(
                        CapabilityGroup::getName,
                        capabilityGroup -> capabilityGroup.getCapabilities().size(),
                        CapabilityGroup::getDescription
                )
                .containsExactlyInAnyOrder(
                        tuple("Agent 1", 2, "Bla"),
                        tuple("Agent 2", 1, "Bla")
                );
    }


    @NotNull
    private static AgentResource createAgentResource(String name) {
        AgentResource agent = new AgentResource();
        ObjectMeta objectMeta = new ObjectMeta();
        objectMeta.setName(name);
        objectMeta.setNamespace("testNamespace");
        agent.setMetadata(objectMeta);
        AgentSpec agentSpec = new AgentSpec();
        agentSpec.setDescription("Bla");
        agent.setSpec(agentSpec);
        return agent;
    }
}

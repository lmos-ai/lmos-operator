/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.resolver;

import ai.ancf.lmos.operator.data.TestDataGenerator;
import ai.ancf.lmos.operator.resolver.impl.AgentResolveContext;
import ai.ancf.lmos.operator.resources.agent.AgentResource;
import ai.ancf.lmos.operator.resources.channel.RequiredCapability;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AgentResolveContextTest {

    @Test
    public void shouldFindResourceWithSameVersion() {
        //Given
        AgentResource agentResource = TestDataGenerator.createAgentResource("1.0.0");

        //When
        AgentResolveContext agentResolveContext = new AgentResolveContext(List.of(agentResource));
        var wiredCapabilities = agentResolveContext.findCapabilityProviders(
                new RequiredCapability("Capability1", "1.0.0"));

        //Then
        assertThat(wiredCapabilities).hasSize(1);
        assertThat(wiredCapabilities).extracting("provider").contains(agentResource);
    }

    @Test
    public void shouldFindResourceWithHigherVersion() {
        //Given
        AgentResource agentResource = TestDataGenerator.createAgentResource("1.1.0");

        //When
        AgentResolveContext agentResolveContext = new AgentResolveContext(List.of(agentResource));
        var wiredCapabilities = agentResolveContext.findCapabilityProviders(
                new RequiredCapability("Capability1", ">=1.0.0"));

        //Then
        assertThat(wiredCapabilities).hasSize(1);
        assertThat(wiredCapabilities).extracting("provider").contains(agentResource);
    }

    @Test
    public void shouldNotFindResourceWithLowerVersion() {
        //Given
        AgentResource agentResource = TestDataGenerator.createAgentResource("1.0.0");

        //When
        AgentResolveContext agentResolveContext = new AgentResolveContext(List.of(agentResource));
        var wiredCapabilities = agentResolveContext.findCapabilityProviders(
                new RequiredCapability("Capability1", "1.1.0"));

        //Then
        assertThat(wiredCapabilities).isEmpty();
    }

    @Test
    public void shouldFindTwoResources() {
        //Given
        AgentResource agentResource1 = TestDataGenerator.createAgentResource("1.0.0");
        AgentResource agentResource2 = TestDataGenerator.createAgentResource("1.2.0");
        AgentResource agentResource3 = TestDataGenerator.createAgentResource("1.1.0");

        //When
        AgentResolveContext agentResolveContext = new AgentResolveContext(List.of(agentResource1, agentResource2, agentResource3));
        var wiredCapabilities = agentResolveContext.findCapabilityProviders(
                new RequiredCapability("Capability1", ">=1.1.0"));

        //Then
        assertThat(wiredCapabilities).hasSize(2);
        assertThat(wiredCapabilities).extracting("provider").contains(agentResource2, agentResource3);
    }

    @Test
    public void shouldDeduplicateSameAgentResource() {
        //Given
        AgentResource agentResource1 = TestDataGenerator.createAgentResource("1.1.0");
        AgentResource agentResource2 = TestDataGenerator.createAgentResource("1.1.0");

        //When
        AgentResolveContext agentResolveContext = new AgentResolveContext(List.of(agentResource1, agentResource2));
        var wiredCapabilities = agentResolveContext.findCapabilityProviders(
                new RequiredCapability("Capability1", "1.1.0"));

        //Then
        assertThat(wiredCapabilities).hasSize(1);
        assertThat(wiredCapabilities).extracting("provider").contains(agentResource2);
    }

    @Test
    public void shouldNotFindAnyResourceWithNonMatchingCapability() {
        //Given
        AgentResource agentResource = TestDataGenerator.createAgentResource("1.0.0");

        //When
        AgentResolveContext agentResolveContext = new AgentResolveContext(List.of(agentResource));
        var wiredCapabilities = agentResolveContext.findCapabilityProviders(
                new RequiredCapability("Nonexistent Capability", "1.0.0"));

        //Then
        assertThat(wiredCapabilities).isEmpty();
    }

    @Test
    public void shouldFindResourceWithMultipleCapabilities() {
        //Given
        AgentResource agentResource = TestDataGenerator.createAgentResourceWithMultipleCapabilities("1.0.0", "2.0.0");

        //When
        AgentResolveContext agentResolveContext = new AgentResolveContext(List.of(agentResource));
        var wiredCapabilities1 = agentResolveContext.findCapabilityProviders(
                new RequiredCapability("Capability1", "1.0.0"));
        var wiredCapabilities2 = agentResolveContext.findCapabilityProviders(
                new RequiredCapability("Capability2", "2.0.0"));

        //Then
        assertThat(wiredCapabilities1).hasSize(1).extracting("provider").contains(agentResource);
        assertThat(wiredCapabilities2).hasSize(1).extracting("provider").contains(agentResource);
    }


}

/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.resolver;

import ai.ancf.lmos.operator.data.TestDataGenerator;
import ai.ancf.lmos.operator.resolver.impl.AgentResolver;
import ai.ancf.lmos.operator.resolver.impl.AgentResolveContext;
import ai.ancf.lmos.operator.resources.agent.AgentResource;
import ai.ancf.lmos.operator.resources.channel.ChannelResource;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

public class AgentResolverTest {

    @Test
    public void shouldThrowResolverExceptionResolveContextIsEmpty() {
        //Given the resolve context is empty
        ChannelResource channelResource = TestDataGenerator.createTestChannelResource(">=1.1.0");
        var resolveContext = new AgentResolveContext(List.of());

        //When
        var resolver = new AgentResolver();
        ResolverException resolverException = catchThrowableOfType(() -> resolver.resolve(channelResource.getSpec().getRequiredCapabilities(), resolveContext),
                ResolverException.class);

        //Then
        assertThat(resolverException).hasMessageContaining("Resolve context is empty");
        assertThat(resolverException.getUnresolvedRequiredCapabilities()).hasSize(1);
    }


    @Test
    public void shouldThrowResolverExceptionWhenRequiredCapabilityNotResolved() {
        //Given the channel requires a capability but only an older version is provided
        ChannelResource channelResource = TestDataGenerator.createTestChannelResource(">=1.1.0");
        AgentResource agentResource = TestDataGenerator.createAgentResource("1.0.0");
        var resolveContext = new AgentResolveContext(List.of(agentResource));

        //When
        var resolver = new AgentResolver();
        ResolverException resolverException = catchThrowableOfType(() -> resolver.resolve(channelResource.getSpec().getRequiredCapabilities(), resolveContext),
                ResolverException.class);

        //Then
        assertThat(resolverException).hasMessageContaining("Required capabilities not resolved");
        assertThat(resolverException.getUnresolvedRequiredCapabilities()).hasSize(1);
    }

    @Test
    public void shouldThrowResolverExceptionWhenRequiredCapabilityNotResolved2() {
        //Given the channel requires two capabilities but only one is provided
        ChannelResource channelResource = TestDataGenerator.createTestChannelResourceWithMultipleCapabilities("1.1.0", "1.2.0", ResolveStrategy.HIGHEST);
        AgentResource agentResource = TestDataGenerator.createAgentResource("1.1.0");
        var resolveContext = new AgentResolveContext(List.of(agentResource));

        //When
        var resolver = new AgentResolver();
        ResolverException resolverException = catchThrowableOfType(() -> resolver.resolve(channelResource.getSpec().getRequiredCapabilities(), resolveContext),
                ResolverException.class);

        //Then
        assertThat(resolverException).hasMessageContaining("Required capabilities not resolved");
        assertThat(resolverException.getUnresolvedRequiredCapabilities()).hasSize(1);
    }


    @Test
    public void shouldWireCapabilityWithHighestVersion() throws ResolverException {
        //Given
        AgentResource agentResource1 = TestDataGenerator.createAgentResource("1.0.0");
        AgentResource agentResource2 = TestDataGenerator.createAgentResource("1.2.0");
        AgentResource agentResource3 = TestDataGenerator.createAgentResource("1.1.0");
        var resolveContext = new AgentResolveContext(List.of(agentResource1, agentResource2, agentResource3));

        ChannelResource channelResource = TestDataGenerator.createTestChannelResource(">=1.1.0");

        //When
        var resolver = new AgentResolver();
        var wiredCapabilities = resolver.resolve(channelResource.getSpec().getRequiredCapabilities(), resolveContext);

        //Then
        //One capability is wired with two agents
        assertThat(wiredCapabilities).hasSize(1);
        assertThat(wiredCapabilities.stream().toList().get(0).getProvider()).isEqualTo(agentResource2);
    }

    @Test
    public void shouldWireHighestVersions() throws ResolverException {
        //Given an agent with two capabilities
        AgentResource agentResource1 = TestDataGenerator.createAgentResourceWithMultipleCapabilities("1.1.0", "1.2.0");
        AgentResource agentResource2 = TestDataGenerator.createAgentResourceWithMultipleCapabilities("1.0.0", "1.1.0");
        var resolveContext = new AgentResolveContext(List.of(agentResource1, agentResource2));

        // Given a channel which requires the two capabilities
        ChannelResource channelResource = TestDataGenerator.createTestChannelResourceWithMultipleCapabilities("1.1.0", ">=1.1.0", ResolveStrategy.HIGHEST);

        //When
        var resolver = new AgentResolver();
        var wiredCapabilities = resolver.resolve(channelResource.getSpec().getRequiredCapabilities(), resolveContext);

        //Then
        //Two capabilities are wired with a single agent
        assertThat(wiredCapabilities).hasSize(2);
        assertThat(wiredCapabilities.stream().toList().get(0).getProvider()).isEqualTo(agentResource1);
        assertThat(wiredCapabilities.stream().toList().get(1).getProvider()).isEqualTo(agentResource1);
    }

    @Test
    public void shouldWireMostSimilarVersions() throws ResolverException {
        //Given an agent with two capabilities
        AgentResource agentResource1 = TestDataGenerator.createAgentResourceWithMultipleCapabilities("1.2.0", "1.2.0");
        AgentResource agentResource2 = TestDataGenerator.createAgentResourceWithMultipleCapabilities("1.1.5", "1.1.1");
        var resolveContext = new AgentResolveContext(List.of(agentResource1, agentResource2));

        // Given a channel which requires the two capabilities
        ChannelResource channelResource = TestDataGenerator.createTestChannelResourceWithMultipleCapabilities(">=1.1.0", ">=1.1.0", ResolveStrategy.MOST_SIMILAR);

        //When
        var resolver = new AgentResolver();
        var wiredCapabilities = resolver.resolve(channelResource.getSpec().getRequiredCapabilities(), resolveContext);

        //Then
        //Two capabilities are wired with a single agent
        assertThat(wiredCapabilities).hasSize(2);
        assertThat(wiredCapabilities.stream().toList().get(0).getProvider()).isEqualTo(agentResource2);
        assertThat(wiredCapabilities.stream().toList().get(1).getProvider()).isEqualTo(agentResource2);
    }
}

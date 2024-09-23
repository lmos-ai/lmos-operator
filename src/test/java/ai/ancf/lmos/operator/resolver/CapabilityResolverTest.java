/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.resolver;

import ai.ancf.lmos.operator.resolver.ResolveStrategy;
import ai.ancf.lmos.operator.resolver.Wire;
import ai.ancf.lmos.operator.resolver.impl.CapabilityResolver;
import ai.ancf.lmos.operator.resolver.impl.AgentWire;
import ai.ancf.lmos.operator.resources.agent.AgentResource;
import ai.ancf.lmos.operator.resources.agent.ProvidedCapability;
import ai.ancf.lmos.operator.resources.channel.RequiredCapability;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class CapabilityResolverTest {

    @Test
    public void testFindHighestVersion(){
        Set<Wire<AgentResource>> wires = new HashSet<>();

        AgentWire bestMatchingWire = new AgentWire(new RequiredCapability("capability1", "1.0.0"), new ProvidedCapability("capability1", "1.3.0", "description"), new AgentResource());

        // Add test data to the wires set
        wires.add(new AgentWire(new RequiredCapability("capability1", ">=1.0.0"), new ProvidedCapability("capability1", "1.2.0", "description"), new AgentResource()));
        wires.add(bestMatchingWire);
        wires.add(new AgentWire(new RequiredCapability("capability1", ">=1.0.0"), new ProvidedCapability("capability1", "1.1.0", "description"), new AgentResource()));

        Optional<Wire<AgentResource>> bestWire = CapabilityResolver.findBestMatchingWire(wires, ResolveStrategy.HIGHEST);

        assertThat(bestWire).isPresent().contains(bestMatchingWire);
    }

    @Test
    public void testFindMostSimilarVersion(){
        Set<Wire<AgentResource>> wires = new HashSet<>();

        AgentWire bestMatchingWire = new AgentWire(new RequiredCapability("capability1", "1.0.0"), new ProvidedCapability("capability1", "1.0.1", "description"), new AgentResource());

        // Add test data to the wires set
        wires.add(new AgentWire(new RequiredCapability("capability1", ">=1.0.0"), new ProvidedCapability("capability1", "1.2.0", "description"), new AgentResource()));
        wires.add(bestMatchingWire);
        wires.add(new AgentWire(new RequiredCapability("capability1", ">=1.0.0"), new ProvidedCapability("capability1", "1.1.0", "description"), new AgentResource()));

        Optional<Wire<AgentResource>> bestWire = CapabilityResolver.findBestMatchingWire(wires, ResolveStrategy.MOST_SIMILAR);

        assertThat(bestWire).isPresent().contains(bestMatchingWire);
    }

}

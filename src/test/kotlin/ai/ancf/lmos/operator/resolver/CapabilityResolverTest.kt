/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.resolver

import ai.ancf.lmos.operator.resolver.impl.CapabilityResolver.findBestMatchingWire
import ai.ancf.lmos.operator.resources.AgentResource
import ai.ancf.lmos.operator.resources.ProvidedCapability
import ai.ancf.lmos.operator.resources.RequiredCapability
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class CapabilityResolverTest {
    @Test
    fun testFindHighestVersion() {
        val wires: MutableSet<Wire<AgentResource>> = HashSet()

        val bestMatchingWire =
            Wire(
                RequiredCapability("capability1", "1.0.0"),
                ProvidedCapability("capability1", "1.3.0", "description"),
                AgentResource(),
            )

        // Add test data to the wires set
        wires.add(
            Wire(
                RequiredCapability("capability1", ">=1.0.0"),
                ProvidedCapability("capability1", "1.2.0", "description"),
                AgentResource(),
            ),
        )
        wires.add(bestMatchingWire)
        wires.add(
            Wire(
                RequiredCapability("capability1", ">=1.0.0"),
                ProvidedCapability("capability1", "1.1.0", "description"),
                AgentResource(),
            ),
        )

        val bestWire: Wire<AgentResource>? = findBestMatchingWire(wires, ResolveStrategy.HIGHEST)

        Assertions.assertThat(bestWire!!).isEqualTo(bestMatchingWire)
    }

    @Test
    fun testFindMostSimilarVersion() {
        val wires: MutableSet<Wire<AgentResource>> = HashSet()

        val bestMatchingWire =
            Wire(
                RequiredCapability("capability1", "1.0.0"),
                ProvidedCapability("capability1", "1.0.1", "description"),
                AgentResource(),
            )

        // Add test data to the wires set
        wires.add(
            Wire(
                RequiredCapability("capability1", ">=1.0.0"),
                ProvidedCapability("capability1", "1.2.0", "description"),
                AgentResource(),
            ),
        )
        wires.add(bestMatchingWire)
        wires.add(
            Wire(
                RequiredCapability("capability1", ">=1.0.0"),
                ProvidedCapability("capability1", "1.1.0", "description"),
                AgentResource(),
            ),
        )

        val bestWire = findBestMatchingWire(wires, ResolveStrategy.MOST_SIMILAR)

        Assertions.assertThat(bestWire).isEqualTo(bestMatchingWire)
    }
}

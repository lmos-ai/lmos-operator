/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.resolver.impl;

import ai.ancf.lmos.operator.resolver.ResolveStrategy;
import ai.ancf.lmos.operator.resolver.Wire;
import ai.ancf.lmos.operator.resources.agent.AgentResource;
import org.semver4j.Semver;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

public class CapabilityResolver {

    /**
     * Finds the best matching Wire based on the resolve strategy.
     *
     * @param wires           the set of wires to filter
     * @param resolveStrategy the resolve strategy to use
     * @return the best matching Wire
     */
    public static Optional<Wire<AgentResource>> findBestMatchingWire(Set<Wire<AgentResource>> wires, ResolveStrategy resolveStrategy) {
        if (resolveStrategy == ResolveStrategy.MOST_SIMILAR) {
            // Implement the logic for MOST_SIMILAR strategy here
            return wires.stream()
                    .min(Comparator.comparing(w -> new Semver(w.getProvidedCapability().getVersion())));
        } else {
            return wires.stream()
                    .max(Comparator.comparing(w -> new Semver(w.getProvidedCapability().getVersion())));
        }
    }
}
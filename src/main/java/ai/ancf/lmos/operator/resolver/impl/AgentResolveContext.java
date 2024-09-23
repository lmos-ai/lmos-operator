/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.resolver.impl;

import ai.ancf.lmos.operator.resolver.ResolveContext;
import ai.ancf.lmos.operator.resolver.Wire;
import ai.ancf.lmos.operator.resources.agent.AgentResource;
import ai.ancf.lmos.operator.resources.agent.ProvidedCapability;
import ai.ancf.lmos.operator.resources.channel.RequiredCapability;
import org.jetbrains.annotations.NotNull;
import org.semver4j.Semver;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class AgentResolveContext implements ResolveContext<AgentResource> {
    private final List<AgentResource> agentResources;

    public AgentResolveContext(List<AgentResource> agentResources) {
        super();
        this.agentResources = agentResources;
    }

    @Override
    public Collection<AgentResource> getAvailableResources() {
        return agentResources;
    }


    /**
     * Finds the capability providers that can fulfill the given required capability.
     *
     * @param requiredCapability the required capability to be matched against provided capabilities
     * @return a set of {@link Wire <AgentResource>} objects representing the connections between the required capability and the provided capabilities that meet the requirements
     */
    @Override
    @NotNull
    public Set<Wire<AgentResource>> findCapabilityProviders(RequiredCapability requiredCapability) {
        var wiredCapabilities = new HashSet<Wire<AgentResource>>();

        for(var agentResource: agentResources){
            var providedCapabilities = agentResource.getSpec().getProvidedCapabilities();
            for(var providedCapability : providedCapabilities){
                if(isCapabilityNameEqual(requiredCapability, providedCapability)){
                    if(isProvidedCapabilityVersionGreaterThanOrEqualTo(providedCapability, requiredCapability.getVersion())){
                        wiredCapabilities.add(new AgentWire(requiredCapability, providedCapability, agentResource));
                    }
                }
            }
        }
        return wiredCapabilities;
    }


    /**
     * Checks if the provided capability's version is greater than or equal to the required capability's version.
     *
     * @param providedCapability the provided capability
     * @param requiredCapabilityVersion the required capability version
     * @return true if the provided capability's version is greater than or equal to the required capability's version, false otherwise
     */
    private static boolean isProvidedCapabilityVersionGreaterThanOrEqualTo(ProvidedCapability providedCapability, String requiredCapabilityVersion) {
        return new Semver(providedCapability.getVersion()).satisfies(requiredCapabilityVersion);
    }

    /**
     * Checks if the names of the required capability and provided capability are equal.
     *
     * @param requiredCapability the required capability
     * @param providedCapability the provided capability
     * @return true if the names are equal, false otherwise
     */
    private static boolean isCapabilityNameEqual(RequiredCapability requiredCapability, ProvidedCapability providedCapability) {
        return providedCapability.getName().equals(requiredCapability.getName());
    }
}

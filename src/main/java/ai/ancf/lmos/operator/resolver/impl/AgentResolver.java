/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.resolver.impl;

import ai.ancf.lmos.operator.resolver.*;
import ai.ancf.lmos.operator.resources.agent.AgentResource;
import ai.ancf.lmos.operator.resources.channel.RequiredCapability;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class AgentResolver implements Resolver<AgentResource> {

    private static final Logger LOG = LoggerFactory.getLogger(AgentResolver.class);

    @Override
    @NotNull
    public Set<Wire<AgentResource>> resolve(Set<RequiredCapability> requiredCapabilities, ResolveContext<AgentResource> resolveContext) throws ResolverException {

        var agentResources = resolveContext.getAvailableResources();

        if(agentResources == null || agentResources.isEmpty()){
            throw new ResolverException("Resolve context is empty", requiredCapabilities);
        }

        var bestMatchingCapabilities = new HashSet<Wire<AgentResource>>();
        var unresolvedRequiredCapabilities = new HashSet<RequiredCapability>();
        requiredCapabilities.forEach(
                requiredCapability -> {
                    Set<Wire<AgentResource>> wireCapabilities = resolveContext.findCapabilityProviders(requiredCapability);
                    if(wireCapabilities.isEmpty()){
                        unresolvedRequiredCapabilities.add(requiredCapability);
                    }else{
                        ResolveStrategy strategy = requiredCapability.getStrategy();
                        var bestMatchingWire = CapabilityResolver.findBestMatchingWire(wireCapabilities, strategy);
                        if(bestMatchingWire.isPresent()){
                            bestMatchingCapabilities.add(bestMatchingWire.get());
                        }else{
                            unresolvedRequiredCapabilities.add(requiredCapability);
                        }
                    }
                }
        );
        if(!unresolvedRequiredCapabilities.isEmpty()){
            throw new ResolverException("Required capabilities not resolved", unresolvedRequiredCapabilities);
        }

        return bestMatchingCapabilities;
    }

}

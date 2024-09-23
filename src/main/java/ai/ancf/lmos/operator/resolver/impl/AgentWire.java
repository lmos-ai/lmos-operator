/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.resolver.impl;

import ai.ancf.lmos.operator.resolver.Wire;
import ai.ancf.lmos.operator.resources.agent.AgentResource;
import ai.ancf.lmos.operator.resources.agent.ProvidedCapability;
import ai.ancf.lmos.operator.resources.channel.RequiredCapability;

import java.util.Objects;

public class AgentWire implements Wire<AgentResource> {

    private final RequiredCapability requiredCapability;
    private final ProvidedCapability providedCapability;
    private final AgentResource agentResource;

    public AgentWire(RequiredCapability requiredCapability,
                     ProvidedCapability providedCapability,
                     AgentResource agentResource
    ) {

        this.requiredCapability = requiredCapability;
        this.providedCapability = providedCapability;
        this.agentResource = agentResource;
    }

    @Override
    public ProvidedCapability getProvidedCapability() {
        return providedCapability;
    }

    @Override
    public RequiredCapability getRequiredCapability() {
        return requiredCapability;
    }

    @Override
    public AgentResource getProvider() {
        return agentResource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgentWire agentWire = (AgentWire) o;
        return Objects.equals(requiredCapability, agentWire.requiredCapability) && Objects.equals(providedCapability, agentWire.providedCapability) && Objects.equals(agentResource, agentWire.agentResource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requiredCapability, providedCapability, agentResource);
    }
}

/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.resources.agent;

import java.util.Objects;
import java.util.Set;

public class AgentSpec {
    private String description;
    private Set<String> supportedTenants;
    private Set<String> supportedChannels;
    private Set<ProvidedCapability> providedCapabilities;

    public AgentSpec() {
    }

    public AgentSpec(String description, Set<String> supportedTenants, Set<String> supportedChannels, Set<ProvidedCapability> providedCapabilities) {
        this.description = description;
        this.supportedTenants = supportedTenants;
        this.supportedChannels = supportedChannels;
        this.providedCapabilities = providedCapabilities;
    }

    // Getters and setters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getSupportedTenants() {
        return supportedTenants;
    }

    public void setSupportedTenants(Set<String> supportedTenants) {
        this.supportedTenants = supportedTenants;
    }

    public Set<String> getSupportedChannels() {
        return supportedChannels;
    }

    public void setSupportedChannels(Set<String> supportedChannels) {
        this.supportedChannels = supportedChannels;
    }

    public Set<ProvidedCapability> getProvidedCapabilities() {
        return providedCapabilities;
    }

    public void setProvidedCapabilities(Set<ProvidedCapability> providedCapabilities) {
        this.providedCapabilities = providedCapabilities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgentSpec agentSpec = (AgentSpec) o;
        return Objects.equals(description, agentSpec.description) && Objects.equals(supportedTenants, agentSpec.supportedTenants) && Objects.equals(supportedChannels, agentSpec.supportedChannels) && Objects.equals(providedCapabilities, agentSpec.providedCapabilities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, supportedTenants, supportedChannels, providedCapabilities);
    }
}

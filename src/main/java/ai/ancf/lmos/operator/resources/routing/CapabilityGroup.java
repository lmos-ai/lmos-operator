/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.resources.routing;

import io.fabric8.generator.annotation.Required;

import java.util.Objects;
import java.util.Set;

public class CapabilityGroup {

    @Required
    private String name;
    private String description;
    @Required
    private Set<ChannelRoutingCapability> capabilities;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<ChannelRoutingCapability> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(Set<ChannelRoutingCapability> capabilities) {
        this.capabilities = capabilities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CapabilityGroup that = (CapabilityGroup) o;
        return Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(capabilities, that.capabilities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, capabilities);
    }
}

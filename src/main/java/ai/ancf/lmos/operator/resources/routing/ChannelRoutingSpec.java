/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.resources.routing;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ChannelRoutingSpec {
    private Set<CapabilityGroup> capabilityGroups = new HashSet<>();


    // Getters and setters
    public Set<CapabilityGroup> getCapabilityGroups() {
        return capabilityGroups;
    }

    public void setCapabilityGroups(Set<CapabilityGroup> capabilityGroups) {
        this.capabilityGroups = capabilityGroups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChannelRoutingSpec that = (ChannelRoutingSpec) o;
        return Objects.equals(capabilityGroups, that.capabilityGroups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(capabilityGroups);
    }
}

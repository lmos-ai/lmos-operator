/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.resources.channel;

import io.fabric8.crd.generator.annotation.PrinterColumn;

import java.util.Set;

public class ChannelStatus {

    @PrinterColumn(name = "RESOLVE_STATUS")
    private ResolveStatus resolveStatus = ResolveStatus.UNRESOLVED;

    private Set<RequiredCapability> unresolvedRequiredCapabilities;

    public ChannelStatus() {
    }

    public ChannelStatus(ResolveStatus resolveStatus) {
        this.resolveStatus = resolveStatus;
    }

    public ChannelStatus(ResolveStatus resolveStatus, Set<RequiredCapability> unresolvedRequiredCapabilities) {
        this.resolveStatus = resolveStatus;
        this.unresolvedRequiredCapabilities = unresolvedRequiredCapabilities;
    }

    public ResolveStatus getResolveStatus() {
        return resolveStatus;
    }

    public void setResolveStatus(ResolveStatus resolveStatus) {
        this.resolveStatus = resolveStatus;
    }

    public Set<RequiredCapability> getUnresolvedRequiredCapabilities() {
        return unresolvedRequiredCapabilities;
    }

    public void setUnresolvedRequiredCapabilities(Set<RequiredCapability> unresolvedRequiredCapabilities) {
        this.unresolvedRequiredCapabilities = unresolvedRequiredCapabilities;
    }
}

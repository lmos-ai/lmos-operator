/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.resources.routing;

import io.fabric8.crd.generator.annotation.PrinterColumn;
import ai.ancf.lmos.operator.resources.channel.RequiredCapability;
import ai.ancf.lmos.operator.resources.channel.ResolveStatus;

import java.util.List;

public class ChannelRoutingStatus {

    @PrinterColumn(name = "RESOLVE_STATUS")
    private ResolveStatus resolveStatus = ResolveStatus.UNRESOLVED;

    private List<RequiredCapability> unresolvedRequiredCapabilities;

    public ChannelRoutingStatus() {
    }

    public ChannelRoutingStatus(ResolveStatus resolveStatus) {
        this.resolveStatus = resolveStatus;
    }

    public ChannelRoutingStatus(ResolveStatus resolveStatus, List<RequiredCapability> unresolvedRequiredCapabilities) {
        this.resolveStatus = resolveStatus;
        this.unresolvedRequiredCapabilities = unresolvedRequiredCapabilities;
    }

    public ResolveStatus getResolveStatus() {
        return resolveStatus;
    }

    public void setResolveStatus(ResolveStatus resolveStatus) {
        this.resolveStatus = resolveStatus;
    }

    public List<RequiredCapability> getUnresolvedRequiredCapabilities() {
        return unresolvedRequiredCapabilities;
    }

    public void setUnresolvedRequiredCapabilities(List<RequiredCapability> unresolvedRequiredCapabilities) {
        this.unresolvedRequiredCapabilities = unresolvedRequiredCapabilities;
    }
}

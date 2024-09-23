/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.resources.channel;

import io.fabric8.generator.annotation.Required;

import java.util.Set;

public class ChannelSpec {

    @Required
    private Set<RequiredCapability> requiredCapabilities;

    public Set<RequiredCapability> getRequiredCapabilities() {
        return requiredCapabilities;
    }

    public void setRequiredCapabilities(Set<RequiredCapability> requiredCapabilities) {
        this.requiredCapabilities = requiredCapabilities;
    }
}

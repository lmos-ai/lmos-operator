/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.resources.rollout;

public class Canary {
    private CanaryChannel canaryChannel;
    private StableChannel stableChannel;

    // Getters and setters
    public CanaryChannel getCanaryChannel() {
        return canaryChannel;
    }

    public void setCanaryChannel(CanaryChannel canaryChannel) {
        this.canaryChannel = canaryChannel;
    }

    public StableChannel getStableChannel() {
        return stableChannel;
    }

    public void setStableChannel(StableChannel stableChannel) {
        this.stableChannel = stableChannel;
    }
}
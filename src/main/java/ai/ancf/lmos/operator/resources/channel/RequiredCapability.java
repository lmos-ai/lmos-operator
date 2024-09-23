/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.resources.channel;

import ai.ancf.lmos.operator.resolver.ResolveStrategy;
import ai.ancf.lmos.operator.resources.agent.Capability;

import java.util.Objects;

public class RequiredCapability extends Capability {
    private ResolveStrategy strategy = ResolveStrategy.HIGHEST;

    public RequiredCapability() {
    }

    public RequiredCapability(String name, String version) {
        super(name, version);
    }

    public RequiredCapability(String name, String version, ResolveStrategy strategy) {
        super(name, version);
        this.strategy = strategy;
    }


    public ResolveStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(ResolveStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RequiredCapability that = (RequiredCapability) o;
        return strategy == that.strategy;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), strategy);
    }
}

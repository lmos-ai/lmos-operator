/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.resolver;

import ai.ancf.lmos.operator.resources.agent.AgentResource;
import ai.ancf.lmos.operator.resources.agent.ProvidedCapability;
import ai.ancf.lmos.operator.resources.channel.RequiredCapability;

/**
 * A wire connecting a {@link AgentResource} to a {@link RequiredCapability}.
 *
 * <p>
 * Instances of this type must be <i>effectively immutable</i>. That is, for a
 * given instance of this interface, the methods defined by this interface must
 * always return the same result.

 */
public interface Wire<P> {
    /**
     * Returns the {@link ProvidedCapability} for this wire.
     *
     * @return The {@link ProvidedCapability} for this wire.
     */
    ProvidedCapability getProvidedCapability();

    /**
     * Returns the {@link RequiredCapability} for this wire.
     *
     * @return The {@link RequiredCapability} for this wire.
     */
    RequiredCapability getRequiredCapability();

    /**
     * Returns the resource providing the {@link #getProvidedCapability() capability}.
     *
     * @return The resource providing the capability.
     */
    P getProvider();
}

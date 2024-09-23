/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.resolver;


import ai.ancf.lmos.operator.resources.channel.RequiredCapability;

import java.util.Collection;
import java.util.Set;

public interface ResolveContext<P> {

    /**
     * Return the resources that are part of this resolve context.
     *
     * @return A collection of the resources are part for this
     *         resolve context. May be empty if there are no available
     *         resources. The returned collection may be unmodifiable.
     */
    Collection<P> getAvailableResources();


    /**
     * Find resources that provide the given required capability.
     *
     * @param requiredCapability The required capability that a resolver is attempting to satisfy.
     *
     * @return A list of Capability objects that match the specified requirement.
     */
    Set<Wire<P>> findCapabilityProviders(RequiredCapability requiredCapability);

}

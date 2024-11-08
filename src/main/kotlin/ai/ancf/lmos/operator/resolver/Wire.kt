/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.resolver

import ai.ancf.lmos.operator.resources.AgentResource
import ai.ancf.lmos.operator.resources.ProvidedCapability
import ai.ancf.lmos.operator.resources.RequiredCapability

/**
 * A wire connecting a [AgentResource] to a [RequiredCapability].
 */
data class Wire<P>(
    /**
     * The [RequiredCapability] for this wire.
     */
    val requiredCapability: RequiredCapability,
    /**
     * The [ProvidedCapability] for this wire.
     */
    val providedCapability: ProvidedCapability,
    /**
     * The resource providing the [capability][.getProvidedCapability].
     */
    val provider: P,
    // val provider: AgentResource
)

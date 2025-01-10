/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.eclipse.lmos.operator.resolver

import org.eclipse.lmos.operator.resources.RequiredCapability
import java.util.Collections

/**
 * Indicates failure to resolve a set of required capabilities.
 *
 * If a resolution failure is caused by a missing mandatory dependency a
 * resolver may include any requirements it has considered in the resolution
 * exception. Clients may access this set of dependencies via the
 * [.getUnresolvedRequiredCapabilities] method.
 */
class ResolverException : Exception {
    private val unresolvedRequiredCapabilities: Set<RequiredCapability>

    /**
     * Create a `ResolutionException` with the specified unresolved required capabilities.
     *
     * @param unresolvedRequiredCapabilities The unresolved required capabilities.
     */
    constructor(unresolvedRequiredCapabilities: Set<RequiredCapability>) : super(
        "Required capabilities not resolved: ${unresolvedRequiredCapabilities.joinToString(", ") { it.toString() }}"
    ) {
        this.unresolvedRequiredCapabilities = Collections.unmodifiableSet(unresolvedRequiredCapabilities)
    }

    /**
     * Create a `ResolutionException` with the specified cause.
     *
     * @param cause The cause of this exception.
     */
    constructor(cause: Throwable?) : super(cause) {
        this.unresolvedRequiredCapabilities = emptySet()
    }

    /**
     * Create a `ResolutionException` with the specified message, cause, and unresolved required capabilities.
     *
     * @param message The message.
     * @param cause The cause of this exception.
     * @param unresolvedRequiredCapabilities The unresolved required capabilities.
     */
    constructor(message: String, cause: Throwable, unresolvedRequiredCapabilities: Set<RequiredCapability>) : super(
        message,
        cause
    ) {
        this.unresolvedRequiredCapabilities = Collections.unmodifiableSet(unresolvedRequiredCapabilities)
    }

    /**
     * Return the unresolved required capabilities, if any, for this exception.
     *
     * @return A collection of the unresolved required capabilities for this exception.
     * The returned collection may be empty if no unresolved
     * requirements information is available.
     */
    fun getUnresolvedRequiredCapabilities(): Set<RequiredCapability> {
        return unresolvedRequiredCapabilities
    }
}

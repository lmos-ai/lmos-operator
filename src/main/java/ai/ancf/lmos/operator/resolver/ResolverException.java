/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.resolver;

import ai.ancf.lmos.operator.resources.channel.RequiredCapability;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Set;


/**
 * Indicates failure to resolve a set of required capabilities.
 *
 * <p>
 * If a resolution failure is caused by a missing mandatory dependency a
 * resolver may include any requirements it has considered in the resolution
 * exception. Clients may access this set of dependencies via the
 * {@link #getUnresolvedRequiredCapabilities()} method.
 *
 *
 */
public class ResolverException extends Exception {

    private final Set<RequiredCapability> unresolvedRequiredCapabilities;

    /**
     * Create a {@code ResolutionException} with the specified message, cause
     * and unresolved requirements.
     *
     * @param message The message.
     * @param cause The cause of this exception.
     * @param unresolvedRequiredCapabilities The unresolved required capabilities from
     *        mandatory resources or {@code null} if no unresolved requirements
     *        information is provided.
     */
    public ResolverException(String message, Throwable cause, Set<RequiredCapability> unresolvedRequiredCapabilities) {
        super(message, cause);
        if ((unresolvedRequiredCapabilities == null) || unresolvedRequiredCapabilities.isEmpty()) {
            this.unresolvedRequiredCapabilities = null;
        } else {
            this.unresolvedRequiredCapabilities = Collections.unmodifiableSet(unresolvedRequiredCapabilities);
        }
    }

    /**
     * Create a {@code ResolutionException} with the specified message.
     *
     * @param message The message.
     */
    public ResolverException(String message, Set<RequiredCapability> unresolvedRequiredCapabilities) {
        super(message);
        this.unresolvedRequiredCapabilities = unresolvedRequiredCapabilities;
    }

    /**
     * Create a {@code ResolutionException} with the specified cause.
     *
     * @param cause The cause of this exception.
     */
    public ResolverException(Throwable cause) {
        super(cause);
        unresolvedRequiredCapabilities = null;
    }

    @SuppressWarnings("unchecked")
    private static List<RequiredCapability> emptyCollection() {
        return Collections.EMPTY_LIST;
    }

    /**
     * Return the unresolved required capabilities, if any, for this exception.
     *
     * @return A collection of the unresolved required capabilities for this exception.
     *         The returned collection may be empty if no unresolved
     *         requirements information is available.
     */
    @NotNull
    public Set<RequiredCapability> getUnresolvedRequiredCapabilities() {
        return (unresolvedRequiredCapabilities != null) ? unresolvedRequiredCapabilities : Collections.emptySet();
    }
}
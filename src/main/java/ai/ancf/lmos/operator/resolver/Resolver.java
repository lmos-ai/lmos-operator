/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.resolver;

import ai.ancf.lmos.operator.resources.agent.AgentResource;
import ai.ancf.lmos.operator.resources.channel.RequiredCapability;

import java.util.Set;

public interface Resolver< P> {

    Set<Wire<AgentResource>> resolve(Set<RequiredCapability> requiredCapabilities, ResolveContext<P> resolveContext) throws ResolverException;
}

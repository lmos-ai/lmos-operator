/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.server.routing;

import ai.ancf.lmos.operator.resources.channel.ChannelResource;
import ai.ancf.lmos.operator.resources.routing.ChannelRoutingResource;

import java.util.List;
import java.util.Optional;

public interface CustomResourcesService {

    Optional<ChannelRoutingResource> getRouting(String tenant, String channel, String subset);

    List<ChannelResource> getChannels(String tenant, String subset);

    Optional<ChannelResource> getChannel(String tenant, String channel, String subset);
}

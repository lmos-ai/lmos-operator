/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.server.routing

import ai.ancf.lmos.operator.resources.ChannelResource
import ai.ancf.lmos.operator.resources.ChannelRoutingResource

interface CustomResourcesService {
    fun getRouting(
        tenant: String,
        channel: String,
        subset: String,
    ): ChannelRoutingResource?

    fun getChannels(
        tenant: String,
        subset: String,
    ): List<ChannelResource>

    fun getChannel(
        tenant: String,
        channel: String,
        subset: String,
    ): ChannelResource?
}

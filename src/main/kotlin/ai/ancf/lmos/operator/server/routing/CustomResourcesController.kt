/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.server.routing

import ai.ancf.lmos.operator.resources.ChannelResource
import ai.ancf.lmos.operator.resources.ChannelRoutingResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

const val X_SUBSET_HEADER: String = "x-subset"

@RestController
@RequestMapping("/apis/v1/tenants/{tenant}", produces = [MediaType.APPLICATION_JSON_VALUE])
class CustomResourcesController(private val customResourcesService: CustomResourcesService) {
    @GetMapping("/channels")
    fun getRouting(
        @RequestHeader(name = X_SUBSET_HEADER) subsetHeader: String,
        @PathVariable tenant: String,
    ) = customResourcesService.getChannels(tenant, subsetHeader)

    @GetMapping("/channels/{channel}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getChannel(
        @RequestHeader(name = X_SUBSET_HEADER) subsetHeader: String,
        @PathVariable tenant: String,
        @PathVariable channel: String,
    ): ResponseEntity<ChannelResource> =
        customResourcesService.getChannel(tenant, channel, subsetHeader)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()

    @GetMapping("/channels/{channel}/routing", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getRouting(
        @RequestHeader(name = X_SUBSET_HEADER) subsetHeader: String,
        @PathVariable tenant: String,
        @PathVariable channel: String,
    ): ResponseEntity<ChannelRoutingResource> =
        customResourcesService.getRouting(tenant, channel, subsetHeader)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
}

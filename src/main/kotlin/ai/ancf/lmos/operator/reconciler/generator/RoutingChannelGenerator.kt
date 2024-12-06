/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.reconciler.generator

import ai.ancf.lmos.operator.resolver.Wire
import ai.ancf.lmos.operator.resources.AgentResource
import ai.ancf.lmos.operator.resources.CapabilityGroup
import ai.ancf.lmos.operator.resources.ChannelResource
import ai.ancf.lmos.operator.resources.ChannelRoutingCapability
import ai.ancf.lmos.operator.resources.ChannelRoutingResource
import ai.ancf.lmos.operator.resources.ChannelRoutingSpec
import ai.ancf.lmos.operator.resources.Labels
import io.fabric8.kubernetes.api.model.ObjectMeta
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val LOG: Logger = LoggerFactory.getLogger(RoutingChannelGenerator::class.java)

object RoutingChannelGenerator {
    fun createChannelRoutingResource(
        channelResource: ChannelResource,
        wiredCapabilities: Set<Wire<AgentResource>>,
        subset: String,
    ): ChannelRoutingResource {
        val channelRoutingResource = createChannelRoutingResource(channelResource)
        val groupedWires = groupWiresByProvider(wiredCapabilities)
        val channelRoutingSpec = channelRoutingResource.spec
        val capabilityGroups = mutableSetOf<CapabilityGroup>()
        groupedWires.forEach { (agent: AgentResource, wires: Set<Wire<AgentResource>>) ->
            val capabilityGroup =
                CapabilityGroup(
                    name = agent.metadata.name,
                    capabilities = wires.map { ChannelRoutingCapability(it, subset) }.toSet(),
                    description = agent.spec.description,
                )
            capabilityGroups.add(capabilityGroup)
        }

        channelRoutingSpec.capabilityGroups = capabilityGroups
        channelRoutingResource.addOwnerReference(channelResource)
        return channelRoutingResource
    }
}

fun createChannelRoutingResource(channelResource: ChannelResource): ChannelRoutingResource {
    val metadata = channelResource.metadata
    val channelResourceName = metadata.name
    val labels = metadata.labels
    LOG.debug("Channel labels:{}", labels)
    val channel = labels[Labels.CHANNEL]
    val tenant = labels[Labels.TENANT]
    val version = labels[Labels.VERSION]
    val subset = labels[Labels.SUBSET]
    val channelRoutingResource = ChannelRoutingResource()
    val channelRoutingResourceMetadata = ObjectMeta()
    channelRoutingResourceMetadata.name = channelResourceName
    channelRoutingResourceMetadata.namespace = metadata.namespace
    channelRoutingResourceMetadata.labels =
        mapOf(
            Labels.CHANNEL to channel,
            Labels.TENANT to tenant,
            Labels.VERSION to version,
            Labels.SUBSET to subset,
        )
    channelRoutingResource.metadata = channelRoutingResourceMetadata
    channelRoutingResource.spec = ChannelRoutingSpec()
    return channelRoutingResource
}

/**
 * Groups the wires by their provider (AgentResource).
 *
 * @param wiredCapabilities the set of wires to be grouped
 * @return a map where the key is the AgentResource and the value is a set of wires belonging to that AgentResource
 */
fun groupWiresByProvider(wiredCapabilities: Set<Wire<AgentResource>>): Map<AgentResource, Set<Wire<AgentResource>>> {
    return wiredCapabilities.groupBy { it.provider }.mapValues { it.value.toSet() }
}

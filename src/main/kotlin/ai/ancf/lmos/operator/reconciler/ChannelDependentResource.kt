/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.reconciler

import ai.ancf.lmos.operator.reconciler.filter.AgentResourcesFilter
import ai.ancf.lmos.operator.reconciler.generator.RoutingChannelGenerator
import ai.ancf.lmos.operator.resolver.ResolverException
import ai.ancf.lmos.operator.resolver.impl.AgentResolveContext
import ai.ancf.lmos.operator.resolver.impl.AgentResolver
import ai.ancf.lmos.operator.resources.AgentResource
import ai.ancf.lmos.operator.resources.ChannelResource
import ai.ancf.lmos.operator.resources.ChannelRoutingResource
import ai.ancf.lmos.operator.resources.ChannelStatus
import ai.ancf.lmos.operator.resources.Labels
import ai.ancf.lmos.operator.resources.ResolveStatus
import io.javaoperatorsdk.operator.api.reconciler.Context
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.BooleanWithUndefined
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@KubernetesDependent(useSSA = BooleanWithUndefined.FALSE)
class ChannelDependentResource :
    CRUDKubernetesDependentResource<ChannelRoutingResource, ChannelResource>(ChannelRoutingResource::class.java) {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    override fun desired(
        channelResource: ChannelResource,
        context: Context<ChannelResource>,
    ): ChannelRoutingResource {
        // Logic to define the desired state of the ChannelRoutingResource
        val namespace = channelResource.metadata.namespace
        log.info("Resolve required capabilities for channel ${channelResource.metadata.name} in namespace $namespace")

        val client = context.client

        // Retrieve all agents from the same namespace of the channel resource
        val subset = channelResource.metadata.labels[Labels.SUBSET] ?: "stable"
        val agentResources =
            client.resources(AgentResource::class.java).inNamespace(namespace)
                .withLabel(Labels.SUBSET, subset).list().items

        // Filter agents which support the tenant and channel of the channel resource
        val filteredAgentResources =
            agentResources.stream()
                .filter(AgentResourcesFilter(channelResource)).toList()

        val resolveContext = AgentResolveContext(filteredAgentResources)
        val requiredCapabilities = channelResource.spec.requiredCapabilities

        val resolver = AgentResolver()
        try {
            // Resolve required capabilities and wire them to the agents
            val wiredCapabilities = resolver.resolve(requiredCapabilities, resolveContext)
            val channelRoutingResource =
                RoutingChannelGenerator.createChannelRoutingResource(channelResource, wiredCapabilities, subset)
            channelResource.status = ChannelStatus(ResolveStatus.RESOLVED)
            log.info("Created ChannelRouting for channel ${channelResource.metadata.name} in namespace $namespace")
            return channelRoutingResource
        } catch (e: ResolverException) {
            // ChannelRoutingResource must be created because this method is not allowed to return null or throw an exception :(
            val channelRoutingResource =
                RoutingChannelGenerator.createChannelRoutingResource(channelResource, emptySet(), subset)
            log.error("Resolve failed for channel ${channelResource.metadata.name} in namespace $namespace", e)
            channelResource.status = ChannelStatus(ResolveStatus.UNRESOLVED, e.getUnresolvedRequiredCapabilities())
            return channelRoutingResource
        }
    }
}

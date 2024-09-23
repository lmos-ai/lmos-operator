/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.reconciler;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;
import ai.ancf.lmos.operator.reconciler.filter.AgentResourcesFilter;
import ai.ancf.lmos.operator.resolver.ResolverException;
import ai.ancf.lmos.operator.resolver.Wire;
import ai.ancf.lmos.operator.resolver.impl.AgentResolveContext;
import ai.ancf.lmos.operator.resolver.impl.AgentResolver;
import ai.ancf.lmos.operator.resources.agent.AgentResource;
import ai.ancf.lmos.operator.resources.channel.ChannelResource;
import ai.ancf.lmos.operator.resources.channel.ChannelStatus;
import ai.ancf.lmos.operator.resources.channel.RequiredCapability;
import ai.ancf.lmos.operator.resources.channel.ResolveStatus;
import ai.ancf.lmos.operator.resources.routing.ChannelRoutingResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

import static io.javaoperatorsdk.operator.processing.dependent.kubernetes.BooleanWithUndefined.FALSE;
import static ai.ancf.lmos.operator.reconciler.generator.RoutingChannelGenerator.createChannelRoutingResource;

@KubernetesDependent(useSSA = FALSE)
public class ChannelRoutingDependentResource extends CRUDKubernetesDependentResource<ChannelRoutingResource, ChannelResource> {

    private static final Logger LOG = LoggerFactory.getLogger(ChannelRoutingDependentResource.class);

    public ChannelRoutingDependentResource() {
        super(ChannelRoutingResource.class);
    }

    @Override
    protected ChannelRoutingResource desired(ChannelResource channelResource, Context<ChannelResource> context) {
        // Logic to define the desired state of the ChannelRoutingResource

        String namespace = channelResource.getMetadata().getNamespace();

        LOG.info(String.format("Resolve required capabilities for channel %s in namespace %s", channelResource.getMetadata().getName(), namespace));

        final KubernetesClient client = context.getClient();

        // Retrieve all agents from the same namespace of the channel resource
        var agentResources = client.resources(AgentResource.class).inNamespace(namespace).list().getItems();

        // Filter agents which support the tenant and channel of the channel resource
        List<AgentResource> filteredAgentResources = agentResources.stream()
                .filter(new AgentResourcesFilter(channelResource)).toList();

        var resolveContext = new AgentResolveContext(filteredAgentResources);
        Set<RequiredCapability> requiredCapabilities = channelResource.getSpec().getRequiredCapabilities();

        var resolver = new AgentResolver();
        try {
            // Resolve required capabilities and wire them to the agents
            Set<Wire<AgentResource>> wiredCapabilities = resolver.resolve(requiredCapabilities, resolveContext);
            ChannelRoutingResource channelRoutingResource = createChannelRoutingResource(channelResource, wiredCapabilities);
            channelResource.setStatus(new ChannelStatus(ResolveStatus.RESOLVED));
            LOG.info(String.format("Created ChannelRouting for channel %s in namespace %s", channelResource.getMetadata().getName(), namespace));
            return channelRoutingResource;
        }catch (ResolverException e) {
            // ChannelRoutingResource must be created because this method is not allowed to return null or throw an exception :(
            var channelRoutingResource = createChannelRoutingResource(channelResource);
            LOG.error(String.format("Resolve failed for channel %s in namespace %s", channelResource.getMetadata().getName(), namespace), e);
            channelResource.setStatus(new ChannelStatus(ResolveStatus.UNRESOLVED, e.getUnresolvedRequiredCapabilities()));
            return channelRoutingResource;
        }
    }

}

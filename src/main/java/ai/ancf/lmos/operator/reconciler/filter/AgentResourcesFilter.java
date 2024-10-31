/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.reconciler.filter;

import ai.ancf.lmos.operator.resources.agent.AgentResource;
import ai.ancf.lmos.operator.resources.channel.ChannelResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Predicate;

public class AgentResourcesFilter implements Predicate<AgentResource>   {

    private static final Logger LOG = LoggerFactory.getLogger(AgentResourcesFilter.class);

    private final Map<String, String> labels;

    public AgentResourcesFilter(ChannelResource channelResource) {
        var metadata = channelResource.getMetadata();
        labels = metadata.getLabels();
    }

    @Override
    public boolean test(AgentResource agentResource) {
        boolean tenantMatches = agentResource.getSpec().getSupportedTenants() == null ||
                                agentResource.getSpec().getSupportedTenants().isEmpty() ||
                                agentResource.getSpec().getSupportedTenants().contains(labels.get("tenant"));

        boolean channelMatches = agentResource.getSpec().getSupportedChannels().contains(labels.get("channel"));

        return tenantMatches && channelMatches;
    }
}

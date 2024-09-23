/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.resources.routing;

import ai.ancf.lmos.operator.resolver.Wire;
import ai.ancf.lmos.operator.resources.agent.AgentResource;
import ai.ancf.lmos.operator.resources.agent.ProvidedCapability;
import io.fabric8.generator.annotation.Required;
import ai.ancf.lmos.operator.resources.channel.RequiredCapability;
import io.fabric8.kubernetes.api.model.ObjectMeta;

import java.util.Objects;

public class ChannelRoutingCapability {

    @Required
    private String name;
    @Required
    private String requiredVersion;
    @Required
    private String providedVersion;
    @Required
    private String description;
    @Required
    private String host;
    private String subset;

    public ChannelRoutingCapability() {
    }

    public ChannelRoutingCapability(Wire<AgentResource> wire) {
        ProvidedCapability providedCapability = wire.getProvidedCapability();
        RequiredCapability requiredCapability = wire.getRequiredCapability();
        this.name = providedCapability.getName();
        this.requiredVersion = requiredCapability.getVersion();
        this.providedVersion = providedCapability.getVersion();
        this.description = providedCapability.getDescription();
        ObjectMeta metadata = wire.getProvider().getMetadata();
        this.host = String.format("%s.%s.svc.cluster.local", metadata.getName(), metadata.getNamespace());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRequiredVersion() {
        return requiredVersion;
    }

    public void setRequiredVersion(String requiredVersion) {
        this.requiredVersion = requiredVersion;
    }

    public String getProvidedVersion() {
        return providedVersion;
    }

    public void setProvidedVersion(String providedVersion) {
        this.providedVersion = providedVersion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getSubset() {
        return subset;
    }

    public void setSubset(String subset) {
        this.subset = subset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChannelRoutingCapability that = (ChannelRoutingCapability) o;
        return Objects.equals(name, that.name) && Objects.equals(requiredVersion, that.requiredVersion) && Objects.equals(providedVersion, that.providedVersion) && Objects.equals(description, that.description) && Objects.equals(host, that.host) && Objects.equals(subset, that.subset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, requiredVersion, providedVersion, description, host, subset);
    }
}

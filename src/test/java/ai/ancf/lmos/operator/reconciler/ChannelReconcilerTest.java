/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.reconciler;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.utils.Serialization;
import io.javaoperatorsdk.operator.springboot.starter.test.EnableMockOperator;
import ai.ancf.lmos.operator.resources.agent.AgentResource;
import ai.ancf.lmos.operator.resources.channel.ChannelResource;
import ai.ancf.lmos.operator.resources.channel.ResolveStatus;
import ai.ancf.lmos.operator.resources.routing.ChannelRoutingResource;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@EnableMockOperator(crdPaths = {"classpath:META-INF/fabric8/channels.lmos.ai-v1.yml",
        "classpath:META-INF/fabric8/agents.lmos.ai-v1.yml",
        "classpath:META-INF/fabric8/channelroutings.lmos.ai-v1.yml",
        "classpath:META-INF/fabric8/channelrollouts.lmos.ai-v1.yml",
        })
class ChannelReconcilerTest {
    @Autowired
    KubernetesClient client;

    @AfterEach
    public void cleanUp() {
        client.resources(AgentResource.class).delete();
        client.resources(ChannelResource.class).delete();
        client.resources(ChannelRoutingResource.class).delete();
    }

    @Test
    void verifyThatCRDAreCreated() {

        assertThat(
                client
                        .apiextensions()
                        .v1()
                        .customResourceDefinitions()
                        .withName("agents.lmos.ai")
                        .get())
                .isNotNull();

        assertThat(
                client
                        .apiextensions()
                        .v1()
                        .customResourceDefinitions()
                        .withName("channels.lmos.ai")
                        .get())
                .isNotNull();
    }

    @Test
    void shouldCreateResolvedChannelRouting() throws FileNotFoundException {

        // When I create two Agents
        client.load(getResource("billing-agent-v1.yaml")).createOrReplace();
        client.load(getResource("contract-agent-v1.yaml")).createOrReplace();

        // When I create a Channel the Reconciler should start
        client.load(getResource("ivr-channel-v1.yaml")).createOrReplace();

        // Then the channel status should be updated to resolved
        var channelResources = await().atMost(5, TimeUnit.SECONDS) // Timeout duration
                .pollInterval(50, TimeUnit.MILLISECONDS) // Polling interval
                .until(() -> client.resources(ChannelResource.class).list().getItems(), c -> c.get(0).getStatus() != null);

        assertThat(channelResources).isNotNull().hasSize(1);
        assertThat(channelResources.get(0).getStatus().getResolveStatus()).isEqualTo(ResolveStatus.RESOLVED);
        assertThat(channelResources.get(0).getStatus().getUnresolvedRequiredCapabilities()).isNull();

        // And the channel routing resource should be created and status is resolved
        var channelRoutingResources = await().atMost(5, TimeUnit.SECONDS) // Timeout duration
                .pollInterval(50, TimeUnit.MILLISECONDS) // Polling interval
                .until(() -> client.resources(ChannelRoutingResource.class).list().getItems(), r -> !r.isEmpty());

        assertThat(channelRoutingResources).hasSize(1);
        ChannelRoutingResource channelRoutingResource = channelRoutingResources.get(0);
        //assertThat(channelRoutingResource.getStatus().getResolveStatus()).isEqualTo(ResolveStatus.RESOLVED);
        assertThat(channelRoutingResource.getMetadata().getName()).isEqualTo("ivr-stable");
        assertThat(channelRoutingResource.getMetadata().getOwnerReferences()).hasSize(1);
        System.out.println(Serialization.asYaml(channelRoutingResource));

        // If I delete the ChannelRoutingResource the desired state is reconciled again
        client.resources(ChannelRoutingResource.class).delete();
        channelRoutingResources = await().atMost(5, TimeUnit.SECONDS) // Timeout duration
                .pollInterval(50, TimeUnit.MILLISECONDS) // Polling interval
                .until(() -> client.resources(ChannelRoutingResource.class).list().getItems(), r -> !r.isEmpty());

        assertThat(channelRoutingResources).hasSize(1);
    }

    @Test
    void shouldCreateUnresolvedChannelRouting() throws FileNotFoundException, InterruptedException {

        // When I create an Agent and a Channel
        client.load(getResource("billing-agent-v1.yaml")).createOrReplace();
        client.load(getResource("web-channel-v1.yaml")).createOrReplace();

        // Then the channel status should be updated to unresolved
        var channelResources = await().atMost(5, TimeUnit.SECONDS) // Timeout duration
                .pollInterval(50, TimeUnit.MILLISECONDS) // Polling interval
                .until(() -> client.resources(ChannelResource.class).list().getItems(), c -> c.get(0).getStatus() != null);

        assertThat(channelResources).isNotNull().hasSize(1);
        assertThat(channelResources.get(0).getStatus().getResolveStatus()).isEqualTo(ResolveStatus.UNRESOLVED);
        assertThat(channelResources.get(0).getStatus().getUnresolvedRequiredCapabilities()).isNotEmpty();

        // And the channel routing resource should be created but status is unresolved
        var channelRoutingResources = await().atMost(5, TimeUnit.SECONDS) // Timeout duration
                .pollInterval(50, TimeUnit.MILLISECONDS) // Polling interval
                .until(() -> client.resources(ChannelRoutingResource.class).list().getItems(), r -> !r.isEmpty());

        assertThat(channelRoutingResources).isNotNull().hasSize(1);
        ChannelRoutingResource channelRoutingResource = channelRoutingResources.get(0);
        //assertThat(channelRoutingResource.getStatus().getResolveStatus()).isEqualTo(ResolveStatus.UNRESOLVED);
        assertThat(channelRoutingResource.getMetadata().getName()).isEqualTo("web-stable");
        assertThat(channelRoutingResource.getMetadata().getOwnerReferences()).hasSize(1);
    }

    @NotNull
    private static FileInputStream getResource(String resourceName) throws FileNotFoundException {
        return new FileInputStream(ResourceUtils.getFile("classpath:" + resourceName));
    }

}

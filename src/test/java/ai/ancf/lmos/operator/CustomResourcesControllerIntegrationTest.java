/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.utils.Serialization;
import io.javaoperatorsdk.operator.springboot.starter.test.EnableMockOperator;
import ai.ancf.lmos.operator.resources.channel.ChannelResource;
import ai.ancf.lmos.operator.resources.routing.ChannelRoutingResource;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import static ai.ancf.lmos.operator.server.routing.CustomResourcesController.X_SUBSET_HEADER;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes={OperatorApplication.class})
@AutoConfigureWebTestClient
@EnableMockOperator(crdPaths = {"classpath:META-INF/fabric8/channels.lmos.ai-v1.yml",
        "classpath:META-INF/fabric8/agents.lmos.ai-v1.yml",
        "classpath:META-INF/fabric8/channelroutings.lmos.ai-v1.yml",
        "classpath:META-INF/fabric8/channelrollouts.lmos.ai-v1.yml",
})
public class CustomResourcesControllerIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    KubernetesClient client;

    @Test
    void shouldReturnChannels() throws FileNotFoundException {
        // Given I create two Channel resources
        client.load(getResource("acme-web-channel-v1.yaml")).create();
        client.load(getResource("acme-ivr-channel-v1.yaml")).create();

        // When I call the API
        EntityExchangeResult<List<ChannelResource>> result = this.webTestClient
                .get()
                .uri("/apis/v1/tenants/acme/channels")
                .header(X_SUBSET_HEADER, "stable")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ChannelResource.class).returnResult();

        List<ChannelResource> responseBody = result.getResponseBody();

        // Then the two created channel resources should be returned
        assertThat(responseBody).hasSize(2);
    }

    @Test
    void shouldReturnChannelResource() throws FileNotFoundException {
        // Given I create an ChannelResource
        client.load(getResource("acme-web-channel-v1.yaml")).createOrReplace();

        // When I call the API
        EntityExchangeResult<ChannelResource> result = this.webTestClient
                .get()
                .uri("/apis/v1/tenants/acme/channels/web")
                .header(X_SUBSET_HEADER, "stable")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ChannelResource.class).returnResult();

        ChannelResource responseBody = result.getResponseBody();

        // Then the created channel resource should be returned
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getMetadata().getName()).isEqualTo("acme-web-stable");
    }

    @Test
    void shouldReturnChannelRoutingResource() throws FileNotFoundException {
        // Given I create an ChannelRouting
        client.load(getResource("channel-routing.yaml")).createOrReplace();

        // When I call the API
        EntityExchangeResult<ChannelRoutingResource> result = this.webTestClient
                .get()
                .uri("/apis/v1/tenants/acme/channels/web/routing")
                .header(X_SUBSET_HEADER, "stable")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ChannelRoutingResource.class).returnResult();

        ChannelRoutingResource responseBody = result.getResponseBody();

        // Then the created channel routing resource should be returned
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getMetadata().getName()).isEqualTo("acme-web-stable");
    }

    @Test
    void shouldReturnNotContent() {
        // Given there is no channel routing

        // When I call the API the response should be 404 no content
         this.webTestClient
                .get()
                .uri("/apis/v1/tenants/de/channels/unknown/routing")
                .header(X_SUBSET_HEADER, "stable")
                .exchange()
                .expectStatus().isNotFound();
    }

    @NotNull
    private static FileInputStream getResource(String resourceName) throws FileNotFoundException {
        return new FileInputStream(ResourceUtils.getFile("classpath:" + resourceName));
    }
}

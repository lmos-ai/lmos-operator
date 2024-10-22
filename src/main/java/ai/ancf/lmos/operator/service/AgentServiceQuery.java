/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.service;

import org.eclipse.ditto.json.JsonObject;
import org.eclipse.ditto.wot.model.ThingDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@org.springframework.stereotype.Service
public class AgentServiceQuery {

    private static final Logger LOG = LoggerFactory.getLogger(AgentServiceQuery.class);

    private final WebClient webClient;

    public AgentServiceQuery() {
        this.webClient = WebClient.builder().build();
    }

    public ThingDescription queryAgentService(String serviceUrl) {
        LOG.info("Querying Agent for TD: {}", serviceUrl);
        String thingDescriptionJson = webClient.get()
                .uri(serviceUrl)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class).block();

        if (thingDescriptionJson == null || thingDescriptionJson.isEmpty()) {
            throw new IllegalStateException("TD Response body from agent is empty");
        }

        ThingDescription thingDescription = ThingDescription.fromJson(JsonObject.of(thingDescriptionJson));
        validateTD(thingDescription);
        return thingDescription;
    }

    private void validateTD(ThingDescription thingDescription) {
        if (thingDescription == null) {
            throw new RuntimeException("ThingDescription is null");
        }

        thingDescription.getDescription()
                .filter(desc -> !desc.isEmpty())
                .orElseThrow(() -> new RuntimeException("Description of TD is not present or is empty"));
    }
}
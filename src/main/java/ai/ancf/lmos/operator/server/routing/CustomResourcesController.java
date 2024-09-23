/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.server.routing;

import ai.ancf.lmos.operator.resources.channel.ChannelResource;
import ai.ancf.lmos.operator.resources.routing.ChannelRoutingResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apis/v1/tenants/{tenant}")
public class CustomResourcesController {

    public static final String X_SUBSET_HEADER = "x-subset";
    private final CustomResourcesService customResourcesService;

    public CustomResourcesController(CustomResourcesService customResourcesService) {
        this.customResourcesService = customResourcesService;
    }

    @GetMapping("/channels")
    public List<ChannelResource> getRouting(@RequestHeader(name = X_SUBSET_HEADER) String subsetHeader,
                                            @PathVariable String tenant) {
        return customResourcesService.getChannels(tenant, subsetHeader);
    }

    @GetMapping("/channels/{channel}")
    public ResponseEntity<ChannelResource> getChannel(@RequestHeader(name = X_SUBSET_HEADER) String subsetHeader,
                                                             @PathVariable String tenant,
                                                             @PathVariable String channel) {

        return ResponseEntity.of(customResourcesService.getChannel(tenant, channel, subsetHeader));
    }


    @GetMapping("/channels/{channel}/routing")
    public ResponseEntity<ChannelRoutingResource> getRouting(@RequestHeader(name = X_SUBSET_HEADER) String subsetHeader,
                                                             @PathVariable String tenant,
                                                             @PathVariable String channel) {

        return ResponseEntity.of(customResourcesService.getRouting(tenant, channel, subsetHeader));
    }

}
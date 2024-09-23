/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.resources.channel;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.*;

@Group("lmos.ai")
@Version("v1")
@Plural("channels")
@Singular("channel")
@Kind("Channel")
@ShortNames("ch")
public class ChannelResource extends CustomResource<ChannelSpec, ChannelStatus> implements Namespaced {

}

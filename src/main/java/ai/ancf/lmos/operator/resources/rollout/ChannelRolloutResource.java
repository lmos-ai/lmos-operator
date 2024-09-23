/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.resources.rollout;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.*;

@Group("lmos.ai")
@Version("v1")
@Plural("channelrollouts")
@Singular("channelrollout")
@Kind("ChannelRollout")
@ShortNames("crl")
public class ChannelRolloutResource extends CustomResource<ChannelRolloutSpec, Void> implements Namespaced {

}

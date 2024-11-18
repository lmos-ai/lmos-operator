/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.reconciler

import ai.ancf.lmos.operator.resources.ChannelResource
import io.javaoperatorsdk.operator.api.reconciler.Context
import io.javaoperatorsdk.operator.api.reconciler.ControllerConfiguration
import io.javaoperatorsdk.operator.api.reconciler.Reconciler
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl
import io.javaoperatorsdk.operator.api.reconciler.dependent.Dependent
import org.springframework.stereotype.Component

@Component
@ControllerConfiguration(dependents = [Dependent(type = ChannelRoutingDependentResource::class)]) // @ControllerConfiguration()
class ChannelReconciler : Reconciler<ChannelResource> {
    override fun reconcile(
        channelResource: ChannelResource?,
        context: Context<ChannelResource?>?,
    ): UpdateControl<ChannelResource?> {
        /*
                   The dependent ChannelRoutingDependentResource is automatically reconciled before the ChannelReconciler is executed.
                   Optional<ChannelRoutingResource> secondaryResource = context.getSecondaryResource(ChannelRoutingResource.class);
                   Unfortunately I don't know yet how the status of the subresource can be updated.
         */

        return UpdateControl.patchStatus(channelResource)
    }
}

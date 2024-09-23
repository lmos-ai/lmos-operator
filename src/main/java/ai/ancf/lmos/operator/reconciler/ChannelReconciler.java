/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.reconciler;

import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ControllerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import io.javaoperatorsdk.operator.api.reconciler.dependent.Dependent;
import ai.ancf.lmos.operator.resources.channel.ChannelResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
@ControllerConfiguration(dependents = { @Dependent(type = ChannelRoutingDependentResource.class)})
//@ControllerConfiguration()
public class ChannelReconciler implements Reconciler<ChannelResource> {

    private static final Logger LOG = LoggerFactory.getLogger(ChannelReconciler.class);

    @Override
    public UpdateControl<ChannelResource> reconcile(ChannelResource channelResource, Context context) {

        /*
            The dependent ChannelRoutingDependentResource is automatically reconciled before the ChannelReconciler is executed.
            Optional<ChannelRoutingResource> secondaryResource = context.getSecondaryResource(ChannelRoutingResource.class);
            Unfortunately I don't know yet how the status of the subresource can be updated.
        */

        return UpdateControl.patchStatus(channelResource);
    }
}
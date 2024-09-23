/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.reconciler;

import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import ai.ancf.lmos.operator.resources.rollout.ChannelRolloutResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ChannelRolloutReconciler implements Reconciler<ChannelRolloutResource> {

    private static final Logger LOG = LoggerFactory.getLogger(ChannelRolloutReconciler.class);


    @Override
    public UpdateControl<ChannelRolloutResource> reconcile(ChannelRolloutResource channelRolloutResource, Context context) {
        // TODO: fill in logic

        LOG.debug("ChannelRollout reconcile");

        return UpdateControl.noUpdate();
    }
}
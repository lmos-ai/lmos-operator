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
import ai.ancf.lmos.operator.resources.agent.AgentResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
@ControllerConfiguration
public class AgentReconciler implements Reconciler<AgentResource> {

    private static final Logger LOG = LoggerFactory.getLogger(AgentReconciler.class);


    @Override
    public UpdateControl<AgentResource> reconcile(AgentResource agentResource, Context context) {
        // TODO: fill in logic
        LOG.debug("Agent reconcile");

        return UpdateControl.noUpdate();
    }
}
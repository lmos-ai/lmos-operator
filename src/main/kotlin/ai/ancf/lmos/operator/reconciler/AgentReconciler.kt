/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.reconciler

import ai.ancf.lmos.operator.resources.AgentResource
import io.javaoperatorsdk.operator.api.reconciler.Context
import io.javaoperatorsdk.operator.api.reconciler.ControllerConfiguration
import io.javaoperatorsdk.operator.api.reconciler.Reconciler
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
@ControllerConfiguration
class AgentReconciler : Reconciler<AgentResource> {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    override fun reconcile(
        agentResource: AgentResource?,
        context: Context<AgentResource?>?,
    ): UpdateControl<AgentResource?> {
        // TODO: fill in logic
        log.debug("Agent reconcile")

        return UpdateControl.noUpdate()
    }
}

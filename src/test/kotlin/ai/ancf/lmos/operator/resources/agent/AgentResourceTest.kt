/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.resources.agent

import ai.ancf.lmos.operator.data.TestDataGenerator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AgentResourceTest {
    @Test
    fun testEqualsAndHasCode() {
        val agentResource1 = TestDataGenerator.createAgentResource("1.1.0")
        val agentResource2 = TestDataGenerator.createAgentResource("1.1.0")

        assertThat(agentResource1).isEqualTo(agentResource2)
        assertThat(agentResource1).hasSameHashCodeAs(agentResource2)
    }
}

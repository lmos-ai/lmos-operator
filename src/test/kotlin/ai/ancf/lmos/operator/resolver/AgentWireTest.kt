/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.resolver

import ai.ancf.lmos.operator.data.TestDataGenerator
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class AgentWireTest {
    @Test
    fun testEqualsAndHasCode() {
        val wire1 = TestDataGenerator.createAgentWire()
        val wire2 = TestDataGenerator.createAgentWire()

        Assertions.assertThat(wire1 == wire2).isTrue()
        Assertions.assertThat(wire1).hasSameHashCodeAs(wire2)
    }
}

/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.resources.agent

import ai.ancf.lmos.operator.data.TestDataGenerator
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class ProvidedCapabilityTest {
    @Test
    fun testEqualsAndHasCode() {
        val providedCapability1 = TestDataGenerator.createProvidedCapability()
        val providedCapability2 = TestDataGenerator.createProvidedCapability()

        Assertions.assertThat(providedCapability1).isEqualTo(providedCapability2)
        Assertions.assertThat(providedCapability1).hasSameHashCodeAs(providedCapability2)
    }
}

/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.resources.channel

import ai.ancf.lmos.operator.data.TestDataGenerator
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class RequiredCapabilityTest {
    @Test
    fun testEqualsAndHasCode() {
        val requiredCapability1 = TestDataGenerator.createRequiredCapability()
        val requiredCapability2 = TestDataGenerator.createRequiredCapability()

        Assertions.assertThat(requiredCapability1 == requiredCapability2).isTrue()
        Assertions.assertThat(requiredCapability1).hasSameHashCodeAs(requiredCapability2)
    }
}

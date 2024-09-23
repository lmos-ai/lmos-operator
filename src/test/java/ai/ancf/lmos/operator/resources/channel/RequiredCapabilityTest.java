/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.resources.channel;

import org.junit.jupiter.api.Test;

import static ai.ancf.lmos.operator.data.TestDataGenerator.createRequiredCapability;
import static org.assertj.core.api.Assertions.assertThat;

public class RequiredCapabilityTest {

    @Test
    public void testEqualsAndHasCode(){

        var requiredCapability1 = createRequiredCapability();
        var requiredCapability2 = createRequiredCapability();

        assertThat(requiredCapability1.equals(requiredCapability2)).isTrue();
        assertThat(requiredCapability1).hasSameHashCodeAs(requiredCapability2);
    }


}

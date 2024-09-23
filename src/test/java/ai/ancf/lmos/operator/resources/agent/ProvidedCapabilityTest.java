/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.resources.agent;

import org.junit.jupiter.api.Test;

import static ai.ancf.lmos.operator.data.TestDataGenerator.createProvidedCapability;
import static org.assertj.core.api.Assertions.assertThat;

public class ProvidedCapabilityTest {

    @Test
    public void testEqualsAndHasCode(){

        var providedCapability1 = createProvidedCapability();
        var providedCapability2 = createProvidedCapability();

        assertThat(providedCapability1.equals(providedCapability2)).isTrue();
        assertThat(providedCapability1).hasSameHashCodeAs(providedCapability2);
    }

}

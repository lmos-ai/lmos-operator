/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.resolver;

import ai.ancf.lmos.operator.data.TestDataGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AgentWireTest {

    @Test
    public void testEqualsAndHasCode(){

        var wire1 = TestDataGenerator.createAgentWire();
        var wire2 = TestDataGenerator.createAgentWire();

        assertThat(wire1.equals(wire2)).isTrue();
        assertThat(wire1).hasSameHashCodeAs(wire2);
    }

}

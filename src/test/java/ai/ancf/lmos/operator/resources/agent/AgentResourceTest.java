/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.resources.agent;

import org.junit.jupiter.api.Test;

import static ai.ancf.lmos.operator.data.TestDataGenerator.createAgentResource;
import static org.assertj.core.api.Assertions.assertThat;

public class AgentResourceTest {
    @Test
    public void testEqualsAndHasCode(){
        AgentResource agentResource1 = createAgentResource("1.1.0");
        AgentResource agentResource2 = createAgentResource("1.1.0");

        assertThat(agentResource1.equals(agentResource2)).isTrue();
        assertThat(agentResource1).hasSameHashCodeAs(agentResource2);
    }
}

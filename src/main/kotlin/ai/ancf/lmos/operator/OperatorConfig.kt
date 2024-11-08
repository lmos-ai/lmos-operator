/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class OperatorConfig {
    @Bean
    open fun objectMapper(): ObjectMapper =
        jsonMapper {
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            addModule(kotlinModule { enable(KotlinFeature.NullIsSameAsDefault) })
        }
}

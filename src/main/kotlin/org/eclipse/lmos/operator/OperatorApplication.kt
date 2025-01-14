/*
 * SPDX-FileCopyrightText: 2025 Deutsche Telekom AG and others
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.eclipse.lmos.operator

import org.springframework.context.annotation.Import
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@Import(OperatorConfig::class)
class OperatorApplication

fun main(args: Array<String>) {
    runApplication<OperatorApplication>(*args)
}

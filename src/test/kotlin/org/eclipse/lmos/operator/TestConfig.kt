/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.eclipse.lmos.operator

import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinition
import io.fabric8.kubernetes.client.KubernetesClient
import io.fabric8.kubernetes.client.server.mock.KubernetesClientBuilderCustomizer
import io.fabric8.kubernetes.client.server.mock.KubernetesCrudDispatcher
import io.fabric8.kubernetes.client.server.mock.KubernetesMockServer
import io.fabric8.kubernetes.client.utils.KubernetesSerialization
import io.fabric8.kubernetes.client.utils.Serialization
import io.fabric8.mockwebserver.Context
import io.javaoperatorsdk.operator.springboot.starter.OperatorAutoConfiguration
import io.javaoperatorsdk.operator.springboot.starter.test.TestConfigurationProperties
import okhttp3.mockwebserver.MockWebServer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.util.ResourceUtils
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.util.stream.Stream

@TestConfiguration
@ImportAutoConfiguration(OperatorAutoConfiguration::class)
@EnableConfigurationProperties(
    TestConfigurationProperties::class
)
class TestConfig {

    @Bean
    fun k8sMockServer(): KubernetesMockServer {
        val mockServer = MockWebServer()
        val server = KubernetesMockServer(
            Context(Serialization.jsonMapper().registerKotlinModule()), mockServer, HashMap(),
            KubernetesCrudDispatcher(emptyList()), true
        )
        server.init()

        return server
    }

    @Bean
    fun kubernetesClient(
        server: KubernetesMockServer,
        properties: TestConfigurationProperties,
    ): KubernetesClient {
        val client = server.createClient{ builder ->
            builder.withKubernetesSerialization(KubernetesSerialization(Serialization.jsonMapper().registerKotlinModule(), true))
        }

        Stream.concat(properties.crdPaths.stream(), properties.globalCrdPaths.stream())
            .forEach { crdPath: String? ->
                val crd: CustomResourceDefinition
                try {
                    crd = Serialization.unmarshal(FileInputStream(ResourceUtils.getFile(crdPath)))
                } catch (e: FileNotFoundException) {
                    log.warn("CRD with path {} not found!", crdPath)
                    e.printStackTrace()
                    return@forEach
                }
                client.apiextensions().v1().customResourceDefinitions().create(crd)
            }

        return client
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(TestConfiguration::class.java)
    }
}

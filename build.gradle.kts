/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
	java
	id("org.springframework.boot") version "3.3.3"
	id("io.spring.dependency-management") version "1.1.6"
	id("com.citi.helm") version "2.2.0"
	id("com.citi.helm-publish") version "2.2.0"
	id("org.cadixdev.licenser") version "0.6.1"
}

group = "ai.ancf.lmos"
version = "0.0.4-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}
helm {
	charts {
		create("main") {
			chartName.set("${project.name}-chart")
			chartVersion.set("${project.version}")
			sourceDir.set(file("src/main/helm"))
		}
	}
}

license {
	include("**/*.java") // Apply license header ONLY to Java files
	include("**/*.yaml") // Apply license header ONLY to Java files
	// OR
	exclude("**/*.properties") // Apply license header NOT to pro
}

tasks.register("helmPush") {
	description = "Push Helm chart to OCI registry"
	group = "helm"
	dependsOn(tasks.named("helmPackageMainChart"))

	doLast {
		val registryUrl = getProperty("REGISTRY_URL")
		val registryUsername = getProperty("REGISTRY_USERNAME")
		val registryPassword = getProperty("REGISTRY_PASSWORD")
		val registryNamespace = getProperty("REGISTRY_NAMESPACE")

		helm.execHelm("registry", "login") {
			option("-u", registryUsername)
			option("-p", registryPassword)
			args(registryUrl)
		}

		helm.execHelm("push") {
			args(tasks.named("helmPackageMainChart").get().outputs.files.singleFile.toString())
			args("oci://${registryUrl}/${registryNamespace}")
		}

		helm.execHelm("registry", "logout") {
			args(registryUrl)
		}
	}
}

fun getProperty(propertyName: String) = System.getenv(propertyName) ?: project.findProperty(propertyName) as String

tasks.named<BootBuildImage>("bootBuildImage") {
	val registryUrl = getProperty("REGISTRY_URL")
	val registryUsername = getProperty("REGISTRY_USERNAME")
	val registryPassword = getProperty("REGISTRY_PASSWORD")
	val registryNamespace = getProperty("REGISTRY_NAMESPACE")

	imageName.set("${registryUrl}/${registryNamespace}/${project.name}:${project.version}")
	publish = true
	docker {
		publishRegistry {
			url.set(registryUrl)
			username.set(registryUsername)
			password.set(registryPassword)
		}
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-actuator")

	implementation("io.javaoperatorsdk:operator-framework-spring-boot-starter:5.5.0")
	implementation("io.fabric8:generator-annotations:6.13.3")

	implementation("org.apache.felix:org.apache.felix.resolver:2.0.4")

	implementation("org.semver4j:semver4j:5.3.0")

	testImplementation("io.javaoperatorsdk:operator-framework-spring-boot-starter-test:5.5.0") {
		exclude(group = "org.apache.logging.log4j", module = "log4j-slf4j2-impl")
	}

	annotationProcessor("io.fabric8:crd-generator-apt:6.13.3")

	testImplementation("org.springframework.boot:spring-boot-starter-test")

	testImplementation("org.awaitility:awaitility:4.2.2")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

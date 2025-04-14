plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.4.4"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.joris"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-retry:1.0.0")

	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.0")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.4.0")

	implementation("com.squareup.moshi:moshi:1.15.0")
	implementation("com.squareup.moshi:moshi-kotlin:1.15.0")

	implementation(platform("org.mongodb:mongodb-driver-bom:5.4.0"))
	implementation("org.mongodb:bson-kotlinx:5.4.0")
	implementation("org.mongodb:mongodb-driver-kotlin-coroutine")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}


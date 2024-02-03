import com.google.cloud.tools.gradle.appengine.appyaml.AppEngineAppYamlExtension
val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

val exposed_version: String by project
val mysql_version: String by project

val postgres_version: String by project

plugins {
    kotlin("jvm") version "1.9.21"
    id("io.ktor.plugin") version "2.3.5"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("com.google.cloud.tools.appengine") version "2.4.2"
}

group = "com.gulftechinnovations"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

configure<AppEngineAppYamlExtension> {
    stage {
        setArtifact("build/libs/${project.name}-all.jar")
    }
    deploy {
        version = "GCLOUD_CONFIG"
        projectId = "GCLOUD_CONFIG"
    }
}

repositories {
    mavenCentral()
    maven {
        name = "clojars.org"
        url = uri("https://repo.clojars.org")
    }
}

dependencies {
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-cors-jvm")
    implementation("io.ktor:ktor-server-host-common-jvm")
    implementation("io.ktor:ktor-server-auth-jvm")
    implementation("io.ktor:ktor-server-auth-jwt-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    // exposed
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")

    //mysql
    implementation("mysql:mysql-connector-java:$mysql_version")

    // postgres
    implementation("org.postgresql:postgresql:$postgres_version")

    // for gcloud mysql
    implementation("com.google.cloud.sql:mysql-socket-factory-connector-j-8:1.13.1")

    // Cloud storage
    implementation("com.google.cloud:google-cloud-storage:2.26.1")

    // ktor Client version
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-okhttp:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")


    // Translation
    implementation ("net.clojars.suuft:libretranslate-java:1.0.5")

    // Detect language
    implementation("com.detectlanguage:detectlanguage:1.1.0")

    // DeepL Translation
    implementation("com.deepl.api:deepl-java:1.4.0")

    // Log4J
    implementation("org.apache.logging.log4j:log4j-api-kotlin:1.4.0")

    // Google cloud translation
    implementation("com.google.cloud:google-cloud-translate:2.33.0")
}

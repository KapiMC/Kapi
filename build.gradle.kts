import java.util.*

/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3 license. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

plugins {
    id("java-library")
    id("java")
    id("io.github.goooler.shadow") version "8.1.8"
    id("maven-publish")
    signing
}

group = "io.github.kapimc"
version = "0.1.0"
description = "A powerful, easy-to-use, and flexible framework for making Spigot Plugins."

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/groups/public/")
}

dependencies {
    api("dev.triumphteam:triumph-gui:3.1.10")
    api("org.jspecify:jspecify:1.0.0")
    api("org.jetbrains:annotations:24.1.0")
    compileOnly("org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
}

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

java {
    withSourcesJar()
    withJavadocJar()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}

tasks.shadowJar {
    relocate("dev.triumphteam.gui", "io.github.kapimc.kapi.gui")
    archiveClassifier.set("") // Remove the "-all" suffix jars
}

tasks.test {
    useJUnitPlatform() // This tells Gradle to use JUnit 5.
}

publishing.publications.create<MavenPublication>("maven") {
    from(components["java"])
    pom {
        name.set("Kapi")
        description.set("A powerful, easy-to-use, and flexible framework for making Spigot Plugins.")
        url.set("https://github.com/kapimc/kapi")
        
        licenses {
            license {
                name.set("AGPLv3 License")
                url.set("https://www.gnu.org/licenses/agpl-3.0.html")
            }
        }
        
        developers {
            developer {
                id.set("kyren")
                name.set("Kyren223")
                organization.set("KapiMC")
                organizationUrl.set("https://github.com/kapimc")
            }
        }
        
        scm {
            connection.set("scm:git:git://github.com/kapimc/kapi.git")
            developerConnection.set("scm:git:ssh://github.com:kapimc/kapi.git")
            url.set("https://github.com/kapimc/kapi")
        }
        
        issueManagement {
            system.set("GitHub")
            url.set("https://github.com/kapimc/kapi/issues")
        }
    }
}

publishing.repositories {
    mavenLocal()
}

tasks {
    signing {
        useInMemoryPgpKeys(
            System.getenv("GPG_KEY"),
            Base64.getEncoder().encodeToString(file("kapi_publishing_keyring.gpg").readBytes()),
            System.getenv("GPG_PASSPHRASE")
        )
        sign(publishing.publications["maven"])
    }
}

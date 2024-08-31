/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3 license. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

plugins {
    id("maven-publish")
    id("java-library")
    id("java")
    id("io.github.goooler.shadow") version "8.1.8"
}

group = "me.kyren223"
version = "0.1.0"
description = "Kapi"

repositories {
    mavenCentral()
    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
    maven {
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
}

dependencies {
    api("org.jetbrains:annotations:24.1.0")
    api("dev.triumphteam:triumph-gui:3.1.10")
    api("org.jspecify:jspecify:1.0.0")
    compileOnly("org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT")
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
    relocate("dev.triumphteam.gui", "me.kyren223.kapi.gui")
}

publishing.publications.create<MavenPublication>("maven") {
    from(components["java"])
}


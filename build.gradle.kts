import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/groups/public/")
}

dependencies {
    implementation("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
}

group = "me.yoursole"
version = "1.0-SNAPSHOT"
description = "CapturetheFlag"

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    named<Jar>("jar") {
        enabled = false
        dependsOn(shadowJar)
    }

    withType<ShadowJar> {
        archiveFileName.set(jar.get().archiveFileName)
    }
}

java.sourceCompatibility = JavaVersion.VERSION_16
java.targetCompatibility = JavaVersion.VERSION_16
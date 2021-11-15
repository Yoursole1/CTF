import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.5.31"
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.codemc.io/repository/nms/")
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/groups/public/")
}


val shadowMe by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}
dependencies {
    implementation("io.papermc.paper:paper:1.17.1-R0.1-SNAPSHOT")

    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    shadowMe("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    shadowMe("org.jetbrains.kotlin:kotlin-reflect")
    shadowMe("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
}

group = "me.yoursole"
version = "1.0-SNAPSHOT"
description = "CapturetheFlag"

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "16"
            freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
        }
    }

    named<Jar>("jar") {
        enabled = false
        dependsOn(shadowJar)
    }

    withType<ShadowJar> {
        configurations = listOf(shadowMe)
        archiveFileName.set(jar.get().archiveFileName)
    }

    register<Copy>("copyPlugin") {
        from("$buildDir/libs/")
        include("*.jar")
        into("$projectDir/server/plugins/")
        dependsOn(shadowJar.get())
    }

    build.get().dependsOn(named("copyPlugin"))
}

java.sourceCompatibility = JavaVersion.VERSION_16
java.targetCompatibility = JavaVersion.VERSION_16
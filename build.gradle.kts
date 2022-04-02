import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.6.20"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.papermc.paperweight.userdev") version "1.3.5"
    id("xyz.jpenilla.run-paper") version "1.0.6"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
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
    paperDevBundle("1.18.2-R0.1-SNAPSHOT")

    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    shadowMe("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    shadowMe("org.jetbrains.kotlin:kotlin-reflect")
    shadowMe("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
}

group = "me.yoursole"
version = "1.0-SNAPSHOT"
description = "CapturetheFlag"

bukkit {
    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
    main = "me.yoursole.ctf.CTF"
    apiVersion = "1.18"
    authors = listOf("Yoursole", "My-Name-Is-Jeff")
}

tasks {

    processResources {
        filteringCharset = "UTF-8"
    }

    javadoc {
        options.encoding = "UTF-8"
    }

    assemble {
        dependsOn(reobfJar)
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "17"
            freeCompilerArgs =
                listOf("-opt-in=kotlin.RequiresOptIn", "-Xjvm-default=all", "-Xrelease=8", "-Xbackend-threads=0")
        }
        kotlinDaemonJvmArguments.set(
            listOf(
                "-Xmx2G",
            )
        )
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

kotlin {
    jvmToolchain {
        check(this is JavaToolchainSpec)
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
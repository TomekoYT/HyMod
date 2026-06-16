import org.apache.commons.lang3.SystemUtils
import dev.architectury.pack200.java.Pack200Adapter

val mod_name: String by project
val mod_id: String by project
val mod_version: String by project
val mod_description: String by project
val mod_archives_name: String by project
val base_group: String by project

val java_version: String by project
val minecraft_version: String by project

val hypixel_mod_api_version: String by project

plugins {
    idea
    java
    kotlin("jvm") version "2.4.0"
    id("gg.essential.loom") version "1.9.31"
    id("dev.architectury.architectury-pack200") version "0.1.3"
    id("com.gradleup.shadow") version "9.4.1"
    id("dev.deftu.gradle.bloom") version "0.2.0"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(java_version))
}

loom {
    runs {
        getByName("client") {
            property("mixin.debug", "true")
            programArgs("--tweakClass", "org.spongepowered.asm.launch.MixinTweaker")
            programArgs("--tweakClass", "cc.polyfrost.oneconfig.loader.stage0.LaunchWrapperTweaker")
        }
    }
    runConfigs {
        getByName("client") {
            if (SystemUtils.IS_OS_MAC_OSX) {
                vmArgs.remove("-XstartOnFirstThread")
            }
        }
        remove(getByName("server"))
    }
    forge {
        pack200Provider.set(Pack200Adapter())
        mixinConfig("mixins.$mod_id.json")
    }

    mixin {
        defaultRefmapName.set("mixins.$mod_id.refmap.json")
    }
}

tasks.compileJava {
    dependsOn(tasks.processResources)
}

sourceSets.main {
    output.setResourcesDir(sourceSets.main.flatMap { it.java.classesDirectory })
    java.srcDir(layout.projectDirectory.dir("src/main/kotlin"))
    kotlin.destinationDirectory.set(java.destinationDirectory)
}

repositories {
    mavenCentral()
    maven("https://repo.spongepowered.org/maven/")
    maven("https://repo.polyfrost.cc/releases")
    maven("https://repo.hypixel.net/repository/Hypixel/")
}

val shadowImpl: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraft_version")
    mappings("de.oceanlabs.mcp:mcp_stable:22-$minecraft_version")
    forge("net.minecraftforge:forge:$minecraft_version-11.15.1.2318-$minecraft_version")

    shadowImpl(kotlin("stdlib-jdk8"))

    annotationProcessor("org.ow2.asm:asm-debug-all:5.2")
    annotationProcessor("com.google.guava:guava:32.1.2-jre")
    annotationProcessor("com.google.code.gson:gson:2.8.9")

    annotationProcessor("org.spongepowered:mixin:0.8.5-SNAPSHOT")
    shadowImpl("org.spongepowered:mixin:0.7.11-SNAPSHOT") {
        isTransitive = false
    }

    compileOnly("cc.polyfrost:oneconfig-$minecraft_version-forge:0.2.2-alpha+")
    shadowImpl("cc.polyfrost:oneconfig-wrapper-launchwrapper:1.0.0-beta+")

    modImplementation("net.hypixel:mod-api-forge:$hypixel_mod_api_version")
}

bloom {
    replacement("@MOD_NAME@", mod_name)
    replacement("@MOD_ID@", mod_id)
    replacement("@MOD_VERSION@", mod_version)
}

tasks.withType(JavaCompile::class) {
    options.encoding = "UTF-$java_version"
}

tasks.withType(org.gradle.jvm.tasks.Jar::class) {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveBaseName.set("$mod_archives_name-$mod_version-${minecraft_version}_forge")
    manifest.attributes.run {
        this["FMLCorePluginContainsFMLMod"] = "true"
        this["ForceLoadAsMod"] = "true"

        this["TweakClass"] = "org.spongepowered.asm.launch.MixinTweaker"
        this["MixinConfigs"] = "mixins.$mod_id.json"
    }
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    val props = mapOf(
        "mod_id" to mod_id,
        "mod_name" to mod_name,
        "mod_version" to mod_version,
        "mod_description" to mod_description,
        "base_group" to base_group,

        "java_version" to java_version,
        "minecraft_version" to minecraft_version,
    )

    inputs.properties(props)

    filesMatching(listOf("mcmod.info", "mixins.$mod_id.json")) {
        expand(props)
    }
}

val remapJar by tasks.named<net.fabricmc.loom.task.RemapJarTask>("remapJar") {
    archiveClassifier.set("")
    from(tasks.shadowJar)
    input.set(tasks.shadowJar.get().archiveFile)
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveClassifier.set("without-deps")
    destinationDirectory.set(layout.buildDirectory.dir("intermediates"))
    manifest.attributes += mapOf(
        "ModSide" to "CLIENT",
        "TweakOrder" to 0,
        "ForceLoadAsMod" to true,
        "TweakClass" to "cc.polyfrost.oneconfig.loader.stage0.LaunchWrapperTweaker"
    )
}

tasks.shadowJar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    destinationDirectory.set(layout.buildDirectory.dir("intermediates"))
    archiveClassifier.set("non-obfuscated-with-deps")
    configurations = listOf(shadowImpl)
}

tasks.assemble.get().dependsOn(tasks.remapJar)
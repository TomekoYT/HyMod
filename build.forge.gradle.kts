import org.apache.commons.lang3.SystemUtils
import dev.architectury.pack200.java.Pack200Adapter

val modName = project.property("mod_name") as String
val modId = project.property("mod_id") as String
val modVersion = project.property("mod_version") as String
val modDescription = project.property("mod_description") as String
val modArchivesName = project.property("mod_archives_name") as String
val baseGroup = project.property("base_group") as String

val javaVersion = project.property("java_version") as String
val minecraftVersion = project.property("minecraft_version") as String

val hypixelModApiVersion = project.property("hypixel_mod_api_version") as String

plugins {
    idea
    java
    kotlin("jvm") version "2.4.10"
    id("gg.essential.loom") version "1.9.31"
    id("dev.architectury.architectury-pack200") version "0.1.3"
    id("com.gradleup.shadow") version "9.4.1"
    id("dev.deftu.gradle.bloom") version "0.2.0"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion))
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
        mixinConfig("mixins.$modId.json")
    }

    @Suppress("UnstableApiUsage")
    mixin {
        defaultRefmapName.set("mixins.$modId.refmap.json")
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

val shadowImpl = configurations.create("shadowImpl")
configurations.named("implementation") {
    extendsFrom(shadowImpl)
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("de.oceanlabs.mcp:mcp_stable:22-$minecraftVersion")
    forge("net.minecraftforge:forge:$minecraftVersion-11.15.1.2318-$minecraftVersion")

    shadowImpl(kotlin("stdlib-jdk8"))

    annotationProcessor("org.ow2.asm:asm-debug-all:5.2")
    annotationProcessor("com.google.guava:guava:32.1.2-jre")
    annotationProcessor("com.google.code.gson:gson:2.8.9")

    annotationProcessor("org.spongepowered:mixin:0.8.5-SNAPSHOT")
    shadowImpl("org.spongepowered:mixin:0.7.11-SNAPSHOT") {
        isTransitive = false

    }

    compileOnly("cc.polyfrost:oneconfig-$minecraftVersion-forge:0.2.2-alpha+")
    shadowImpl("cc.polyfrost:oneconfig-wrapper-launchwrapper:1.0.0-beta+")

    modImplementation("net.hypixel:mod-api-forge:$hypixelModApiVersion")
}

bloom {
    replacement("@MOD_NAME@", modName)
    replacement("@MOD_ID@", modId)
    replacement("@MOD_VERSION@", modVersion)
}

tasks.withType(JavaCompile::class) {
    options.encoding = "UTF-$javaVersion"
}

tasks.withType(org.gradle.jvm.tasks.Jar::class) {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    archiveBaseName.set("$modArchivesName-$modVersion-${minecraftVersion}_forge")
    manifest.attributes.run {
        this["FMLCorePluginContainsFMLMod"] = "true"
        this["ForceLoadAsMod"] = "true"

        this["TweakClass"] = "org.spongepowered.asm.launch.MixinTweaker"
        this["MixinConfigs"] = "mixins.$modId.json"
    }
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    val props = mapOf(
        "mod_id" to modId,
        "mod_name" to modName,
        "mod_version" to modVersion,
        "mod_description" to modDescription,
        "base_group" to baseGroup,

        "java_version" to javaVersion,
        "minecraft_version" to minecraftVersion,
    )

    inputs.properties(props)

    filesMatching(listOf("fabric.mod.json", "mcmod.info", "mixins.$modId.json")) {
        expand(props)
    }
}

val shadowJar = tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar")
val remapJar = tasks.named<net.fabricmc.loom.task.RemapJarTask>("remapJar") {
    archiveClassifier.set("")
    from(shadowJar)
    inputFile.set(shadowJar.flatMap { it.archiveFile })
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
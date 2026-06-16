import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val mod_name: String by project
val mod_id: String by project
val mod_version: String by project
val mod_description: String by project
val mod_archives_name: String by project
val base_group: String by project

val java_version: String by project
val minecraft_version: String by project
val fabric_loader_version: String by project
val fabric_api_version: String by project
val fabric_language_kotlin_version: String by project

val oneconfig_version: String by project
val mod_menu_version: String by project
val hypixel_mod_api_version: String by project

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.4.0"
    id("net.fabricmc.fabric-loom-remap") version "1.16-SNAPSHOT"
    id("dev.deftu.gradle.bloom") version "0.2.0"
}

base {
    archivesName.set("$mod_archives_name-$mod_version-${minecraft_version}_fabric")
}

repositories {
    google()

    maven("https://repo.polyfrost.org/releases")
    maven("https://repo.polyfrost.org/snapshots")
    maven("https://maven.terraformersmc.com/")
    maven("https://repo.hypixel.net/repository/Hypixel/")
}

loom {
    runConfigs.all {
        ideConfigGenerated(stonecutter.current.isActive)
        runDir = "../../run"
    }
    runConfigs.remove(runConfigs["server"])
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraft_version")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:$fabric_loader_version")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabric_api_version")
    modImplementation("net.fabricmc:fabric-language-kotlin:$fabric_language_kotlin_version")

    val oneconfigModules = arrayOf("commands", "config-impl", "events", "hud", "internal", "ui", "utils")
    for (module in oneconfigModules) {
        modImplementation("org.polyfrost.oneconfig:${module}:${oneconfig_version}")
    }
    modImplementation("org.polyfrost.oneconfig:$minecraft_version-fabric:$oneconfig_version")

    modImplementation("com.terraformersmc:modmenu:$mod_menu_version")
    modImplementation("net.hypixel:mod-api:$hypixel_mod_api_version")
}

bloom {
    replacement("@MOD_NAME@", mod_name)
    replacement("@MOD_ID@", mod_id)
    replacement("@MOD_VERSION@", mod_version)
}

tasks.processResources {
    val props = mapOf(
        "mod_id" to mod_id,
        "mod_name" to mod_name,
        "mod_version" to mod_version,
        "mod_description" to mod_description,
        "mod_archives_name" to mod_archives_name,
        "base_group" to base_group,

        "java_version" to java_version,
        "minecraft_version" to minecraft_version,
        "fabric_loader_version" to fabric_loader_version,
        "fabric_api_version" to fabric_api_version,
        "fabric_language_kotlin_version" to fabric_language_kotlin_version,

        "oneconfig_version" to oneconfig_version,
        "mod_menu_version" to mod_menu_version,
        "hypixel_mod_api_version" to hypixel_mod_api_version
    )

    inputs.properties(props)

    filesMatching(listOf("fabric.mod.json", "mixins.$mod_id.json")) {
        expand(props)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release = java_version.toInt()
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.toVersion(java_version)
    targetCompatibility = JavaVersion.toVersion(java_version)

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(java_version))
    }
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.fromTarget(java_version)
    }
}

tasks.jar {
    inputs.property("archivesName", base.archivesName)

    from("LICENSE") {
        rename { "${it}_${base.archivesName.get()}" }
    }
}
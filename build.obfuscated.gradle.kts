import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val modName = project.property("mod_name") as String
val modId = project.property("mod_id") as String
val modVersion = project.property("mod_version") as String
val modDescription = project.property("mod_description") as String
val modArchivesName = project.property("mod_archives_name") as String
val baseGroup = project.property("base_group") as String

val javaVersion = project.property("java_version") as String
val minecraftVersion = project.property("minecraft_version") as String
val fabricLoaderVersion = project.property("fabric_loader_version") as String
val fabricApiVersion = project.property("fabric_api_version") as String
val fabricLanguageKotlinVersion = project.property("fabric_language_kotlin_version") as String

val oneconfigVersion = project.property("oneconfig_version") as String
val modMenuVersion = project.property("mod_menu_version") as String
val hypixelModApiVersion = project.property("hypixel_mod_api_version") as String

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.4.10"
    id("org.jetbrains.kotlin.plugin.compose") version "2.4.10"
    id("net.fabricmc.fabric-loom-remap") version "1.17-SNAPSHOT"
    id("dev.deftu.gradle.bloom") version "0.2.0"
}

base {
    archivesName.set("$modArchivesName-$modVersion-${minecraftVersion}_fabric")
}

repositories {
    mavenCentral()
    google()

    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.stellardrift.ca/repository/maven-snapshots/")
    maven("https://repo.polyfrost.org/releases")
    maven("https://repo.polyfrost.org/snapshots")
    maven("https://maven.terraformersmc.com/")
    maven("https://repo.hypixel.net/repository/Hypixel/")
}

loom {
    runConfigs.remove(runConfigs["server"])
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")
    modImplementation("net.fabricmc:fabric-language-kotlin:$fabricLanguageKotlinVersion")

    modImplementation("org.polyfrost.oneconfig:$minecraftVersion-fabric:$oneconfigVersion")
    modImplementation("com.terraformersmc:modmenu:$modMenuVersion")
    modImplementation("net.hypixel:mod-api:$hypixelModApiVersion")
}

bloom {
    replacement("@MOD_NAME@", modName)
    replacement("@MOD_ID@", modId)
    replacement("@MOD_VERSION@", modVersion)
}

tasks.processResources {
    val props = mapOf(
        "mod_id" to modId,
        "mod_name" to modName,
        "mod_version" to modVersion,
        "mod_description" to modDescription,
        "mod_archives_name" to modArchivesName,
        "base_group" to baseGroup,

        "java_version" to javaVersion,
        "minecraft_version" to minecraftVersion,
        "fabric_loader_version" to fabricLoaderVersion,
        "fabric_api_version" to fabricApiVersion,
        "fabric_language_kotlin_version" to fabricLanguageKotlinVersion,

        "oneconfig_version" to oneconfigVersion,
        "mod_menu_version" to modMenuVersion,
        "hypixel_mod_api_version" to hypixelModApiVersion
    )

    inputs.properties(props)

    filesMatching(listOf("fabric.mod.json", "mixins.$modId.json")) {
        expand(props)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release = javaVersion.toInt()
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.toVersion(javaVersion)
    targetCompatibility = JavaVersion.toVersion(javaVersion)

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.fromTarget(javaVersion)
    }
}

tasks.jar {
    inputs.property("archivesName", base.archivesName)

    from("LICENSE") {
        rename { "${it}_${base.archivesName.get()}" }
    }
}
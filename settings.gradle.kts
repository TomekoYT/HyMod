pluginManagement {
	repositories {
		mavenCentral()
		gradlePluginPortal()

		maven("https://maven.kikugie.dev/snapshots")
		maven("https://maven.kikugie.dev/releases")

		maven("https://maven.minecraftforge.net/")
		maven("https://maven.fabricmc.net")

		maven("https://maven.architectury.dev/")
		maven("https://oss.sonatype.org/content/repositories/snapshots")
		maven("https://jitpack.io/")
		maven("https://repo.spongepowered.org/maven/")
		maven("https://repo.essential.gg/repository/maven-public")

		maven("https://maven.deftu.dev/releases")
		maven("https://maven.deftu.dev/snapshots")
	}
	resolutionStrategy {
		eachPlugin {
			when (requested.id.id) {
				"gg.essential.loom" -> useModule("gg.essential:architectury-loom:${requested.version}")
			}
		}
	}
}

plugins {
	id("dev.kikugie.stonecutter") version providers.gradleProperty("stonecutter_version")
	id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

stonecutter {
	create(rootProject) {
		version("1.8.9-forge").buildscript("build.forge.gradle.kts")
		version("1.21.11-fabric").buildscript("build.obfuscated.gradle.kts")
		versions("26.1-fabric", "26.2-fabric")
		vcsVersion = "26.1-fabric"
	}
}

dependencyResolutionManagement {
	versionCatalogs {
		create("libs")
	}
}

rootProject.name = "HyMod"
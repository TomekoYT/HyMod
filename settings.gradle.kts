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
}

stonecutter {
	create(rootProject) {
		version("1.8.9").buildscript("build.forge.gradle.kts")
		version("1.21.11").buildscript("build.obfuscated.gradle.kts")
		versions("26.1", "26.2")
		vcsVersion = "26.1"
	}
}

dependencyResolutionManagement {
	versionCatalogs {
		create("libs")
	}
}

rootProject.name = "HyMod"
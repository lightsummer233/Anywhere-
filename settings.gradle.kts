@file:Suppress("UnstableApiUsage")

pluginManagement {
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
    mavenLocal()
  }
}

dependencyResolutionManagement {
  repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
  repositories {
    google()
    mavenCentral()
    maven("https://jitpack.io")
  }
}

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "Anywhere-"
include(":app", ":color-picker")

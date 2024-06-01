// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
  repositories {
    google()
    gradlePluginPortal()
    maven("https://jitpack.io")
  }
  dependencies {
    classpath("com.android.tools.build:gradle:8.6.0-alpha04")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.0")
    classpath("com.google.devtools.ksp:symbol-processing-gradle-plugin:2.0.0-1.0.21")
    classpath("dev.rikka.tools.materialthemebuilder:gradle-plugin:1.4.0")
  }
}

allprojects {
  repositories {
    google()
    maven("https://jitpack.io")
    mavenCentral()
  }
}

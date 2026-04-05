plugins {
  id("com.android.library")
}

android {
  namespace = "com.flask.colorpicker"
  compileSdk {
    version = release(37) {
      minorApiLevel = 0
    }
  }
  buildToolsVersion = "37.0.0"
  ndkVersion = "29.0.14206865"

  defaultConfig {
    minSdk = 23
  }

  buildTypes {
    release {
      isMinifyEnabled = false
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
  }
}

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

configurations.all {
  exclude("org.jetbrains.kotlin", "kotlin-stdlib-jdk7")
  exclude("org.jetbrains.kotlin", "kotlin-stdlib-jdk8")
}

dependencies {
  api("androidx.appcompat:appcompat:1.7.1")
}

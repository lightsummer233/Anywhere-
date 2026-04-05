@file:Suppress("UnstableApiUsage")

import com.android.build.gradle.tasks.PackageAndroidArtifact
// import java.nio.file.Paths

plugins {
  id("com.android.application")
  id("com.google.devtools.ksp")
  id("org.jetbrains.kotlin.plugin.parcelize")
  id("dev.rikka.tools.materialthemebuilder")
}

android {
  namespace = "com.absinthe.anywhere_"
  compileSdk {
    version = release(37) {
      minorApiLevel = 0
    }
  }
  buildToolsVersion = "37.0.0"
  ndkVersion = "29.0.14206865"

  defaultConfig {
    applicationId = "com.absinthe.anywhere_"
    minSdk = 23
    targetSdk = 37
    versionCode = 2050500
    versionName = "2.5.5"
    manifestPlaceholders["appName"] = "Anywhere-"

    base.archivesName = "Anywhere-$versionName-$versionCode"

    ndk {
      abiFilters += listOf("arm64-v8a", "x86_64")
    }
  }

  buildFeatures {
    aidl = true
    buildConfig = true
    viewBinding = true
  }

  buildTypes {
    debug {
      applicationIdSuffix = ".debug"
      manifestPlaceholders["appName"] = "Anywhere-β"
      buildConfigField("boolean", "BETA", "true")
    }
    release {
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      buildConfigField("boolean", "BETA", "false")
    }
    all {
      buildConfigField(
        "String",
        "APP_CENTER_SECRET",
        "\"" + System.getenv("APP_CENTER_SECRET").orEmpty() + "\""
      )
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
  }

  dependenciesInfo {
    includeInBundle = false
    includeInApk = false
  }

  externalNativeBuild {
    cmake {
      path = file("CMakeLists.txt")
    }
  }

  androidResources {
    generateLocaleConfig = true
  }

  packaging {
    resources {
      excludes += "okhttp3/**"
      excludes += "kotlin/**"
      excludes += "org/**"
      excludes += "**.properties"

      excludes += "META-INF/*.version"
      excludes += "META-INF/**/LICENSE.txt"

      excludes += "DebugProbesKt.bin"

      excludes += "XPP3_1.1.3.2_VERSION"
      excludes += "XPP3_1.1.3.3_VERSION"
    }
    dex.useLegacyPackaging = false
  }
}

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

ksp {
  arg("room.incremental", "true")
  arg("room.schemaLocation", "$projectDir/schemas")
}

// https://stackoverflow.com/a/77745844
tasks.withType<PackageAndroidArtifact> {
  doFirst { appMetadata.asFile.orNull?.writeText("") }
}

materialThemeBuilder {
  themes {
    create("anywhere") {
      primaryColor = "#8BC34A"
      lightThemeFormat = "Theme.Material3.Light.%s"
      lightThemeParent = "Theme.Material3.Light.Rikka"
      darkThemeFormat = "Theme.Material3.Dark.%s"
      darkThemeParent = "Theme.Material3.Dark.Rikka"
    }
  }
  generatePalette = true
}

/*
val optimizeReleaseRes: Task = task("optimizeReleaseRes").doLast {
  val aapt2 = File(
    androidComponents.sdkComponents.sdkDirectory.get().asFile,
    "build-tools/${project.android.buildToolsVersion}/aapt2"
  )
  val abi = listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64", "universal")
  for (i in abi) {
    val zip = Paths.get(
      buildDir.path,
      "intermediates",
      "optimized_processed_res",
      "release",
      "optimizeReleaseResources",
      "resources-$i-release-optimize.ap_"
    )
    val optimized = File("${zip}.opt")
    val cmd = exec {
      commandLine(
        aapt2, "optimize",
        "--collapse-resource-names",
        "--resources-config-path",
        "aapt2-resources.cfg",
        "-o", optimized,
        zip
      )
      isIgnoreExitValue = false
    }
    if (cmd.exitValue == 0) {
      delete(zip)
      optimized.renameTo(zip.toFile())
    }
  }
}

tasks.configureEach {
  if (name == "optimizeReleaseResources") {
    finalizedBy(optimizeReleaseRes)
  }
}
 */

configurations.all {
  exclude("androidx.appcompat", "appcompat")
  exclude("org.jetbrains.kotlin", "kotlin-stdlib-jdk7")
  exclude("org.jetbrains.kotlin", "kotlin-stdlib-jdk8")
}

dependencies {
  implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

  implementation(project(":color-picker"))
  implementation(files("libs/IceBox-SDK-1.0.6.aar"))

  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

  implementation("com.github.zhaobozhen.libraries:me:1.1.4")
  implementation("com.github.zhaobozhen.libraries:utils:1.1.4")

  val appCenterSdkVersion = "5.0.6"
  implementation("com.microsoft.appcenter:appcenter-analytics:${appCenterSdkVersion}")
  implementation("com.microsoft.appcenter:appcenter-crashes:${appCenterSdkVersion}")

  // Android X
  val roomVersion = "2.8.4"
  implementation("androidx.room:room-runtime:${roomVersion}")
  implementation("androidx.room:room-ktx:${roomVersion}")
  ksp("androidx.room:room-compiler:${roomVersion}")

  val lifecycleVersion = "2.10.0"
  implementation("androidx.lifecycle:lifecycle-livedata-ktx:${lifecycleVersion}")
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${lifecycleVersion}")

  implementation("androidx.browser:browser:1.10.0")
  implementation("androidx.constraintlayout:constraintlayout:2.2.1")
  implementation("androidx.coordinatorlayout:coordinatorlayout:1.3.0")
  implementation("androidx.viewpager2:viewpager2:1.1.0")
  implementation("androidx.recyclerview:recyclerview:1.4.0")
  implementation("androidx.drawerlayout:drawerlayout:1.2.0")

  // KTX
  implementation("androidx.collection:collection-ktx:1.6.0")
  implementation("androidx.activity:activity-ktx:1.13.0")
  implementation("androidx.fragment:fragment-ktx:1.8.9")
  implementation("androidx.palette:palette-ktx:1.0.0")
  implementation("androidx.core:core-ktx:1.18.0")
  implementation("androidx.preference:preference-ktx:1.2.1")

  // Google
  implementation("com.google.android.material:material:1.13.0")

  // Function
  implementation("com.github.bumptech.glide:glide:5.0.5")
  ksp("com.github.bumptech.glide:compiler:5.0.5")

  implementation("com.google.code.gson:gson:2.13.2")
  implementation("com.google.zxing:core:3.5.4")
  implementation("com.tencent:mmkv-static:2.4.0")
  implementation("com.github.CymChad:BaseRecyclerViewAdapterHelper:3.0.11")
  implementation("com.github.heruoxin.Delegated-Scopes-Manager:client:master-SNAPSHOT")
  implementation("com.github.topjohnwu.libsu:core:6.0.0")
  implementation("com.github.thegrizzlylabs:sardine-android:0.8")
  implementation("com.jonathanfinerty.once:once:1.3.1")
  implementation("org.lsposed.hiddenapibypass:hiddenapibypass:6.1")
  implementation("com.jakewharton.timber:timber:5.0.1")

  // TODO: Remove it
  implementation("com.blankj:utilcodex:1.31.1") {
    exclude("org.jetbrains.kotlin", "kotlin-android-extensions-runtime")
  }

  // UX
  implementation("com.drakeet.about:about:2.5.2")
  implementation("com.drakeet.multitype:multitype:4.3.0")
  implementation("com.drakeet.drawer:drawer:1.0.3")
  implementation("com.github.sephiroth74:android-target-tooltip:2.0.4")
  implementation("com.leinardi.android:speed-dial:3.3.0")
  implementation("me.zhanghai.android.fastscroll:library:1.3.0")

  val shizukuVersion = "12.2.0"
  // required by Shizuku and Sui
  implementation("dev.rikka.shizuku:api:$shizukuVersion")
  // required by Shizuku
  implementation("dev.rikka.shizuku:provider:$shizukuVersion")

  implementation("dev.rikka.rikkax.appcompat:appcompat:1.6.1")
  implementation("dev.rikka.rikkax.core:core:1.4.1")
  implementation("dev.rikka.rikkax.material:material:2.7.2")
  implementation("dev.rikka.rikkax.recyclerview:recyclerview-ktx:1.3.2")
  implementation("dev.rikka.rikkax.widget:borderview:1.1.0")
  implementation("dev.rikka.rikkax.preference:simplemenu-preference:1.0.3")
  implementation("dev.rikka.rikkax.insets:insets:1.3.0")
  implementation("dev.rikka.rikkax.layoutinflater:layoutinflater:1.3.0")
  implementation("dev.rikka.rikkax.material:material-preference:2.0.0")

  // Network
  implementation("com.squareup.okhttp3:okhttp:5.3.2")
  implementation("com.squareup.retrofit2:retrofit:3.0.0")
  implementation("com.squareup.retrofit2:converter-gson:3.0.0")
  implementation("com.squareup.okio:okio:3.17.0")

  // Rx
  implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
  implementation("io.reactivex.rxjava2:rxjava:2.2.21")
  implementation("org.reactivestreams:reactive-streams:1.0.4")

  // Debug
  debugImplementation("com.squareup.leakcanary:leakcanary-android:3.0-alpha-8")
}

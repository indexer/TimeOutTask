import groovy.json.JsonSlurper
import org.jose4j.json.internal.json_simple.JSONObject

plugins {
  @Suppress("DSL_SCOPE_VIOLATION")
  alias(libs.plugins.android.application)
  @Suppress("DSL_SCOPE_VIOLATION")
  alias(libs.plugins.jetbrain.kotlin)
}

android {
  namespace = "com.indexer.timeouttask"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.indexer.timeouttask"
    minSdk= 24
    targetSdk =34
    versionCode =1
    versionName ="1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables {
      useSupportLibrary= true
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles (getDefaultProguardFile("proguard-android-optimize.txt"),("proguard-rules.pro"))
    }
  }
  compileOptions {
    sourceCompatibility =JavaVersion.VERSION_1_8
    targetCompatibility =JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }

  testOptions{
    unitTests.isReturnDefaultValues = true
  }

  buildFeatures {
    compose= true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.4.4"
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }


}

tasks.register("generateStringsXml") {
  val jsonFile = file("src/main/json/strings.json")
  val outputFile = file("src/main/res/values/strings.xml")

  doLast {
    val jsonString = jsonFile.readText()
    val jsonObject = JsonSlurper().parseText(jsonString) as JSONObject
    val xmlContent = buildString {
      appendLine("""<?xml version="1.0" encoding="utf-8"?>""")
      appendLine("<resources>")
      jsonObject.keys.forEach { key ->
        appendLine("""    <string name="$key">${jsonObject[key]}</string>""")
      }
      appendLine("</resources>")
    }

    outputFile.parentFile.mkdirs()
    outputFile.writeText(xmlContent)

    println("Generated strings.xml at ${outputFile.path}")
  }
}

dependencies {
  implementation(libs.androidx.ktx)
  implementation(libs.androidx.life.cycle)
  androidTestImplementation(libs.androidx.test.junit)
  androidTestImplementation(libs.androidx.core.espresso)
  implementation(libs.androidx.test.junit)
  implementation(libs.koin.android)
  implementation(libs.androidx.compose.ui)
  implementation(libs.jetbrain.reflection)
  implementation(libs.androidx.compose.material)
  implementation(libs.androidx.activity.compose)
  debugImplementation(libs.androidx.compose.ui.tooling)

  implementation ("com.google.android.material:material:1.9.0")
  implementation ("androidx.compose.animation:animation:1.5.0")
  implementation ("androidx.compose.material:material-icons-extended:1.5.0")

  implementation(project(":common"))
}





plugins {
  @Suppress("DSL_SCOPE_VIOLATION")
  alias(libs.plugins.android.library)
  @Suppress("DSL_SCOPE_VIOLATION")
  alias(libs.plugins.jetbrain.kotlin)
}

android {
  namespace = "com.indexer.common"
  compileSdk = 34

  defaultConfig {
    minSdk = 24
    targetSdk = 34

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
}

dependencies {
  implementation(libs.androidx.ktx)
  implementation(libs.androidx.test.junit)
  implementation(libs.koin.android)
  implementation(libs.androidx.compose.ui)
  implementation(libs.jetbrain.reflection)
  implementation(libs.androidx.compose.material)
  implementation(libs.androidx.activity.compose)
  debugImplementation(libs.androidx.compose.ui.tooling)
  implementation(libs.androidx.compose.ui.foundation)



}
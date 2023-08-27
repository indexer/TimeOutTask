# TimeOutTask
Personal Project to Try Toml &amp; Compose

# Video
![Demo Video](https://videoapi-muybridge.vimeocdn.com/animated-thumbnails/image/fa2b8fb8-5b36-47cf-9be8-c28a00528a2a.gif?ClientID=vimeo-core-prod&Date=1693111450&Signature=7a7a4701c35f24adb050d0caa64e29d5dcb04ec3)


## What is TOML?
TOML is a configuration file format that aims to strike a balance between being easy to read and write for humans and easy to parse for machines. Tom Preston-Werner, the co-founder of GitHub, created it.
TOML is characterized by its simplicity and readability. It uses key-value pairs and is inspired by INI files but with additional features and improvements. It has a minimalistic syntax that makes it easy to understand and maintain.

## Why use TOML?

TOML offers several advantages over other configuration file formats:

- **Readability**: TOML files are easy to read and understand, even for non-technical users. The syntax is clean and free of unnecessary characters, making it pleasant to work with.
- **Expressiveness**: TOML supports various data types, including strings, integers, floats, booleans, arrays, and tables. This allows you to represent complex configurations in a concise and organized manner.
- **Interoperability**: TOML has widespread support across different programming languages. Many libraries and tools provide parsers and serializers for TOML, making it easy to integrate into your projects.
- **Compatibility**: TOML is designed to work well with version control systems like Git. Its format is straightforward and diff-friendly, allowing for easy tracking of configuration changes over time.
- **Extensibility**: TOML supports the use of inline comments, which can be helpful for documenting your configuration options and providing additional context.

## TOML Syntax
Here's an example of a simple TOML configuration:

```toml
# This is a TOML configuration file

[versions]
composeMaterial = "1.4.3"
composeUiVersion = "1.4.3"
kotlinRunTime = "2.6.1"
kotlinCore = "1.10.1"
activityCompose = "1.7.2"
junitTest = "4.13.2"
junit = "1.1.5"
espressoCore="3.5.1"
androidGradlePlugin ="8.0.2"
kotlinJetbrains="1.8.10"

[libraries]
#Android
androidx-activity-compose = { module = "androidx.activity:activity-compose", version.ref = "activityCompose", name = "androidx-activity-compose" }
androidx-ktx = { module = "androidx.core:core-ktx", version.ref = "kotlinCore", name = "android-ktx" }
androidx-compose-ui = { module = "androidx.compose.ui:ui", version.ref = "composeUiVersion", name = "androidx-compose-ui" }
androidx-compose-ui-tooling-preview = { module = "androidx.compose.ui:ui-tooling-preview", version.ref = "composeUiVersion", name = "androidx-compose-ui-tooling-preview" }
androidx-life-cycle = { module = "androidx.lifecycle:lifecycle-runtime-ktx", version.ref = "kotlinRunTime", name = "androidx-life-cycle" }
androidx-compose-material = { module = "androidx.compose.material:material", version.ref = "composeMaterial", name = "androidx-compose-material" }
androidx-compose-ui-tooling={module="androidx.compose.ui:ui-tooling",version.ref="composeUiVersion",name = "androidx-compose-ui-tooling"}
#Test
junit = { module = "junit:junit", version.ref = "junitTest", name = "junit" }
androidx-test-junit = { module = "androidx.test.ext:junit",version.ref="junit",name="android.test.junit"}
androidx-core-espresso = {module="androidx.test.espresso:espresso-core",version.ref="espressoCore",name="espresso"}

[plugins]
android-application = { id = "com.android.application", version.ref = "androidGradlePlugin" }
android-library = { id = "com.android.library", version.ref = "androidGradlePlugin"}
jetbrain-kotlin = { id = "org.jetbrains.kotlin.android",version.ref="kotlinJetbrains"}

```

## kotlin Syntax
Here's an example of how to apply TOML configuration in build.gradle:
```kotlin
dependencies {
  implementation(libs.androidx.ktx)
  implementation(libs.androidx.compose.ui)
  debugImplementation(libs.androidx.compose.ui.tooling)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.life.cycle)
  implementation(libs.androidx.compose.material)
  implementation(libs.androidx.activity.compose)
  androidTestImplementation(libs.androidx.test.junit)
  androidTestImplementation(libs.androidx.core.espresso)
  implementation(libs.androidx.test.junit)

  implementation(project(":common"))

}
```

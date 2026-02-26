plugins {
    alias(libs.plugins.android.application) apply false
    id("com.android.library") version "9.0.1" apply false
    id("org.jetbrains.kotlin.multiplatform") version "2.0.21" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21" apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
}
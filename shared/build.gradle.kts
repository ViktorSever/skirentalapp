plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.library")
    id("org.jetbrains.kotlin.plugin.serialization")
}

kotlin {
    androidTarget()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

//    sourceSets {
//        val commonMain by getting
//        val commonTest by getting
//        val androidMain by getting
//
//    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
                implementation("io.ktor:ktor-client-core:2.3.8")
                implementation("io.insert-koin:koin-core:3.5.3")
                implementation("androidx.core:core-ktx:1.17.0")
            }
        }
        val commonTest by getting

        val androidMain by getting {
            dependencies {
                implementation("androidx.core:core-ktx:1.17.0")
                implementation("com.squareup.retrofit2:retrofit:2.9.0")
                implementation("com.squareup.retrofit2:converter-gson:2.9.0")
                implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")
                implementation("io.ktor:ktor-client-okhttp:2.3.8")
                implementation("com.google.firebase:firebase-firestore-ktx:24.5.0")
                implementation("androidx.preference:preference-ktx:1.2.1")
            }
        }
        val androidUnitTest by getting

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
    }
}

android {
    namespace = "com.example.skirental.shared"
    compileSdk = 36

    defaultConfig {
        minSdk = 29
    }
}


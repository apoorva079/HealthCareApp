

plugins {

    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    // This is the Firebase Google Services plugin. It must be applied here.
    id("com.google.gms.google-services")

    // If you are using specific Kotlin Compose compiler plugins, keep this.
    // Otherwise, if 'compose = true' in android block is enough, you can remove it.
    // Assuming you need it for advanced Compose features.
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.healthapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.healthapp"
        minSdk = 26
        targetSdk = 35 // You updated this to 35, which is good.
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11 // You updated this to 11, which is good.
        targetCompatibility = JavaVersion.VERSION_11 // You updated this to 11, which is good.
    }
    kotlinOptions {
        jvmTarget = "11" // You updated this to 11, which is good.
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Your existing core dependencies, using 'libs' aliases from libs.versions.toml
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)


    // Firebase BOM (Platform) - This ensures all Firebase libraries use compatible versions
    implementation(platform("com.google.firebase:firebase-bom:33.1.1"))
    // Firebase Authentication library
    implementation("com.google.firebase:firebase-auth-ktx")
    // Firebase Firestore library
    implementation("com.google.firebase:firebase-firestore-ktx")


    implementation("androidx.compose.material:material-icons-extended") // For icons like MedicalServices, Bed, Bathtub
    implementation("androidx.navigation:navigation-compose:2.7.7") // For Jetpack Compose Navigation


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
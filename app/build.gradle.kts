plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.nav.safe.args)
    kotlin("kapt")
}

val marvelPublicAPIKey = System.getenv("MARVEL_PUBLIC_API_KEY") ?: ""
val marvelPrivateAPIKey = System.getenv("MARVEL_PRIVATE_API_KEY") ?: ""
val marvelBASEURL = System.getenv("MARVEL_BASE_URL") ?: ""

android {
    namespace = "com.marvelheroesapi"
    compileSdk = 34

    android.buildFeatures.buildConfig = true

    defaultConfig {
        applicationId = "com.marvelheroesapi"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    viewBinding {
        enable = true
    }

    defaultConfig {
        buildConfigField("String", "MARVEL_PUBLIC_API_KEY", "\"$marvelPublicAPIKey\"")
        buildConfigField("String", "MARVEL_PRIVATE_API_KEY", "\"$marvelPrivateAPIKey\"")
        buildConfigField("String", "BASE_URL", "\"$marvelBASEURL\"")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}

dependencies {
    implementation(libs.android.material)
    implementation(libs.androidx.viewmodel.compose)
    implementation(libs.androidx.junit.ktx)
    kapt(libs.android.room.compiler)
    implementation(libs.android.room.paging)
    implementation(libs.android.room.ktx)
    implementation(libs.android.room.runtime)
    implementation(libs.androidx.core.testing)
    implementation(libs.com.squareup.okhttp3.logging)
    implementation(platform(libs.com.squareup.okhttp3))
    implementation(libs.androidx.pag.runt)
    implementation(libs.org.kotlinx.serialization)
    implementation(libs.com.github.glide)
    implementation(libs.com.squareup.retrofit2)
    implementation(libs.com.squareup.retrofit2.converter)
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.androidx.livedata.ktx)
    implementation(libs.io.koin)
    implementation(libs.androidx.nav.frag.ktx)
    implementation(libs.androidx.nav.ui.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.cardview)
    testImplementation(libs.junit)
    testImplementation(libs.android.kotlin.test)
    testImplementation(libs.android.mockK)
    testImplementation(libs.android.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
}

android {
    namespace = "com.example.bit_chronicles_1"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.bit_chronicles_1"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // === AÑADE ESTA LÍNEA PARA DEFINIR API_KEY EN BuildConfig ===
        // Reemplaza "YOUR_ACTUAL_API_KEY_HERE" con tu clave real.
        // Mantén las comillas dobles escapadas \" alrededor del valor.
        buildConfigField("String", "API_KEY", "\"AIzaSyDFg56YfweggI8Ja9PdYW5AjovYNDcw0Ms\"")
        // ==========================================================
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        // Si quieres la clave también en builds de depuración de forma explícita,
        // aunque default config ya la incluye.
        // debug {
        //    buildConfigField("String", "API_KEY", "\"TU_CLAVE_DE_API_AQUI\"")
        // }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true // Asegúrate de que esta línea esté en 'true' para generar BuildConfig
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    // Asegúrate de que esta dependencia para Gemini API esté incluida
    implementation(libs.generativeai)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
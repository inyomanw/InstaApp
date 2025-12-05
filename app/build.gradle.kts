plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.library)
    alias(libs.plugins.ksp.library)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.inyomanw.instaapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.inyomanw.instaapp"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter)
    implementation(libs.okhttp)
    implementation(libs.okhttp.urlconnection)
    implementation(libs.okhttp.logging.interceptor)

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    implementation(libs.lifecycle.extensions)
    implementation(libs.lifecycle.viewmodel)

    implementation(libs.coil)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.paging.runtime)
    debugImplementation(libs.chucker)

    // Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)

    // Google Sign-In
    implementation(libs.play.services.auth)
    implementation(libs.credentials.manager)
    implementation(libs.credentials.play.services)
    implementation(libs.googleid)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
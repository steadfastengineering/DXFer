plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.steadfastfabrication.dxfer"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.steadfastfabrication.dxfer"
        minSdk = 34
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // Kabeja jars both contain a file at the same path. This allows us to build by ignoring it.
    packaging {
        resources {
            excludes += "/icons/copy_edit.gif"
            excludes += "/icons/cut_edit.gif"
            excludes += "/icons/paste_edit.gif"
            excludes += "/icons/print.gif"
        }
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // Includes all JAR files in the libs folder
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    // Include special SVG view container
    implementation("com.caverock:androidsvg:1.4")
}
plugins {
    alias(libs.plugins.nowinandroid.android.application)
    alias(libs.plugins.nowinandroid.android.application.compose)
    alias(libs.plugins.nowinandroid.android.application.flavors)
    alias(libs.plugins.nowinandroid.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "cn.thecover.media"

    defaultConfig {
        applicationId = "com.charleslee.compose"
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            abiFilters += listOf("arm64-v8a")
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file("ynperformance.jks")
            storePassword = "ynrb123456"
            keyAlias = "ynperformance"
            keyPassword = "ynrb123456"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
        resValues = true
        compose = true
    }

    lint {
        checkReleaseBuilds = false
        abortOnError = false
        disable += "MissingTranslation"
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.network)
    implementation(projects.core.data)
    implementation(projects.core.widget)

    implementation(projects.feature.basis)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.coroutines.guava)
    implementation(libs.kotlinx.serialization.json)

    ksp(libs.hilt.compiler)
    kspTest(libs.hilt.compiler)
    testImplementation(libs.hilt.android.testing)
    testImplementation(libs.kotlin.test)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.kotlin.test)
}

dependencyGuard {
    configuration("onlineReleaseRuntimeClasspath")
}

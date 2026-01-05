plugins {
    alias(libs.plugins.nowinandroid.android.library)
    alias(libs.plugins.nowinandroid.hilt)
    id("kotlinx-serialization")
}

android {
    namespace = "cn.thecover.media.core.network"
    buildFeatures {
        buildConfig = true
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.widget)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.coil)
    api(libs.okhttp.logging)
    api(libs.retrofit.core)
    api(libs.retrofit.converter.gson)
    api(libs.retrofit.kotlin.serialization)
    testImplementation(libs.kotlinx.coroutines.test)
}
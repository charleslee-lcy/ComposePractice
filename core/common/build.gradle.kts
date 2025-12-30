plugins {
//    alias(libs.plugins.nowinandroid.jvm.library)
    alias(libs.plugins.nowinandroid.hilt)
    alias(libs.plugins.nowinandroid.android.library)
    id("kotlinx-serialization")
}

android {
    namespace = "cn.thecover.media.core.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)
    api(libs.kotlinx.datetime)
    api(libs.webview)
    api(libs.dataStore)
    api(libs.logger)
}
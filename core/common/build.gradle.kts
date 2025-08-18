plugins {
    alias(libs.plugins.nowinandroid.jvm.library)
    alias(libs.plugins.nowinandroid.hilt)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)
    api(libs.kotlinx.datetime)
    api(libs.webview)
    api(libs.dataStore)
}
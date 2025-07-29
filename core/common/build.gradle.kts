plugins {
    alias(libs.plugins.nowinandroid.jvm.library)
//    alias(libs.plugins.nowinandroid.android.library)
    alias(libs.plugins.nowinandroid.hilt)
//    id("com.android.library")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)
    api(libs.kotlinx.datetime)
}
plugins {
    alias(libs.plugins.nowinandroid.android.library)
    alias(libs.plugins.nowinandroid.android.library.compose)
    alias(libs.plugins.nowinandroid.hilt)
}

android {
    namespace = "cn.thecover.media.core.widget"
}

dependencies {
    api(projects.core.common)
    api(projects.core.data)

    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material3.adaptive)
    api(libs.androidx.compose.material3.navigationSuite)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.ui.util)
    api(libs.androidx.activity.compose)
    api(libs.androidx.constraintLayout)
    api(libs.bundles.coil)
    api(libs.androidx.browser)

    testImplementation(libs.hilt.android.testing)
}
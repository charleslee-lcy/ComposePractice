package com.google.samples.apps.nowinandroid

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.ApplicationProductFlavor
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ProductFlavor

@Suppress("EnumEntryName")
enum class FlavorDimension {
    apiType
}

// The content for the app can either come from local static data which is useful for demo
// purposes, or from a production backend server which supplies up-to-date, real content.
// These two product flavors reflect this behaviour.
@Suppress("EnumEntryName")
enum class NiaFlavor(val dimension: FlavorDimension, val applicationIdSuffix: String? = null) {
    online(FlavorDimension.apiType),
    qa(FlavorDimension.apiType, applicationIdSuffix = ".qa")
}

fun configureFlavors(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    flavorConfigurationBlock: ProductFlavor.(flavor: NiaFlavor) -> Unit = {},
) {
    commonExtension.apply {
        FlavorDimension.values().forEach { flavorDimension ->
            flavorDimensions += flavorDimension.name
        }

        productFlavors {
            NiaFlavor.values().forEach { niaFlavor ->
                register(niaFlavor.name) {
                    dimension = niaFlavor.dimension.name
                    flavorConfigurationBlock(this, niaFlavor)
                    if (this@apply is ApplicationExtension && this is ApplicationProductFlavor) {
                        if (niaFlavor.applicationIdSuffix != null) {
                            applicationIdSuffix = niaFlavor.applicationIdSuffix
                        }
                    }

                    when (niaFlavor) {
                        NiaFlavor.qa -> {
                            buildConfigField("int", "ENV_CONFIG", "1")
                            resValue("string", "app_name", "ComposePractice(qa)")
                        }
                        NiaFlavor.online -> {
                            buildConfigField("int", "ENV_CONFIG", "2")
                            resValue("string", "app_name", "ComposePractice")
                        }
                    }
                }
            }
        }
    }
}

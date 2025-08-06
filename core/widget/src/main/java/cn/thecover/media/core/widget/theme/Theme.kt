/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.thecover.media.core.widget.theme

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/**
 * 日间主题色设定
 * 容器颜色是指用于承载内容的背景色，它们与主要颜色（primary）相关但用途不同常见使用场景如信息展示卡片
 * surface: 通用的 UI 组件背景
 * container: 强调特定内容或功能的背景
 */
@VisibleForTesting
val LightDefaultColorScheme = lightColorScheme(
    // 主要颜色 - 用于关键组件，如 FAB、重要按钮等
    primary = MainColor,
    // 主要颜色上的内容 - 在 primary 颜色上显示的文本和图标颜色
    onPrimary = Color.White,
    // 主要颜色容器 - 用于容纳主要颜色内容的容器背景
    primaryContainer = Color(0xFF306CFF).copy(0.1f),
    // 主要颜色容器上的内容 - 在 primaryContainer 上显示的文本和图标颜色
    onPrimaryContainer = MainColor,

    // 次要颜色 - 用于次要组件，如次要按钮等
    secondary = Orange40,
    // 次要颜色上的内容 - 在 secondary 颜色上显示的文本和图标颜色
    onSecondary = Color.White,
    // 次要颜色容器 - 用于容纳次要颜色内容的容器背景
    secondaryContainer = Orange90,
    // 次要颜色容器上的内容 - 在 secondaryContainer 上显示的文本和图标颜色
    onSecondaryContainer = Orange10,

    // 第三颜色 - 用于装饰性或补充性元素
    tertiary = Blue40,
    // 第三颜色上的内容 - 在 tertiary 颜色上显示的文本和图标颜色
    onTertiary = Color.White,
    // 第三颜色容器 - 用于容纳第三颜色内容的容器背景
    tertiaryContainer = Blue90,
    // 第三颜色容器上的内容 - 在 tertiaryContainer 上显示的文本和图标颜色
    onTertiaryContainer = Blue10,

    // 错误颜色 - 用于表示错误状态的元素
    error = Color(0xFFF53E3E),
    // 错误颜色上的内容 - 在 error 颜色上显示的文本和图标颜色
    onError = Color.White,
    // 错误颜色容器 - 用于容纳错误状态内容的容器背景
    errorContainer = Red90,
    // 错误颜色容器上的内容 - 在 errorContainer 上显示的文本和图标颜色
    onErrorContainer = Red10,

    // 背景颜色 - 应用程序的主要背景颜色
    background = Color(0xFFFAFAFA),
    // 背景上的内容 - 在 background 上显示的文本和图标颜色
    onBackground = DarkPurpleGray10,

    // 表面颜色 - UI 组件（如卡片、列表）的表面颜色
    surface = Color(0xFFFFFFFF),
    // 表面上的内容 - 在 surface 上显示的文本和图标颜色
    onSurface = Color(0xFF1A1A1A),

    // 表面变体颜色 - surface 的变体，用于区分不同类型的表面
    surfaceVariant = Color(0xFFFAFAFA),
    // 表面变体上的内容 - 在 surfaceVariant 上显示的文本和图标颜色
    onSurfaceVariant = Color(0xFF737373),

    // 反转表面颜色 - 与主要表面颜色形成对比的表面颜色
    inverseSurface = DarkPurpleGray20,
    // 反转表面上的内容 - 在 inverseSurface 上显示的文本和图标颜色
    inverseOnSurface = DarkPurpleGray95,

    // 轮廓颜色 - 用于边框、分隔线等轮廓元素
    outline = Color(0xFFE5E5E5),
    // 轮廓边框颜色
    outlineVariant = Color(0xFFE5E5E5),
)


/**
 * Dark default theme color scheme
 */
@VisibleForTesting
val DarkDefaultColorScheme = darkColorScheme(
    primary = Purple80,
    onPrimary = Purple20,
    primaryContainer = Purple30,
    onPrimaryContainer = Purple90,
    secondary = Orange80,
    onSecondary = Orange20,
    secondaryContainer = Orange30,
    onSecondaryContainer = Orange90,
    tertiary = Blue80,
    onTertiary = Blue20,
    tertiaryContainer = Blue30,
    onTertiaryContainer = Blue90,
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = DarkPurpleGray10,
    onBackground = DarkPurpleGray90,
    surface = DarkPurpleGray10,
    onSurface = DarkPurpleGray90,
    surfaceVariant = PurpleGray30,
    onSurfaceVariant = PurpleGray80,
    inverseSurface = DarkPurpleGray90,
    inverseOnSurface = DarkPurpleGray10,
    outline = PurpleGray60,
)

/**
 * Light Android theme color scheme
 */
@VisibleForTesting
val LightAndroidColorScheme = lightColorScheme(
    primary = Green40,
    onPrimary = Color.White,
    primaryContainer = Green90,
    onPrimaryContainer = Green10,
    secondary = DarkGreen40,
    onSecondary = Color.White,
    secondaryContainer = DarkGreen90,
    onSecondaryContainer = DarkGreen10,
    tertiary = Teal40,
    onTertiary = Color.White,
    tertiaryContainer = Teal90,
    onTertiaryContainer = Teal10,
    error = Red40,
    onError = Color.White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    background = DarkGreenGray99,
    onBackground = DarkGreenGray10,
    surface = DarkGreenGray99,
    onSurface = DarkGreenGray10,
    surfaceVariant = GreenGray90,
    onSurfaceVariant = GreenGray30,
    inverseSurface = DarkGreenGray20,
    inverseOnSurface = DarkGreenGray95,
    outline = GreenGray50,
)

/**
 * Dark Android theme color scheme
 */
@VisibleForTesting
val DarkAndroidColorScheme = darkColorScheme(
    primary = Green80,
    onPrimary = Green20,
    primaryContainer = Green30,
    onPrimaryContainer = Green90,
    secondary = DarkGreen80,
    onSecondary = DarkGreen20,
    secondaryContainer = DarkGreen30,
    onSecondaryContainer = DarkGreen90,
    tertiary = Teal80,
    onTertiary = Teal20,
    tertiaryContainer = Teal30,
    onTertiaryContainer = Teal90,
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = DarkGreenGray10,
    onBackground = DarkGreenGray90,
    surface = DarkGreenGray10,
    onSurface = DarkGreenGray90,
    surfaceVariant = GreenGray30,
    onSurfaceVariant = GreenGray80,
    inverseSurface = DarkGreenGray90,
    inverseOnSurface = DarkGreenGray10,
    outline = GreenGray60,
)

/**
 * Light Android gradient colors
 */
val LightAndroidGradientColors = GradientColors(container = DarkGreenGray95)

/**
 * Dark Android gradient colors
 */
val DarkAndroidGradientColors = GradientColors(container = Color.Black)

/**
 * Light Android background theme
 */
val LightAndroidBackgroundTheme = BackgroundTheme(color = DarkGreenGray95)

/**
 * Dark Android background theme
 */
val DarkAndroidBackgroundTheme = BackgroundTheme(color = Color.Black)

/**
 * Now in Android theme.
 *
 * @param darkTheme Whether the theme should use a dark color scheme (follows system by default).
 * @param androidTheme Whether the theme should use the Android theme color scheme instead of the
 *        default theme.
 * @param disableDynamicTheming If `true`, disables the use of dynamic theming, even when it is
 *        supported. This parameter has no effect if [androidTheme] is `true`.
 */
@Composable
fun YBTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    androidTheme: Boolean = false,
    disableDynamicTheming: Boolean = true,
    content: @Composable () -> Unit,
) {
    // Color scheme
    val colorScheme = when {
        androidTheme -> if (darkTheme) DarkAndroidColorScheme else LightDefaultColorScheme
        !disableDynamicTheming && supportsDynamicTheming() -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        else -> if (darkTheme) DarkDefaultColorScheme else LightDefaultColorScheme
    }
    // Gradient colors
    val emptyGradientColors = GradientColors(container = colorScheme.surfaceColorAtElevation(2.dp))
    val defaultGradientColors = GradientColors(
        top = colorScheme.inverseOnSurface,
        bottom = colorScheme.primaryContainer,
        container = colorScheme.surface,
    )
    val gradientColors = when {
        androidTheme -> if (darkTheme) DarkAndroidGradientColors else LightAndroidGradientColors
        !disableDynamicTheming && supportsDynamicTheming() -> emptyGradientColors
        else -> defaultGradientColors
    }
    // Background theme
    val defaultBackgroundTheme = BackgroundTheme(
        color = colorScheme.background,
        tonalElevation = 2.dp,
    )
    val backgroundTheme = when {
        androidTheme -> if (darkTheme) DarkAndroidBackgroundTheme else LightAndroidBackgroundTheme
        else -> defaultBackgroundTheme
    }
    val tintTheme = when {
        androidTheme -> TintTheme()
        !disableDynamicTheming && supportsDynamicTheming() -> TintTheme(colorScheme.primary)
        else -> TintTheme()
    }
    // Composition locals
    CompositionLocalProvider(
        LocalGradientColors provides gradientColors,
        LocalBackgroundTheme provides backgroundTheme,
        LocalTintTheme provides tintTheme,
        LocalRippleConfiguration provides null
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = YBTypography,
            content = content,
        )
    }
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun supportsDynamicTheming() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

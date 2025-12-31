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

package cn.thecover.media.navigation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import cn.thecover.media.core.widget.event.showToast
import cn.thecover.media.feature.basis.home.navigation.SplashRoute
import cn.thecover.media.feature.basis.home.navigation.homeIndex
import cn.thecover.media.feature.basis.mine.navigation.mineScreen
import cn.thecover.media.ui.CommonAppState

/**
 * Top-level navigation graph. Navigation is organized as explained at
 * https://d.android.com/jetpack/compose/nav-adaptive
 *
 * The navigation graph defined in this file defines the different top level routes. Navigation
 * within each route is handled using state and Back Handlers.
 */
@Composable
fun MainNavHost(
    appState: CommonAppState,
    onShowSnackBar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController

    val isTopLevelDestination = appState.isTopLevelDestination == true

    NavHost(
        navController = navController,
        startDestination = SplashRoute,
        modifier = modifier,
        enterTransition = {
            if (!isTopLevelDestination) {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300))
            } else {
                fadeIn(animationSpec = tween(300))
            }
        },
        exitTransition = {
            if (!isTopLevelDestination) {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            } else {
                fadeOut(animationSpec = tween(300))
            }
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        }
    ) {
        homeIndex(navController)
        mineScreen(navController)
    }

    // 全局拦截
    DoubleBackExitHandler(isTopLevelDestination)
}

@Composable
fun DoubleBackExitHandler(isTopLevelDestination: Boolean) {
    val context = LocalContext.current
    var lastBackPress by remember { mutableLongStateOf(0L) }
    val exitDelay = 2000L // 2s

    BackHandler(isTopLevelDestination) {
        val now = System.currentTimeMillis()
        if (now - lastBackPress < exitDelay) {
            // 2 秒内再次按返回 → 真正退出
            (context as Activity).finishAffinity()
        } else {
            lastBackPress = now
            showToast("再次返回退出应用")
        }
    }
}

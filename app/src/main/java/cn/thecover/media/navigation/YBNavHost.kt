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

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import cn.thecover.media.feature.basis.home.navigation.HomeBaseRoute
import cn.thecover.media.feature.basis.home.navigation.HomeRoute
import cn.thecover.media.feature.basis.home.navigation.LoginRoute
import cn.thecover.media.feature.basis.home.navigation.homeIndex
import cn.thecover.media.feature.basis.mine.navigation.mineScreen
import cn.thecover.media.feature.review_data.navigation.reviewDataScreen
import cn.thecover.media.feature.review_manager.navigation.reviewManageScreen
import cn.thecover.media.ui.YBAppState

/**
 * Top-level navigation graph. Navigation is organized as explained at
 * https://d.android.com/jetpack/compose/nav-adaptive
 *
 * The navigation graph defined in this file defines the different top level routes. Navigation
 * within each route is handled using state and Back Handlers.
 */
@Composable
fun YBNavHost(
    appState: YBAppState,
    onShowSnackBar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = LoginRoute,
        modifier = modifier,
        enterTransition = {
            fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(300))
        }
    ) {
        homeIndex(navController)
        reviewManageScreen()
        reviewDataScreen()
        mineScreen(navController)
    }
}

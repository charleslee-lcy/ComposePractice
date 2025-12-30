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

package cn.thecover.media.feature.basis.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import cn.thecover.media.feature.basis.MainScreen
import cn.thecover.media.feature.basis.SplashRoute
import cn.thecover.media.feature.basis.home.HomeRoute
import cn.thecover.media.feature.basis.login.LoginRoute
import cn.thecover.media.feature.basis.message.MessageRoute
import kotlinx.serialization.Serializable

@Serializable
data object SplashRoute // route to ForYou screen

@Serializable
data object LoginRoute // route to base navigation graph

@Serializable
data object HomeRoute // route to ForYou screen

@Serializable
data object HomeBaseRoute // route to base navigation graph

@Serializable
data object MessageRoute // route to 消息通知 navigation graph

fun NavController.navigateToMessage(navOptions: NavOptions? = null) =
    navigate(MessageRoute, navOptions)

fun NavController.navigateToLogin(navOptions: NavOptions? = null) =
    navigate(route = LoginRoute, navOptions)

fun NavController.navigateToHome(navOptions: NavOptions) = navigate(route = HomeRoute, navOptions)

fun NavGraphBuilder.homeIndex(navController: NavController) {
    composable<SplashRoute> {
        SplashRoute(navController = navController)
    }

    composable<LoginRoute> {
//        LoginRoute(navController = navController)
        MainScreen()
    }

    composable<HomeRoute>(
        deepLinks = listOf(
            navDeepLink {
                /**
                 * This destination has a deep link that enables a specific news resource to be
                 * opened from a notification (@see SystemTrayNotifier for more). The news resource
                 * ID is sent in the URI rather than being modelled in the route type because it's
                 * transient data (stored in SavedStateHandle) that is cleared after the user has
                 * opened the news resource.
                 */
                uriPattern = ""
            },
        ),
    ) {
        HomeRoute(navController)
    }


    composable<MessageRoute> {
        MessageRoute(onPopBack = {
            navController.popBackStack()
        })
    }
}

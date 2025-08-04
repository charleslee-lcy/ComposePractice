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

import androidx.annotation.StringRes
import cn.thecover.media.feature.basis.R
import cn.thecover.media.feature.basis.home.navigation.HomeBaseRoute
import cn.thecover.media.feature.basis.home.navigation.HomeRoute
import cn.thecover.media.feature.basis.mine.navigation.MineRoute
import cn.thecover.media.feature.review_data.navigation.ReviewDataRoute
import cn.thecover.media.feature.review_manager.navigation.ReviewManageRoute
import kotlin.reflect.KClass

/**
 * Type for the top level destinations in the application. Contains metadata about the destination
 * that is used in the top app bar and common navigation UI.
 *
 * @param selectedIcon The icon to be displayed in the navigation UI when this destination is
 * selected.
 * @param unselectedIcon The icon to be displayed in the navigation UI when this destination is
 * not selected.
 * @param iconTextId Text that to be displayed in the navigation UI.
 * @param titleTextId Text that is displayed on the top app bar.
 * @param route The route to use when navigating to this destination.
 * @param baseRoute The highest ancestor of this destination. Defaults to [route], meaning that
 * there is a single destination in that section of the app (no nested destinations).
 */
enum class TopLevelDestination(
    val selectedIcon: Any,
    val unselectedIcon: Any,
    @StringRes val iconTextId: Int,
    @StringRes val titleTextId: Int,
    val route: KClass<*>,
    val baseRoute: KClass<*> = route,
) {
    HOME(
        selectedIcon = R.mipmap.ic_tab_home_normal,
        unselectedIcon = R.mipmap.ic_tab_home_checked,
        iconTextId = R.string.feature_home_title,
        titleTextId = R.string.feature_home_title,
        route = HomeRoute::class,
        baseRoute = HomeBaseRoute::class,
    ),
    REVIEW_MANAGE(
        selectedIcon = R.mipmap.ic_tab_review_manage_normal,
        unselectedIcon = R.mipmap.ic_tab_review_manage_checked,
        iconTextId = R.string.feature_review_manage_title,
        titleTextId = R.string.feature_review_manage_title,
        route = ReviewManageRoute::class,
    ),
    REVIEW_DATA(
        selectedIcon = R.mipmap.ic_tab_review_data_normal,
        unselectedIcon = R.mipmap.ic_tab_review_data_checked,
        iconTextId = R.string.feature_review_data_title,
        titleTextId = R.string.feature_review_data_title,
        route = ReviewDataRoute::class,
    ),
    MINE(
        selectedIcon = R.mipmap.ic_tab_mine_normal,
        unselectedIcon = R.mipmap.ic_tab_mine_checked,
        iconTextId = R.string.feature_mine_title,
        titleTextId = R.string.feature_mine_title,
        route = MineRoute::class,
    )
}

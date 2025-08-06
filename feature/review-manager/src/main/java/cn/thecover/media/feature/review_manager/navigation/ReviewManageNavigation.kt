package cn.thecover.media.feature.review_manager.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import cn.thecover.media.feature.review_manager.ReviewManageRoute
import cn.thecover.media.feature.review_manager.appeal.AppealDetailRoute
import kotlinx.serialization.Serializable


/**
 *
 * <p> Created by CharlesLee on 2025/7/28
 * 15708478830@163.com
 */

@Serializable object ReviewManageRoute
@Serializable object AppealDetailRoute

fun NavController.navigateToReviewManage(navOptions: NavOptions? = null) =
    navigate(route = ReviewManageRoute, navOptions)

fun NavController.navigateToAppealDetail(navOptions: NavOptions? = null) =
    navigate(route = AppealDetailRoute, navOptions)

fun NavGraphBuilder.reviewManageScreen(navController: NavController) {
    composable<ReviewManageRoute> {
        ReviewManageRoute(navController = navController)
    }
    composable<AppealDetailRoute> {
        AppealDetailRoute(navController = navController)
    }
}
package cn.thecover.media.feature.review_manager.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import cn.thecover.media.feature.review_manager.ReviewManageRoute
import kotlinx.serialization.Serializable


/**
 *
 * <p> Created by CharlesLee on 2025/7/28
 * 15708478830@163.com
 */

@Serializable object ReviewManageRoute

fun NavController.navigateToReviewManage(navOptions: NavOptions) =
    navigate(route = ReviewManageRoute, navOptions)

fun NavGraphBuilder.reviewManageScreen() {
    composable<ReviewManageRoute> {
        ReviewManageRoute()
    }
}
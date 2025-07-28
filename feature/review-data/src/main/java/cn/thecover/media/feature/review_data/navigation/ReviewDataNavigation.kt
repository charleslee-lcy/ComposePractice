package cn.thecover.media.feature.review_data.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import cn.thecover.media.feature.review_data.ReviewDataRoute
import kotlinx.serialization.Serializable


/**
 *
 * <p> Created by CharlesLee on 2025/7/28
 * 15708478830@163.com
 */

@Serializable object ReviewDataRoute

fun NavController.navigateToReviewData(navOptions: NavOptions) =
    navigate(route = ReviewDataRoute, navOptions)

fun NavGraphBuilder.reviewDataScreen() {
    composable<ReviewDataRoute> {
        ReviewDataRoute()
    }
}
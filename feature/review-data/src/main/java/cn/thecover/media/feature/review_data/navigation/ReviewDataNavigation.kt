package cn.thecover.media.feature.review_data.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import cn.thecover.media.feature.review_data.ReviewDataRoute
import cn.thecover.media.feature.review_data.department_review.DepartmentReviewScreen
import cn.thecover.media.feature.review_data.department_review.DepartmentTaskReviewPage
import kotlinx.serialization.Serializable


/**
 *
 * <p> Created by CharlesLee on 2025/7/28
 * 15708478830@163.com
 */

@Serializable
object ReviewDataRoute

@Serializable
object DepartmentReviewRoute
@Serializable
object DepartmentTaskReviewRoute

fun NavController.navigateToReviewData(navOptions: NavOptions) =
    navigate(route = ReviewDataRoute, navOptions)

fun NavGraphBuilder.reviewDataScreen() {
    composable<ReviewDataRoute> {
        ReviewDataRoute()
    }
}

fun NavGraphBuilder.reviewDataPage() {
    composable<DepartmentReviewRoute> {
        DepartmentReviewScreen()
    }
    composable<DepartmentTaskReviewRoute> {
        DepartmentTaskReviewPage()
    }
}

internal fun NavController.navigateToDepartmentReviewPage() {
    navigate(DepartmentReviewRoute)
}

internal fun NavController.navigateToDepartmentTaskReviewPage() {
    navigate(DepartmentTaskReviewRoute)
}

enum class ReviewDataNavigationType(
    val cateName: String,
    val navigation: (navController: NavController) -> Unit
) {
    DepartmentTotalData(
        "部门总数据排行",
        { nav -> nav.navigateToDepartmentReviewPage() }
    ),

    DepartmentTaskData(
        "部门完成任务情况",
        { nav -> nav.navigateToDepartmentTaskReviewPage() }
    )
}
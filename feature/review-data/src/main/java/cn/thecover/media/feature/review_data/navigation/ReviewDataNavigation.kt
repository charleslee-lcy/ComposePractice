package cn.thecover.media.feature.review_data.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import cn.thecover.media.feature.review_data.ReviewDataRoute
import cn.thecover.media.feature.review_data.ReviewDataViewModel
import cn.thecover.media.feature.review_data.department_review.DepartmentReviewScreen
import cn.thecover.media.feature.review_data.department_review.DepartmentTaskReviewPage
import cn.thecover.media.feature.review_data.department_review.DepartmentTopRankingPage
import cn.thecover.media.feature.review_data.manuscript_review.manuscript_diffusion.ManuscriptDiffusionPage
import cn.thecover.media.feature.review_data.manuscript_review.review.ManuscriptReviewPage
import cn.thecover.media.feature.review_data.manuscript_review.topranking.ManuscriptTopRankingPage
import kotlinx.serialization.Serializable


/**
 *¬
 * <p> Created by CharlesLee on 2025/7/28
 * 15708478830@163.com
 */

/**
 * 定义 ReviewData 页面的路由标识对象，用于导航。
 */
@Serializable
object ReviewDataRoute

/**
 * 部门总数据排行页面的路由标识对象。
 */
@Serializable
internal object DepartmentReviewRoute

/**
 * 部门任务完成情况页面的路由标识对象。
 */
@Serializable
internal object DepartmentTaskReviewRoute

/**
 * 部门 TOP 排行页面的路由标识对象。
 */
@Serializable
internal object DepartmentTopRankingRoute

/**
 * 稿件总排行页面的路由标识对象。
 */
@Serializable
internal object ManuscriptReviewRoute

/**
 * 稿件 TOP 排行页面的路由标识对象。
 */
@Serializable
internal object ManuscriptTopRoute

/**
 * 稿件传播效果页面的路由标识对象。
 */
@Serializable
internal object ManuscriptDiffusionRoute

/**
 * 导航到 ReviewData 页面。
 *
 * @param navOptions 导航选项，控制导航行为（如是否弹出栈等）。
 */
fun NavController.navigateToReviewData(navOptions: NavOptions) =
    navigate(route = ReviewDataRoute, navOptions)

/**
 * 在导航图中注册 ReviewDataRoute 页面。
 * 该页面使用 ReviewDataRoute 作为路由标识。
 */
fun NavGraphBuilder.reviewDataScreen(routeToMsgScreen:()-> Unit) {
    composable<ReviewDataRoute> {
        ReviewDataRoute(routeToMsgScreen = routeToMsgScreen)
    }
}

/**
 * 在导航图中注册多个 ReviewData 相关子页面。
 * 包括部门和稿件相关的多个页面路由。
 */
fun NavGraphBuilder.reviewDataPage(viewModel: ReviewDataViewModel ) {
    composable<DepartmentReviewRoute> {
        DepartmentReviewScreen(viewmodel = viewModel)
    }
    composable<DepartmentTaskReviewRoute> {
        DepartmentTaskReviewPage(viewModel = viewModel)
    }
    composable<DepartmentTopRankingRoute> {
        DepartmentTopRankingPage(viewModel = viewModel)
    }
    composable<ManuscriptTopRoute> {
        ManuscriptTopRankingPage(viewModel = viewModel)
    }
    composable<ManuscriptReviewRoute> {
        ManuscriptReviewPage(viewModel = viewModel)
    }
    composable<ManuscriptDiffusionRoute> {
        ManuscriptDiffusionPage(viewModel = viewModel)
    }
}

/**
 * 导航到部门总数据排行页面。
 */
internal fun NavController.navigateToDepartmentReviewPage() {
    navigate(DepartmentReviewRoute)
}

/**
 * 导航到部门任务完成情况页面。
 */
internal fun NavController.navigateToDepartmentTaskReviewPage() {
    navigate(DepartmentTaskReviewRoute)
}

/**
 * 定义 ReviewData 模块中各个页面的导航类型枚举。
 *
 * @property cateName 页面中文名称，用于展示。
 * @property navigation 导航函数，用于执行跳转逻辑。
 * @property route 页面对应的路由字符串标识。
 */
enum class ReviewDataNavigationType(
    val cateName: String,
    val navigation: (navController: NavController) -> Unit,
    val route: String,
) {
    /**
     * 稿件总排行页面。
     */
    MANUSCRIPT_REVIEW(
        "稿件总排行",
        { nav -> nav.navigate(ManuscriptReviewRoute) },
        ManuscriptReviewRoute.toString(),
    ),

    /**
     * 稿件传播效果页面。
     */
    MANUSCRIPT_DIFFUSION(
        "稿件传播效果",
        { nav -> nav.navigate(ManuscriptDiffusionRoute) },
        ManuscriptDiffusionRoute.toString(),
    ),

    /**
     * 稿件 TOP 排行页面。
     */
    MANUSCRIPT_TOP_RANKING(
        "稿件TOP排行",
        { nav -> nav.navigate(ManuscriptTopRoute) },
        ManuscriptTopRoute.toString(),
    ),

    /**
     * 部门总数据排行页面。
     */
    DEPARTMENT_TOTAL_DATA(
        "部门总数据排行",
        { nav -> nav.navigateToDepartmentReviewPage() },
        DepartmentReviewRoute.toString(),
    ),

    /**
     * 部门任务完成情况页面。
     */
    DEPARTMENT_TASK_DATA(
        "部门完成任务情况",
        { nav -> nav.navigateToDepartmentTaskReviewPage() },
        DepartmentTaskReviewRoute.toString(),
    ),

    /**
     * 部门 TOP 排行页面。
     */
    DEPARTMENT_TOP_RANKING(
        "部门TOP排行",
        { nav -> nav.navigate(DepartmentTopRankingRoute) },
        DepartmentTopRankingRoute.toString(),
    ),
}

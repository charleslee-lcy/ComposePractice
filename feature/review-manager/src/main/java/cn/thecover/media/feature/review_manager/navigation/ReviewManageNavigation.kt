package cn.thecover.media.feature.review_manager.navigation

import android.R.attr.type
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navOptions
import androidx.navigation.toRoute
import cn.thecover.media.feature.review_manager.ArchiveDetailRoute
import cn.thecover.media.feature.review_manager.ArchiveListData
import cn.thecover.media.feature.review_manager.ReviewManageRoute
import cn.thecover.media.feature.review_manager.appeal.AppealDetailRoute
import kotlinx.serialization.Serializable
import java.util.Map.entry


/**
 *
 * <p> Created by CharlesLee on 2025/7/28
 * 15708478830@163.com
 */

@Serializable object ReviewManageRoute
@Serializable object AppealDetailRoute
@Serializable data class ArchiveDetailRoute(val link: String)


fun NavController.navigateToReviewManage(navOptions: NavOptions? = null) =
    navigate(route = ReviewManageRoute, navOptions)

fun NavController.navigateToAppealDetail(navOptions: NavOptions? = null) =
    navigate(route = AppealDetailRoute, navOptions)

fun NavController.navigateToArchiveDetail(archiveListData: ArchiveListData, navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(route = archiveListData) {
        navOptions()
    }
}

fun NavGraphBuilder.reviewManageScreen(navController: NavController, routeToMsgScreen: () -> Unit = {}) {
    composable<ReviewManageRoute> {
        ReviewManageRoute(navController = navController, routeToMsgScreen = routeToMsgScreen)
    }
    composable<AppealDetailRoute> {
        AppealDetailRoute(navController = navController)
    }
    composable<ArchiveListData>() {
        val data = it.toRoute<ArchiveListData>()
        ArchiveDetailRoute(data = data, navController = navController)
    }
}
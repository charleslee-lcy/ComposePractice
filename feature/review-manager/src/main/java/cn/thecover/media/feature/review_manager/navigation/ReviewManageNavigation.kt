package cn.thecover.media.feature.review_manager.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import cn.thecover.media.core.data.ArchiveListData
import cn.thecover.media.feature.review_manager.ArchiveDetailRoute
import cn.thecover.media.feature.review_manager.ReviewManageRoute
import cn.thecover.media.feature.review_manager.appeal.AppealDetailRoute
import com.google.gson.Gson
import kotlinx.serialization.Serializable


/**
 *
 * <p> Created by CharlesLee on 2025/7/28
 * 15708478830@163.com
 */

@Serializable object ReviewManageRoute
@Serializable data class AppealDetailRoute(val canEdit: Boolean)
@Serializable data class ArchiveDetailRoute(val dataJsonStr: String)


fun NavController.navigateToReviewManage(navOptions: NavOptions? = null) =
    navigate(route = ReviewManageRoute, navOptions)

fun NavController.navigateToAppealDetail(canEdit: Boolean, navOptions: NavOptionsBuilder.() -> Unit = {}) =
    navigate(route = AppealDetailRoute(canEdit = canEdit), navOptions)

fun NavController.navigateToArchiveDetail(archiveListData: ArchiveListData, navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(route = ArchiveDetailRoute(dataJsonStr = Gson().toJson(archiveListData))) {
        navOptions()
    }
}

fun NavGraphBuilder.reviewManageScreen(navController: NavController, routeToMsgScreen: () -> Unit = {}) {
    composable<ReviewManageRoute> {
        ReviewManageRoute(navController = navController, routeToMsgScreen = routeToMsgScreen)
    }
    composable<AppealDetailRoute> {
        val data = it.toRoute<AppealDetailRoute>()
        AppealDetailRoute(canEdit = data.canEdit, navController = navController)
    }
    composable<ArchiveDetailRoute> {
        val data = it.toRoute<ArchiveDetailRoute>()
        ArchiveDetailRoute(dataJsonStr = data.dataJsonStr, navController = navController)
    }
}
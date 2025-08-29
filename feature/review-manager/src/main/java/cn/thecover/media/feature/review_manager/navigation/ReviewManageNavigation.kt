package cn.thecover.media.feature.review_manager.navigation

import android.R.attr.data
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
import cn.thecover.media.feature.review_manager.appeal.AppealListData
import kotlinx.serialization.Serializable
import java.util.Map.entry


/**
 *
 * <p> Created by CharlesLee on 2025/7/28
 * 15708478830@163.com
 */

@Serializable object ReviewManageRoute
@Serializable data class AppealDetailRoute(val canEdit: Boolean)
@Serializable data class ArchiveDetailRoute(val link: String)


fun NavController.navigateToReviewManage(navOptions: NavOptions? = null) =
    navigate(route = ReviewManageRoute, navOptions)

fun NavController.navigateToAppealDetail(canEdit: Boolean, navOptions: NavOptionsBuilder.() -> Unit = {}) =
    navigate(route = AppealDetailRoute(canEdit = canEdit), navOptions)

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
        val data = it.toRoute<AppealDetailRoute>()
        AppealDetailRoute(canEdit = data.canEdit, navController = navController)
    }
    composable<ArchiveListData> {
        val data = it.toRoute<ArchiveListData>()
        ArchiveDetailRoute(data = data, navController = navController)
    }
}
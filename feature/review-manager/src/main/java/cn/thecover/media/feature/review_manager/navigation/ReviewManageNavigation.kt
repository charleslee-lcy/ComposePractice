package cn.thecover.media.feature.review_manager.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import cn.thecover.media.core.data.ArchiveListData
import cn.thecover.media.feature.review_manager.ArchiveDetailForDataRoute
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
@Serializable data class AppealDetailRoute(val appealId: Long, val canEdit: Boolean)
@Serializable data class ArchiveDetailRoute(val url: String)
@Serializable data class ArchiveDetailRoute1(val htmlData: String, val imgList: List<String>)


fun NavController.navigateToReviewManage(navOptions: NavOptions? = null) =
    navigate(route = ReviewManageRoute, navOptions)

fun NavController.navigateToAppealDetail(appealId: Long, canEdit: Boolean, navOptions: NavOptionsBuilder.() -> Unit = {}) =
    navigate(route = AppealDetailRoute(appealId = appealId, canEdit = canEdit), navOptions)

fun NavController.navigateToArchiveDetail(url: String, navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(route = ArchiveDetailRoute(url = url)) {
        navOptions()
    }
}

fun NavController.navigateToArchiveDetail(htmlData: String, imgList: List<String>, navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(route = ArchiveDetailRoute1(htmlData = htmlData, imgList = imgList)) {
        navOptions()
    }
}

fun NavGraphBuilder.reviewManageScreen(navController: NavController, routeToMsgScreen: () -> Unit = {}) {
    composable<ReviewManageRoute> {
        ReviewManageRoute(navController = navController, routeToMsgScreen = routeToMsgScreen)
    }
    composable<AppealDetailRoute> {
        val data = it.toRoute<AppealDetailRoute>()
        AppealDetailRoute(appealId = data.appealId, canEdit = data.canEdit, navController = navController)
    }
    composable<ArchiveDetailRoute> {
        val data = it.toRoute<ArchiveDetailRoute>()
        ArchiveDetailRoute(url = data.url, navController = navController)
    }
    composable<ArchiveDetailRoute1> {
        val data = it.toRoute<ArchiveDetailRoute1>()
        ArchiveDetailForDataRoute(htmlData = data.htmlData, imgList = data.imgList, navController = navController)
    }
}
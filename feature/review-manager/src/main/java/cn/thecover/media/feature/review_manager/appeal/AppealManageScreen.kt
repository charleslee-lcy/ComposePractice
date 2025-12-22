package cn.thecover.media.feature.review_manager.appeal

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import cn.thecover.media.core.data.UserInfo
import cn.thecover.media.core.network.BaseUiState
import cn.thecover.media.core.network.HttpStatus
import cn.thecover.media.core.network.previewRetrofit
import cn.thecover.media.core.widget.component.YBTab
import cn.thecover.media.core.widget.component.YBTabRow
import cn.thecover.media.core.widget.datastore.Keys
import cn.thecover.media.core.widget.datastore.rememberDataStoreState
import cn.thecover.media.core.widget.theme.OutlineColor
import cn.thecover.media.core.widget.theme.TertiaryTextColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.feature.review_manager.ReviewManageViewModel
import cn.thecover.media.feature.review_manager.navigation.navigateToArchiveDetail
import com.google.gson.Gson
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch


/**
 * 申诉管理
 * <p> Created by CharlesLee on 2025/7/28
 * 15708478830@163.com
 */
@Composable
internal fun AppealManageScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ReviewManageViewModel = hiltViewModel()
) {
    val tabs = listOf(
        FilterType(0, "我的申诉"),
        FilterType(1, "申诉审批")
    )
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { tabs.size })
    val currentTabIndex = remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
    val userInfoJson = rememberDataStoreState(Keys.USER_INFO, "")

    LaunchedEffect(userInfoJson) {
        val userInfo = Gson().fromJson(userInfoJson, UserInfo::class.java)
        userInfo?.apply {
            if (hasAuditAppealAuth) {
                if (!viewModel.isFromAppealDetail) {
                    currentTabIndex.intValue = 1
                    scope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                } else {
                    viewModel.isFromAppealDetail = false
                }
            }
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        try {
            val currentPage = pagerState.currentPage
            currentTabIndex.intValue = currentPage
            when (currentTabIndex.intValue) {
                0 -> {
                    focusManager.clearFocus()
                    viewModel.myAppealSearchKeyword.value = ""
                    viewModel.getMyAppealList(isRefresh = true)
                }
                1 -> {
                    focusManager.clearFocus()
                    viewModel.appealManageSearchKeyword.value = ""
                    viewModel.getAppealTabInfo()
                    viewModel.getAppealManageList(isRefresh = true)
                }
            }
        } catch (e: CancellationException) {
            e.printStackTrace()
        }
    }

    val appealNewsInfo by viewModel.appealNewsUiState.collectAsStateWithLifecycle()
    LaunchedEffect(appealNewsInfo) {
        when(appealNewsInfo.status) {
            HttpStatus.SUCCESS -> {
                if (appealNewsInfo.data?.status == 4) {
                    navController.navigateToArchiveDetail(appealNewsInfo.data?.wapUrl ?: "")
                } else {
                    navController.navigateToArchiveDetail(htmlData = appealNewsInfo.data?.content ?: "", imgList = listOf(
                        appealNewsInfo.data?.img43 ?: "",
                        appealNewsInfo.data?.img169 ?: ""
                    ))
                }

                viewModel.appealNewsUiState.value = BaseUiState()
            }
            HttpStatus.ERROR -> {
                Toast.makeText(context, appealNewsInfo.errorMsg, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        YBTabRow(
            selectedTabIndex = currentTabIndex.intValue,
            modifier = Modifier.background(Color.White).padding(horizontal = 30.dp),
            indicatorWidth = 64.dp,
            indicatorBottomMargin = 0.dp
        ) {
            tabs.forEachIndexed { index, tab ->
                YBTab(
                    modifier = Modifier.height(44.dp),
                    selected = index == currentTabIndex.intValue,
                    normalTextColor = TertiaryTextColor,
                    onClick = {
                        if (currentTabIndex.intValue == index) {
                            when (currentTabIndex.intValue) {
                                0 -> {
                                    focusManager.clearFocus()
                                    viewModel.getMyAppealList(isRefresh = true)
                                }
                                1 -> {
                                    focusManager.clearFocus()
                                    viewModel.getAppealTabInfo()
                                    viewModel.getAppealManageList(isRefresh = true)
                                }
                            }
                        }
                        currentTabIndex.intValue = index
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = { Text(text = tab.desc, fontSize = 16.sp) }
                )
            }
        }

        HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = OutlineColor, thickness = 0.25.dp)

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) {pageIndex ->
            when (pageIndex) {
                0 -> MyAppealContent(viewModel = viewModel, navController = navController)
                1 -> AppealManageTabContent(viewModel = viewModel, navController = navController)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun AppealManagePreview() {
    YBTheme {
        AppealManageScreen(
            viewModel = ReviewManageViewModel(
                savedStateHandle = SavedStateHandle(),
                retrofit = { previewRetrofit }
            ),
            navController = NavController(LocalContext.current)
        )
    }
}
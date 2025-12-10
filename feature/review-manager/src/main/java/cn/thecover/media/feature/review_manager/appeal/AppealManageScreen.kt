package cn.thecover.media.feature.review_manager.appeal

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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import cn.thecover.media.core.network.previewRetrofit
import cn.thecover.media.core.widget.component.YBTab
import cn.thecover.media.core.widget.component.YBTabRow
import cn.thecover.media.core.widget.theme.OutlineColor
import cn.thecover.media.core.widget.theme.TertiaryTextColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.feature.review_manager.ReviewManageViewModel
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
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { tabs.size })
    val currentTabIndex = remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        try {
            val currentPage = pagerState.currentPage
            currentTabIndex.intValue = currentPage
        } catch (e: CancellationException) {
            e.printStackTrace()
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
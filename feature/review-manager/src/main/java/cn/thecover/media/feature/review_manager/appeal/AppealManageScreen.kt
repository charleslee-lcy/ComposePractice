package cn.thecover.media.feature.review_manager.appeal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.navigation.NavController
import cn.thecover.media.core.widget.component.YBTab
import cn.thecover.media.core.widget.component.YBTabRow
import cn.thecover.media.core.widget.theme.TertiaryTextColor
import cn.thecover.media.core.widget.theme.YBTheme
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
    navController: NavController
) {
    val count = remember { mutableIntStateOf(10) }
    val tabs = listOf(
        FilterType("我的申诉", 0),
        FilterType("申诉审批${if (count.intValue > 0) "(${count.intValue})" else ""}", 1)
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
                    text = { Text(text = tab.title, fontSize = 16.sp) }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) {pageIndex ->
            when (pageIndex) {
                0 -> MyAppealContent()
                1 -> MyAppealContent()
            }
        }
    }
}

private data class FilterType(val title: String, val type: Int)


@Preview(showSystemUi = true)
@Composable
private fun AppealManagePreview() {
    YBTheme {
        AppealManageScreen(navController = NavController(LocalContext.current))
    }
}
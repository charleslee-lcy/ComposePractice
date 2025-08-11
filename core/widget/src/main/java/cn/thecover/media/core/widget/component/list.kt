package cn.thecover.media.core.widget.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.thecover.media.core.widget.theme.MainColor
import cn.thecover.media.core.widget.theme.TertiaryTextColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.core.widget.ui.PhonePreview
import kotlinx.coroutines.flow.collectLatest


/**
 *
 * <p> Created by CharlesLee on 2025/8/5
 * 15708478830@163.com
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> YBNormalList(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    items: MutableState<List<T>>,
    header: @Composable () -> Unit = {},
    onRefresh: () -> Unit = {},
    onLoadMore: () -> Unit = {},
    isRefreshing: MutableState<Boolean> = mutableStateOf(false),
    isLoadingMore: MutableState<Boolean>  = mutableStateOf(false),
    canLoadMore: MutableState<Boolean> = mutableStateOf(false),
    itemContent: @Composable (item: T, position: Int) -> Unit
) {
    val listState = rememberLazyListState()
    val state = rememberPullToRefreshState()

    // 上拉加载：监听滚动到底部
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collectLatest { lastVisible ->
                if (lastVisible == items.value.lastIndex && !isLoadingMore.value && canLoadMore.value) {
                    isLoadingMore.value = true
                    onLoadMore.invoke()
                }
            }
    }

    PullToRefreshBox(
        state = state,
        isRefreshing = isRefreshing.value,
        onRefresh = {
            isRefreshing.value = true
            onRefresh.invoke()
        },
        modifier = modifier,
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = isRefreshing.value,
                containerColor = Color.White,
                color = MainColor,
                state = state
            )
        }
    ) {
        LazyColumn(state = listState, modifier = Modifier.fillMaxSize(),verticalArrangement=verticalArrangement) {
            stickyHeader {
                header()
            }

            itemsIndexed(items.value, key = { index, _ -> index }) { index, item ->
                itemContent.invoke(item, index)
            }

            when {
                // 上拉加载footer
                isLoadingMore.value -> item {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
                        Spacer(Modifier.width(8.dp))
                        Text(text = "加载中…", fontSize = 12.sp, color = TertiaryTextColor)
                    }
                }

                !canLoadMore.value -> item {
                    Text(
                        "没有更多数据了",
                        Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        textAlign = TextAlign.Center,
                        color = TertiaryTextColor,
                        fontSize = 12.sp
                    )
                }
            }
        }

        if (items.value.isEmpty()) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "暂无数据",
                    Modifier.padding(12.dp),
                    textAlign = TextAlign.Center,
                    color = TertiaryTextColor,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@PhonePreview
@Composable
private fun YBNormalListPreview() {
    YBTheme {
//        YBNormalList()
    }
}
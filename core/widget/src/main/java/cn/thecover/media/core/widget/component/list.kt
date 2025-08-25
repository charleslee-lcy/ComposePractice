package cn.thecover.media.core.widget.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.thecover.media.core.widget.R
import cn.thecover.media.core.widget.component.coordinator.CoordinatorLayout
import cn.thecover.media.core.widget.component.coordinator.CoordinatorState
import cn.thecover.media.core.widget.component.coordinator.rememberCoordinatorState
import cn.thecover.media.core.widget.theme.MainColor
import cn.thecover.media.core.widget.theme.TertiaryTextColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.core.widget.ui.PhonePreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine


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
    listState: LazyListState = rememberLazyListState(),
    header: @Composable () -> Unit = {},
    onRefresh: () -> Unit = {},
    onLoadMore: () -> Unit = {},
    isRefreshing: MutableState<Boolean> = mutableStateOf(false),
    isLoadingMore: MutableState<Boolean> = mutableStateOf(false),
    canLoadMore: MutableState<Boolean> = mutableStateOf(false),
    itemContent: @Composable (item: T, position: Int) -> Unit
) {
    val refreshState = rememberPullToRefreshState()

    // 上拉加载：监听滚动到底部
    LaunchedEffect(listState) {
        combine(
            snapshotFlow {
                listState.layoutInfo.visibleItemsInfo.firstOrNull()?.index
            },
            snapshotFlow {
                listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
            },
            snapshotFlow {
                listState.canScrollForward
            },
            snapshotFlow {
                listState.canScrollBackward
            }
        ) { firstVisible, lastVisible, canScrollForward, canScrollBackward ->
            var shouldLoadMore = false
            if (canScrollBackward && lastVisible == items.value.lastIndex && !isLoadingMore.value && canLoadMore.value) {
                shouldLoadMore = true
            }
            shouldLoadMore
        }.collectLatest { shouldLoadMore ->
            if (shouldLoadMore) {
                isLoadingMore.value = true
                onLoadMore.invoke()
            }
        }
    }

    PullToRefreshBox(
        state = refreshState,
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
                state = refreshState
            )
        }
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = verticalArrangement
        ) {
            stickyHeader {
                Box(modifier = Modifier.fillMaxWidth()) {
                    header.invoke()
                }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> YBCoordinatorList(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    items: MutableState<List<T>>,
    listState: LazyListState = rememberLazyListState(),
    coordinatorState: CoordinatorState = rememberCoordinatorState(),
    collapsableContent: @Composable ColumnScope.() -> Unit = {},
    enableCollapsable: Boolean = true,
    enableScrim: Boolean = false,
    nonCollapsableHeight: Int = 0,
    onRefresh: () -> Unit = {},
    onLoadMore: () -> Unit = {},
    isRefreshing: MutableState<Boolean> = mutableStateOf(false),
    isLoadingMore: MutableState<Boolean> = mutableStateOf(false),
    canLoadMore: MutableState<Boolean> = mutableStateOf(false),
    itemContent: @Composable (item: T, position: Int) -> Unit
) {
    val refreshState = rememberPullToRefreshState()

    // 上拉加载：监听滚动到底部
    LaunchedEffect(listState) {
        combine(
            snapshotFlow {
                listState.layoutInfo.visibleItemsInfo.firstOrNull()?.index
            },
            snapshotFlow {
                listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
            },
            snapshotFlow {
                listState.canScrollForward
            },
            snapshotFlow {
                listState.canScrollBackward
            }
        ) { firstVisible, lastVisible, canScrollForward, canScrollBackward ->
            var shouldLoadMore = false
            if (canScrollBackward && lastVisible == items.value.lastIndex && !isLoadingMore.value && canLoadMore.value) {
                shouldLoadMore = true
            }
            shouldLoadMore
        }.collectLatest { shouldLoadMore ->
            if (shouldLoadMore) {
                isLoadingMore.value = true
                onLoadMore.invoke()
            }
        }
    }

    PullToRefreshBox(
        modifier = modifier,
        state = refreshState,
        isRefreshing = isRefreshing.value,
        onRefresh = {
            isRefreshing.value = true
            onRefresh.invoke()
        },
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = isRefreshing.value,
                containerColor = Color.White,
                color = MainColor,
                state = refreshState
            )
        }
    ) {
        CoordinatorLayout(
            nestedScrollableState = { listState },
            state = coordinatorState,
            modifier = Modifier.fillMaxSize(),
            enableCollapsable = enableCollapsable,
            nonCollapsableHeight = nonCollapsableHeight,
            collapsableContent = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                ) {
                    Column {
                        this.collapsableContent()
                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                this.alpha =
                                    if (enableScrim && enableCollapsable) coordinatorState.collapsedHeight / coordinatorState.maxCollapsableHeight else 0f
                            }
                            .background(Color.White))
                }
            },
        ) {
            Box {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = verticalArrangement
                ) {
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

                if (items.value.isEmpty() && !isRefreshing.value) {
                    Box(
                        Modifier.fillMaxWidth().height(with(LocalDensity.current) { coordinatorState.emptyHeight.toDp() }),
                        contentAlignment = Alignment.Center
                    ) {
                        YBImage(
                            modifier = Modifier.padding(12.dp),
                            placeholder = painterResource(R.mipmap.img_empty_content)
                        )
                    }
                }
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
package cn.thecover.media.feature.review_manager.appeal

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import cn.thecover.media.core.data.AppealListData
import cn.thecover.media.core.network.previewRetrofit
import cn.thecover.media.core.widget.R
import cn.thecover.media.core.widget.component.YBImage
import cn.thecover.media.core.widget.component.YBInput
import cn.thecover.media.core.widget.component.YBNormalList
import cn.thecover.media.core.widget.component.popup.YBAlignDropdownMenu
import cn.thecover.media.core.widget.event.clickableWithoutRipple
import cn.thecover.media.core.widget.theme.EditHintTextColor
import cn.thecover.media.core.widget.theme.MainColor
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.OutlineColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.core.widget.ui.PhonePreview
import cn.thecover.media.feature.review_manager.ReviewManageViewModel
import cn.thecover.media.feature.review_manager.navigation.navigateToAppealDetail


/**
 * 申诉审批Tab页卡
 * <p> Created by CharlesLee on 2025/8/4
 * 15708478830@163.com
 */
@Composable
fun AppealManageTabContent(viewModel: ReviewManageViewModel, navController: NavController) {
    val filters = listOf(
        FilterType(type = 0, desc = "稿件名称"),
        FilterType(type = 1, desc = "人员姓名"),
        FilterType(type = 2, desc = "申诉理由")
    )

    val listState = rememberLazyListState()
    val items = remember { mutableStateOf(listOf<AppealListData>()) }
    val isRefreshing = remember { mutableStateOf(false) }
    val isLoadingMore = remember { mutableStateOf(false) }
    val canLoadMore = remember { mutableStateOf(true) }
    val focusManager = LocalFocusManager.current
    val appealManageListUiState by viewModel.appealManageListDataState.collectAsStateWithLifecycle()
    val tabInfoUiState by viewModel.tabInfoState.collectAsStateWithLifecycle()

    var dealingCount by remember { mutableIntStateOf(0) }
    var passCount by remember { mutableIntStateOf(0) }
    var rejectCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.getAppealTabInfo()
        viewModel.getAppealManageList(isRefresh = true)
    }

    LaunchedEffect(appealManageListUiState) {
        isRefreshing.value = appealManageListUiState.isRefreshing
        isLoadingMore.value = appealManageListUiState.isLoading
        canLoadMore.value = appealManageListUiState.canLoadMore
        items.value = appealManageListUiState.list
    }

    LaunchedEffect(tabInfoUiState) {
        tabInfoUiState.forEachIndexed { index, value ->
            when (index) {
                0 -> dealingCount = value
                1 -> passCount = value
                2 -> rejectCount = value
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp)
    ) {
        FilterSearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .border(0.5.dp, Color(0xFFEAEAEB), RoundedCornerShape(4.dp))
                .background(Color.White)
                .height(36.dp),
            initialIndex = viewModel.appealManageSearchType.intValue,
            initialSearchText = viewModel.appealManageSearchKeyword.value,
            filterData = filters,
            filterClick = { _, index ->
                viewModel.appealManageSearchType.intValue = index
            }
        ) {
            viewModel.appealManageSearchKeyword.value = it
            focusManager.clearFocus()
            viewModel.getAppealTabInfo()
            viewModel.getAppealManageList(isRefresh = true)
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .height(36.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            shape = RoundedCornerShape(4.dp),
            border = BorderStroke(0.6.dp, MainColor)
        ) {
            Row(modifier = Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .background(color = if (viewModel.currentPos == 0) MainColor else Color.White)
                        .clickableWithoutRipple {
                            viewModel.currentPos = 0
                            viewModel.getAppealManageList(isRefresh = true)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "待审批（${dealingCount}）",
                        style = TextStyle(
                            fontSize = 13.sp,
                            color = if (viewModel.currentPos == 0) Color.White else MainColor,
                            textAlign = TextAlign.Center
                        )
                    )
                }
                VerticalDivider(
                    modifier = Modifier.fillMaxHeight(),
                    color = MainColor,
                    thickness = 0.6.dp
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .background(color = if (viewModel.currentPos == 1) MainColor else Color.White)
                        .clickableWithoutRipple {
                            viewModel.currentPos = 1
                            viewModel.getAppealManageList(isRefresh = true)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "通过（${passCount}）",
                        style = TextStyle(
                            fontSize = 13.sp,
                            color = if (viewModel.currentPos == 1) Color.White else MainColor,
                            textAlign = TextAlign.Center
                        )
                    )
                }
                VerticalDivider(
                    modifier = Modifier.fillMaxHeight(),
                    color = MainColor,
                    thickness = 0.6.dp
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .background(color = if (viewModel.currentPos == 2) MainColor else Color.White)
                        .clickableWithoutRipple {
                            viewModel.currentPos = 2
                            viewModel.getAppealManageList(isRefresh = true)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "驳回（${rejectCount}）",
                        style = TextStyle(
                            fontSize = 13.sp,
                            color = if (viewModel.currentPos == 2) Color.White else MainColor,
                            textAlign = TextAlign.Center
                        )
                    )
                }

            }
        }

        YBNormalList(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            items = items,
            listState = listState,
            isRefreshing = isRefreshing,
            isLoadingMore = isLoadingMore,
            canLoadMore = canLoadMore,
            onRefresh = {
                viewModel.getAppealManageList(isRefresh = true)
            },
            onLoadMore = {
                viewModel.getAppealManageList(isRefresh = false)
            }) { item, _ ->
            AppealListItem(
                viewModel = viewModel,
                modifier = Modifier.fillMaxWidth(),
                item = item
            ) {
                // 跳转到申诉详情页
                navController.navigateToAppealDetail(item.id, viewModel.currentPos == 0)
            }
        }
    }
}

data class FilterType(val type: Int, val desc: String)

@Composable
fun FilterSearchBar(
    modifier: Modifier = Modifier,
    initialIndex: Int = 0,
    initialSearchText: String = "",
    filterData: List<FilterType>,
    filterClick: (String, Int) -> Unit = { _, _ -> },
    onSearch: (String) -> Unit = {}
) {
    val expanded = remember { mutableStateOf(false) }
    val animRotate = remember { Animatable(0f) }
    var title by remember { mutableStateOf(filterData[initialIndex].desc) }
    var searchText by remember { mutableStateOf(initialSearchText) }

    // 当菜单状态改变时触发动画
    LaunchedEffect(expanded.value) {
        animRotate.animateTo(
            targetValue = if (expanded.value) 180f else 0f,
            animationSpec = tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing
            )
        )
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        YBAlignDropdownMenu(
            data = filterData.map { it.desc },
            expanded = expanded,
            initialIndex = initialIndex,
            isItemWidthAlign = true,
            modifier = Modifier.weight(0.28f),
            offset = DpOffset(0.dp, 0.dp),
            onItemClick = { text, index ->
                title = text
                filterClick.invoke(text, index)
            }) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterHorizontally)
                    .clickableWithoutRipple {
                        expanded.value = !expanded.value
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(start = 12.dp),
                    text = title,
                    color = MainTextColor,
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(Modifier.weight(1f))
                YBImage(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(18.dp)
                        .rotate(animRotate.value),
                    placeholder = painterResource(R.mipmap.ic_arrow_down_light_grey)
                )
            }
        }
        VerticalDivider(modifier = Modifier.height(20.dp), thickness = 0.5.dp, color = OutlineColor)
        YBInput(
            text = searchText,
            modifier = Modifier
                .weight(0.7f)
                .padding(horizontal = 15.dp),
            textStyle = TextStyle(
                fontSize = 13.sp, color = MainTextColor
            ),
            hint = "请输入搜索内容",
            hintTextSize = 13.sp,
            hintTextColor = EditHintTextColor,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch.invoke(searchText)
                }
            ),
            onValueChange = {
                searchText = it
            })
    }
}

@PhonePreview
@Composable
private fun AppealTabContentPreview() {
    YBTheme {
        AppealManageTabContent(
            viewModel = ReviewManageViewModel(
                savedStateHandle = SavedStateHandle(),
                retrofit = { previewRetrofit }
            ),
            navController = NavController(LocalContext.current)
        )
    }
}
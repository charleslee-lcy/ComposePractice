package cn.thecover.media.feature.review_manager

import android.R.attr.label
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.navOptions
import cn.thecover.media.core.widget.GradientLeftBottom
import cn.thecover.media.core.widget.GradientLeftTop
import cn.thecover.media.core.widget.component.YBImage
import cn.thecover.media.core.widget.component.YBNormalList
import cn.thecover.media.core.widget.component.picker.DateType
import cn.thecover.media.core.widget.component.picker.SingleColumnPicker
import cn.thecover.media.core.widget.component.picker.YBDatePicker
import cn.thecover.media.core.widget.component.popup.YBDialog
import cn.thecover.media.core.widget.component.popup.YBLoadingDialog
import cn.thecover.media.core.widget.component.popup.YBPopup
import cn.thecover.media.core.widget.event.clickableWithoutRipple
import cn.thecover.media.core.widget.gradientShape
import cn.thecover.media.core.widget.state.rememberTipsDialogState
import cn.thecover.media.core.widget.theme.EditHintTextColor
import cn.thecover.media.core.widget.theme.MainColor
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.OutlineColor
import cn.thecover.media.core.widget.theme.PageBackgroundColor
import cn.thecover.media.core.widget.theme.SecondaryTextColor
import cn.thecover.media.core.widget.theme.TertiaryTextColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.core.widget.ui.PhonePreview
import cn.thecover.media.feature.review_manager.appeal.FilterSearchBar
import cn.thecover.media.feature.review_manager.appeal.FilterType
import cn.thecover.media.feature.review_manager.assign.FilterDropMenuView
import cn.thecover.media.feature.review_manager.navigation.navigateToArchiveDetail
import kotlinx.coroutines.launch


/**
 *
 * <p> Created by CharlesLee on 2025/8/8
 * 15708478830@163.com
 */
@Composable
fun ArchiveScoreScreen(
    navController: NavController,
    viewModel: ReviewManageViewModel = hiltViewModel()
) {

    var curPage = remember { mutableIntStateOf(0) }
    var isRefreshing = remember { mutableStateOf(false) }
    var isLoadingMore = remember { mutableStateOf(false) }
    var canLoadMore = remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val loadingState = rememberTipsDialogState()
    var items = remember { mutableStateOf(listOf<ArchiveListData>()) }

    LaunchedEffect(Unit) {
        curPage.intValue = 0
        viewModel.getArchiveList(curPage.intValue).collect {
            when(it) {
                is ArchiveListUiState.Loading -> {
//                    loadingState.show()
                    isRefreshing.value = true
                }
                is ArchiveListUiState.Success -> {
                    loadingState.hide()
                    items.value = it.list
                    isRefreshing.value = false
                    canLoadMore.value = true
                }
                else -> {}
            }
        }
    }

    ArchiveScoreScreen(
        navController = navController,
        items = items,
        isRefreshing = isRefreshing,
        isLoadingMore = isLoadingMore,
        canLoadMore = canLoadMore,
        onRefresh = {
            scope.launch {
                curPage.intValue = 0
                viewModel.getArchiveList(curPage.intValue).collect {
                    if (it is ArchiveListUiState.Success) {
                        items.value = it.list
                        isRefreshing.value = false
                        canLoadMore.value = true
                    }
                }
            }
        },
        onLoadMore = {
            scope.launch {
                curPage.intValue++
                viewModel.getArchiveList(curPage.intValue).collect { state ->
                    if (state is ArchiveListUiState.Success) {
                        items.value = items.value + state.list
                        isLoadingMore.value = false
                        // 最后一页
                        if (state.list.size < 10) canLoadMore.value = false
                    }
                }
            }
        }
    )

    YBLoadingDialog(loadingState, enableDismiss = true, onDismissRequest = { loadingState.hide() })
}

@Composable
fun ArchiveScoreScreen(
    navController: NavController,
    items: MutableState<List<ArchiveListData>>,
    isRefreshing: MutableState<Boolean>,
    isLoadingMore: MutableState<Boolean>,
    canLoadMore: MutableState<Boolean>,
    onRefresh: () -> Unit = {},
    onLoadMore: () -> Unit = {},
) {
    var showScoreDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .fillMaxSize()
    ) {
        ArchiveScoreHeader()
        YBNormalList(
            modifier = Modifier.fillMaxSize(),
            items = items,
            isRefreshing = isRefreshing,
            isLoadingMore = isLoadingMore,
            canLoadMore = canLoadMore,
            onRefresh = {
                onRefresh.invoke()
            },
            onLoadMore = {
                onLoadMore.invoke()
            }) { item, index ->
            ArchiveListItem(
                modifier = Modifier.fillMaxWidth(),
                item = item,
                onDetailClick = {
                    navController.navigateToArchiveDetail(item)
                },
                onScoreClick = {
                    showScoreDialog.value = true
                }
            )
        }
    }

    YBPopup(
        visible = showScoreDialog.value,
        isShowTopActionBar = true,
        draggable = false,
        onClose = { showScoreDialog.value = false }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(15.dp))
            ScoreInfoContent(
                title = "值班副总编辑",
                enable = false,
                scoredList = listOf(
                    "A-"
                )
            )
            HorizontalDivider(
                modifier = Modifier
                    .padding(top = 15.dp)
                    .fillMaxWidth(),
                thickness = 0.5.dp,
                color = OutlineColor
            )
            ScoreInfoContent(
                title = "值班编委",
                enable = true,
                scoredList = listOf(
                    "A-", "B+", "B", "C", "D"
                )
            )
            HorizontalDivider(
                modifier = Modifier
                    .padding(top = 15.dp)
                    .fillMaxWidth(),
                thickness = 0.5.dp,
                color = OutlineColor
            )
            ScoreInfoContent(
                title = "专家",
                enable = false
            )
            Spacer(modifier = Modifier.height(15.dp))
        }

    }
}

@Composable
private fun ScoreInfoContent(
    title: String,
    enable: Boolean = false,
    initialIndex: Int = 0,
    scoredList: List<String> = listOf()
) {
    val scoreStateFilters = listOf(
        FilterType(type = 1, desc = "A+"),
        FilterType(type = 2, desc = "A"),
        FilterType(type = 3, desc = "A-"),
        FilterType(type = 4, desc = "B+"),
        FilterType(type = 5, desc = "B"),
        FilterType(type = 6, desc = "B-"),
        FilterType(type = 7, desc = "C+"),
        FilterType(type = 8, desc = "C"),
        FilterType(type = 9, desc = "C-"),
        FilterType(type = 10, desc = "D")
    )
    var showScoreFilter = remember { mutableStateOf(false) }
    var currentIndex by remember { mutableIntStateOf(initialIndex) }

    Row(
        modifier = Modifier
            .padding(top = 15.dp)
            .fillMaxWidth()
            .height(36.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            style = scoreTitleStyle(enable)
        )
        Box(
            modifier = Modifier.weight(3f)
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .size(81.dp, 36.dp)
                    .background(
                        color = if (enable) PageBackgroundColor else OutlineColor,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .clickableWithoutRipple {
                        if (enable) {
                            showScoreFilter.value = true
                        }
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = if (enable) scoreStateFilters[currentIndex].desc else scoreStateFilters[initialIndex].desc,
                    textAlign = TextAlign.Center,
                    style = scoreTitleStyle(enable)
                )
                Icon(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(18.dp),
                    painter = painterResource(cn.thecover.media.core.widget.R.mipmap.ic_arrow_down),
                    contentDescription = "",
                    tint = TertiaryTextColor
                )
            }
        }
    }
    Row(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
            .height(36.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = "已打分记录",
            fontSize = 14.sp,
            color = MainTextColor,
            textAlign = TextAlign.End,
            fontWeight = FontWeight.SemiBold
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            modifier = Modifier
                .padding(start = 10.dp)
                .weight(3f),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (scoredList.isEmpty()) {
                item {
                    Text(
                        text = "无",
                        color = TertiaryTextColor,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.background(
                            color = OutlineColor,
                            shape = RoundedCornerShape(4.dp)
                        )
                    )
                }
            } else {
                scoredList.forEach {
                    item {
                        Text(
                            text = it,
                            color = MainColor,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.background(
                                color = Color(0xFFEAF0FF),
                                shape = RoundedCornerShape(4.dp)
                            )
                        )
                    }
                }
            }
        }
    }

    SingleColumnPicker(
        visible = showScoreFilter.value,
        range = scoreStateFilters.map { it.desc },
        value = currentIndex,
        onChange = {
            currentIndex = it
        },
        onCancel = {
            showScoreFilter.value = false
        }
    )
}

private fun scoreTitleStyle(enable: Boolean) = TextStyle(
    fontSize = 14.sp,
    color = if (enable) MainTextColor else TertiaryTextColor,
    fontWeight = FontWeight.SemiBold,
    textAlign = TextAlign.End
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArchiveScoreHeader() {
    val showScoreDialog = remember { mutableStateOf(false) }

    var isStartDatePickerShow by remember { mutableStateOf(true) }
    var datePickerShow by remember { mutableStateOf(false) }
    val startDate = remember { mutableStateOf("开始时间") }
    val endDate = remember { mutableStateOf("结束时间") }

    val scoreStateFilters = listOf(
        FilterType(type = 1, desc = "全部"),
        FilterType(type = 2, desc = "待打分"),
        FilterType(type = 3, desc = "已打分")
    )

    val searchFilters = listOf(
        FilterType(type = 1, desc = "稿件标题"),
        FilterType(type = 2, desc = "稿件id"),
        FilterType(type = 3, desc = "稿件人员")
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .gradientShape(
                    colors = listOf(Color(0xFFEBF1FF), Color.White),
                    start = GradientLeftTop,
                    end = GradientLeftBottom
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            YBImage(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 14.dp),
                placeholder = painterResource(R.mipmap.img_archive_score_label),
                contentScale = ContentScale.Fit
            )
            VerticalDivider(
                modifier = Modifier.height(36.dp), thickness = 0.5.dp, color = OutlineColor
            )
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "等级",
                    color = SecondaryTextColor,
                    fontSize = 12.sp
                )
                Text(
                    text = "A",
                    color = MainTextColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            VerticalDivider(
                modifier = Modifier.height(36.dp), thickness = 0.5.dp, color = OutlineColor
            )
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "当月数量",
                    color = SecondaryTextColor,
                    fontSize = 12.sp
                )
                Text(
                    text = "223",
                    color = MainTextColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            VerticalDivider(
                modifier = Modifier.height(36.dp), thickness = 0.5.dp, color = OutlineColor
            )
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "当天数量",
                    color = SecondaryTextColor,
                    fontSize = 12.sp
                )
                Text(
                    text = "14",
                    color = MainTextColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            VerticalDivider(
                modifier = Modifier.height(36.dp), thickness = 0.5.dp, color = OutlineColor
            )
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .clickableWithoutRipple {
                        showScoreDialog.value = true
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "更多",
                    color = TertiaryTextColor,
                    lineHeight = 14.sp,
                    fontSize = 14.sp,
                )
                Icon(
                    painterResource(cn.thecover.media.core.widget.R.drawable.icon_right_arrow),
                    contentDescription = "Localized description",
                    Modifier
                        .size(18.dp)
                        .padding(2.dp),
                    tint = TertiaryTextColor
                )
            }
        }
    }

    Card(
        modifier = Modifier
            .padding(top = 12.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Text(
            modifier = Modifier.padding(start = 12.dp, top = 12.dp),
            text = "稿件发布时间",
            color = MainTextColor,
            lineHeight = 13.sp,
            fontSize = 13.sp
        )
        Row(
            modifier = Modifier
                .padding(top = 6.dp, start = 12.dp, end = 12.dp)
                .fillMaxWidth()
                .background(PageBackgroundColor)
                .border(0.5.dp, Color(0xFFEAEAEB), RoundedCornerShape(4.dp))
                .height(36.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .fillMaxHeight()
                    .weight(1f)
                    .clickableWithoutRipple {
                        isStartDatePickerShow = true
                        datePickerShow = true
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = startDate.value,
                    style = MaterialTheme.typography.labelMedium,
                    color = EditHintTextColor
                )
                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(R.mipmap.ic_date_from_pick),
                    contentDescription = "${label}下拉筛选按钮",
                    tint = TertiaryTextColor
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Row(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .fillMaxHeight()
                    .weight(1f)
                    .clickableWithoutRipple {
                        isStartDatePickerShow = false
                        datePickerShow = true
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = endDate.value,
                    style = MaterialTheme.typography.labelMedium,
                    color = EditHintTextColor
                )
                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(R.mipmap.ic_date_to_pick),
                    contentDescription = "${label}下拉筛选按钮",
                    tint = TertiaryTextColor
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 12.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 左侧排序指数选择区域
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    modifier = Modifier,
                    text = "本人打分状态",
                    color = MainTextColor,
                    lineHeight = 13.sp,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                FilterDropMenuView(filterData = scoreStateFilters, initialIndex = 1)
            }
            Spacer(Modifier.width(12.dp))
            // 右侧时间选择区域
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    modifier = Modifier,
                    text = "稿件打分状态",
                    color = MainTextColor,
                    lineHeight = 13.sp,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                FilterDropMenuView(filterData = scoreStateFilters)
            }
        }
        FilterSearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .border(0.5.dp, Color(0xFFEAEAEB), RoundedCornerShape(4.dp))
                .background(PageBackgroundColor)
                .height(36.dp),
            initialIndex = 0,
            filterData = searchFilters
        ) { text, index ->
            Log.d("CharlesLee", "filterType: ${searchFilters[index].type}")
        }
    }

    YBDialog(
        dialogState = showScoreDialog,
        onDismissRequest = { showScoreDialog.value = false },
        title = "稿件评分情况",
        widthRate = 0.9f,
        content = {
            val dataList = listOf(
                ArchiveScoreRule(
                    grade = "A+",
                    monthlyCount = 20,
                    dailyCount = 9
                ),
                ArchiveScoreRule(
                    grade = "A",
                    monthlyCount = 20,
                    dailyCount = 9
                ),
                ArchiveScoreRule(
                    grade = "A-",
                    monthlyCount = 20,
                    dailyCount = 9
                ),
                ArchiveScoreRule(
                    grade = "B+",
                    monthlyCount = 20,
                    dailyCount = 9
                ),
                ArchiveScoreRule(
                    grade = "B",
                    monthlyCount = 20,
                    dailyCount = 9
                ),
                ArchiveScoreRule(
                    grade = "B-",
                    monthlyCount = 20,
                    dailyCount = 9
                ),
                ArchiveScoreRule(
                    grade = "C+",
                    monthlyCount = 20,
                    dailyCount = 9
                ),
                ArchiveScoreRule(
                    grade = "C",
                    monthlyCount = 20,
                    dailyCount = 9
                ),
                ArchiveScoreRule(
                    grade = "C-",
                    monthlyCount = 20,
                    dailyCount = 9
                ),
                ArchiveScoreRule(
                    grade = "D",
                    monthlyCount = 20,
                    dailyCount = 9
                )
            )
            Column {
                Text(
                    text = "提示：当前等级统计数据包含一篇稿件多个打分的等级数据，仅供打分时参考。",
                    fontSize = 14.sp,
                    lineHeight = 14.sp * 1.3f,
                    color = SecondaryTextColor
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(OutlineColor),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        text = "等级",
                        fontSize = 14.sp,
                        color = SecondaryTextColor,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        text = "当月数量",
                        fontSize = 14.sp,
                        color = SecondaryTextColor,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        text = "当天数量",
                        fontSize = 14.sp,
                        color = SecondaryTextColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                dataList.forEachIndexed { index, item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .background(if (index % 2 == 0) Color.Transparent else PageBackgroundColor),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            text = item.grade,
                            fontSize = 14.sp,
                            color = MainTextColor
                        )
                        Text(
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            text = "${item.monthlyCount}",
                            fontSize = 14.sp,
                            color = MainTextColor
                        )
                        Text(
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            text = "${item.dailyCount}",
                            fontSize = 14.sp,
                            color = MainTextColor
                        )
                    }
                }
            }

        },
        confirmText = null,
        cancelText = null
    )

    YBDatePicker(
        visible = datePickerShow,
        type = DateType.DAY,
        title = if (isStartDatePickerShow) "选择开始时间" else "选择结束时间",
        onCancel = { datePickerShow = false },
        onChange = {
            if (isStartDatePickerShow) {
                startDate.value = "${it.year}-${it.monthValue}-${it.dayOfMonth}"
            } else {
                endDate.value = "${it.year}-${it.monthValue}-${it.dayOfMonth}"
            }
        }
    )
}

private data class ArchiveScoreRule(
    val grade: String,
    val monthlyCount: Int,
    val dailyCount: Int
)

@PhonePreview
@Composable
private fun ArchiveScoreScreenPreview() {
    YBTheme {
        var isRefreshing = remember { mutableStateOf(false) }
        var isLoadingMore = remember { mutableStateOf(false) }
        var canLoadMore = remember { mutableStateOf(true) }
        var items = remember { mutableStateOf(listOf<ArchiveListData>()) }
        repeat(10) {
            items.value = items.value + ArchiveListData(title = "标题$it", niceDate = "2023-05-05")
        }
        ArchiveScoreScreen(
            navController = NavController(LocalContext.current),
            items = items,
            isRefreshing = isRefreshing,
            isLoadingMore = isLoadingMore,
            canLoadMore = canLoadMore,
        )
    }
}
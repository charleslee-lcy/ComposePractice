package cn.thecover.media.feature.review_manager

import android.R.attr.label
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
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
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import cn.thecover.media.core.common.util.toMillisecond
import cn.thecover.media.core.data.ArchiveListData
import cn.thecover.media.core.data.ScoreLevelData
import cn.thecover.media.core.data.ScoreRuleData
import cn.thecover.media.core.network.HttpStatus
import cn.thecover.media.core.network.previewRetrofit
import cn.thecover.media.core.widget.GradientLeftBottom
import cn.thecover.media.core.widget.GradientLeftTop
import cn.thecover.media.core.widget.component.YBCoordinatorList
import cn.thecover.media.core.widget.component.YBImage
import cn.thecover.media.core.widget.component.picker.DateType
import cn.thecover.media.core.widget.component.picker.SingleColumnPicker
import cn.thecover.media.core.widget.component.picker.YBDatePicker
import cn.thecover.media.core.widget.component.popup.YBDialog
import cn.thecover.media.core.widget.component.popup.YBLoadingDialog
import cn.thecover.media.core.widget.component.popup.YBPopup
import cn.thecover.media.core.widget.event.clickableWithoutRipple
import cn.thecover.media.core.widget.gradientShape
import cn.thecover.media.core.widget.state.TipsDialogState
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
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalDateTime


/**
 * 稿件打分
 * <p> Created by CharlesLee on 2025/8/8
 * 15708478830@163.com
 */
@Composable
fun ArchiveScoreScreen(
    navController: NavController,
    viewModel: ReviewManageViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isRefreshing = remember { mutableStateOf(false) }
    val isLoadingMore = remember { mutableStateOf(false) }
    val canLoadMore = remember { mutableStateOf(true) }
    val focusManager = LocalFocusManager.current
    val loadingState = rememberTipsDialogState()
    val items = remember { mutableStateOf(listOf<ArchiveListData>()) }
    val archiveListUiState by viewModel.archiveListDataState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        // 获取打分规则
        viewModel.getScoreRuleInfo()
    }

    LaunchedEffect(viewModel.startLocalDate, viewModel.endLocalDate) {
        // 获取稿件评分列表
        viewModel.getArchiveList(isRefresh = true)
    }

    LaunchedEffect(archiveListUiState) {
        isRefreshing.value = archiveListUiState.isRefreshing
        isLoadingMore.value = archiveListUiState.isLoading
        canLoadMore.value = archiveListUiState.canLoadMore
        items.value = archiveListUiState.list
    }

    ArchiveScoreScreen(
        viewModel = viewModel,
        navController = navController,
        items = items,
        isRefreshing = isRefreshing,
        isLoadingMore = isLoadingMore,
        canLoadMore = canLoadMore,
        loadingState = loadingState,
        onRefresh = {
            viewModel.getArchiveList(isRefresh = true)
        },
        onLoadMore = {
            viewModel.getArchiveList(isRefresh = false)
        },
        onSearch = {
//            if (it.isEmpty()) {
//                Toast.makeText(context, "搜索内容不能为空", Toast.LENGTH_SHORT).show()
//                return@ArchiveScoreScreen
//            }
            viewModel.getArchiveList(isRefresh = true)
            focusManager.clearFocus()
        }
    )

    YBLoadingDialog(loadingState, enableDismiss = true, onDismissRequest = { loadingState.hide() })
}

@Composable
fun ArchiveScoreScreen(
    viewModel: ReviewManageViewModel,
    navController: NavController,
    items: MutableState<List<ArchiveListData>>,
    isRefreshing: MutableState<Boolean>,
    isLoadingMore: MutableState<Boolean>,
    canLoadMore: MutableState<Boolean>,
    loadingState: TipsDialogState,
    onRefresh: () -> Unit = {},
    onLoadMore: () -> Unit = {},
    onSearch: (String) -> Unit = {},
) {
    val context = LocalContext.current
    val showScoreDialog = remember { mutableStateOf(false) }
    var checkedItem by remember { mutableStateOf<ArchiveListData?>(null) }
    val updateScoreStatus by viewModel.updateScoreState.collectAsStateWithLifecycle()

    LaunchedEffect(updateScoreStatus) {
        when (updateScoreStatus.status) {
            HttpStatus.LOADING -> {
                loadingState.show()
            }
            HttpStatus.SUCCESS -> {
                loadingState.hide()
                showScoreDialog.value = false
                checkedItem = null
            }
            HttpStatus.ERROR -> {
                loadingState.hide()
                Toast.makeText(context, updateScoreStatus.errorMsg, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    YBCoordinatorList(
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
        },
        enableCollapsable = true,
        collapsableContent = {
            ArchiveScoreHeader(viewModel, onSearch = onSearch)
        }) { item, index ->
        ArchiveListItem(
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxWidth(),
            item = item,
            onDetailClick = {
                navController.navigateToArchiveDetail(item.wapUrl)
            },
            onScoreClick = {
                checkedItem = item
                showScoreDialog.value = true
            }
        )
    }

    var scoreLevel by remember { mutableIntStateOf(0) }

    YBPopup(
        visible = showScoreDialog.value,
        isShowTopActionBar = true,
        draggable = false,
        onClose = {
            showScoreDialog.value = false
            checkedItem = null
        },
        onConfirm = {
            viewModel.updateScore(
                scoreGroupId = viewModel.scoreGroupState.value.takeIf { it.isNotEmpty() }?.first()?.id ?: 0,
                scoreLevel = scoreLevel,
                newsId = checkedItem?.id
            )
        }
    ) {
        val scoreLevelStatus by viewModel.scoreLevelState.collectAsStateWithLifecycle()
        val scoreGroupStatus by viewModel.scoreGroupState.collectAsStateWithLifecycle()

        var scoreList1 by remember { mutableStateOf(listOf<String>()) }
        var scoreList2 by remember { mutableStateOf(listOf<String>()) }
        var scoreList3 by remember { mutableStateOf(listOf<String>()) }

        LaunchedEffect(Unit) {
            viewModel.getScoreLevelInfo()
            viewModel.getUserGroupInfo()

            val newList1 = mutableListOf<String>()
            val newList2 = mutableListOf<String>()
            val newList3 = mutableListOf<String>()

            checkedItem?.scoreLevels?.takeIf { it.isNotEmpty() }?.let { list ->
                list.forEach {
                    when(it.scoreGroupId) {
                        1 -> newList1.add(it.scoreLevelName ?: "-")
                        2 -> newList2.add(it.scoreLevelName ?: "-")
                        else -> newList3.add(it.scoreLevelName ?: "-")
                    }
                }

                // 替换整个状态以触发重组
                scoreList1 = newList1
                scoreList2 = newList2
                scoreList3 = newList3
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(15.dp))
            ScoreInfoContent(
                title = "值班副总编辑",
                enable = scoreGroupStatus.map { it.type }.contains(1),
                scoreLevelList = scoreLevelStatus,
                scoredList = scoreList1
            ) {
                scoreLevel = it
            }
            HorizontalDivider(
                modifier = Modifier
                    .padding(top = 15.dp)
                    .fillMaxWidth(),
                thickness = 0.5.dp,
                color = OutlineColor
            )
            ScoreInfoContent(
                title = "值班编委",
                enable = scoreGroupStatus.map { it.type }.contains(2),
                scoreLevelList = scoreLevelStatus,
                scoredList = scoreList2
            ) {
                scoreLevel = it
            }
            HorizontalDivider(
                modifier = Modifier
                    .padding(top = 15.dp)
                    .fillMaxWidth(),
                thickness = 0.5.dp,
                color = OutlineColor
            )
            ScoreInfoContent(
                title = "专家",
                enable = scoreGroupStatus.map { it.type }.contains(3),
                scoreLevelList = scoreLevelStatus,
                scoredList = scoreList3
            ) {
                scoreLevel = it
            }
            Spacer(modifier = Modifier.height(15.dp))
        }

    }
}

@Composable
private fun ScoreInfoContent(
    title: String,
    enable: Boolean = false,
    scoreLevelList: List<ScoreLevelData> = listOf(),
    scoredList: List<String> = listOf(),
    onScoreSelect: (Int) -> Unit = {_ -> }
) {
    val showScoreFilter = remember { mutableStateOf(false) }
    var currentIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        if(enable && scoreLevelList.isNotEmpty()) {
            onScoreSelect.invoke(scoreLevelList[currentIndex].levelNum)
        }
    }

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
                    text = if (enable && scoreLevelList.isNotEmpty()) scoreLevelList[currentIndex].levelCode else "A",
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
        range = scoreLevelList.map { it.levelCode },
        value = currentIndex,
        onChange = {
            currentIndex = it
            onScoreSelect.invoke(scoreLevelList[it].levelNum)
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
private fun ArchiveScoreHeader(viewModel: ReviewManageViewModel, onSearch: (String) -> Unit = {}) {
    val showScoreDialog = remember { mutableStateOf(false) }

    var isStartDatePickerShow by remember { mutableStateOf(true) }
    var datePickerShow by remember { mutableStateOf(false) }
    val scoreRuleStatus by viewModel.scoreRuleStatus.collectAsStateWithLifecycle()
    var currentIndex = remember { mutableIntStateOf(0) }

    val scoreStateFilters = listOf(
        FilterType(type = 1, desc = "全部"),
        FilterType(type = 2, desc = "待打分"),
        FilterType(type = 3, desc = "已打分")
    )

    val searchFilters = listOf(
        FilterType(type = 3, desc = "稿件名称"),
        FilterType(type = 2, desc = "稿件ID"),
        FilterType(type = 1, desc = "记者")
    )

    LaunchedEffect(scoreRuleStatus) {
        if (scoreRuleStatus.isNotEmpty()) {
            while (true) {
                delay(4000) // 每3秒切换一次
                currentIndex.intValue = (currentIndex.intValue + 1) % scoreRuleStatus.size
            }
        }
    }

    if (scoreRuleStatus.isNotEmpty()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 15.dp, end = 15.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            ArchiveScoreFlipItem(scoreRuleStatus, currentIndex) {
                showScoreDialog.value = true
            }
        }
    }

    Card(
        modifier = Modifier
            .padding(top = 12.dp, start = 15.dp, end = 15.dp)
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
                    text = viewModel.startDateText.value,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (viewModel.startDateText.value != "开始时间") MainTextColor else EditHintTextColor
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
                    text = viewModel.endDateText.value,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (viewModel.endDateText.value != "结束时间") MainTextColor else EditHintTextColor
                )
                Icon(
                    modifier = Modifier.size(18.dp).clickableWithoutRipple {
                        viewModel.startDateText.value = "开始时间"
                        viewModel.endDateText.value = "结束时间"
                        viewModel.startLocalDate = LocalDate.now()
                        viewModel.endLocalDate = LocalDate.now()
                    },
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
                FilterDropMenuView(
                    filterData = scoreStateFilters,
                    initialIndex = viewModel.userScoreStatus.intValue
                ) { _, index ->
                    viewModel.userScoreStatus.intValue = index
                    viewModel.getArchiveList(isRefresh = true)
                }
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
                FilterDropMenuView(
                    filterData = scoreStateFilters,
                    initialIndex = viewModel.newsScoreStatus.intValue
                ) { _, index ->
                    viewModel.newsScoreStatus.intValue = index
                    viewModel.getArchiveList(isRefresh = true)
                }
            }
        }
        FilterSearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .border(0.5.dp, Color(0xFFEAEAEB), RoundedCornerShape(4.dp))
                .background(PageBackgroundColor)
                .height(36.dp),
            initialIndex = viewModel.searchType.intValue,
            initialSearchText = viewModel.searchKeyword.value,
            filterData = searchFilters,
            filterClick = { _, index ->
                viewModel.searchType.intValue = index
            }
        ) {
            viewModel.searchKeyword.value = it
            onSearch.invoke(it)
        }
    }

    YBDialog(
        dialogState = showScoreDialog,
        onDismissRequest = { showScoreDialog.value = false },
        title = "稿件评分情况",
        widthRate = 0.9f,
        content = {
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
                scoreRuleStatus.forEachIndexed { index, item ->
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
                            text = item.scoreLevelName ?: "--",
                            fontSize = 14.sp,
                            color = MainTextColor
                        )
                        Text(
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            text = "${item.monthCount}",
                            fontSize = 14.sp,
                            color = MainTextColor
                        )
                        Text(
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            text = "${item.dayCount}",
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
        value = if (isStartDatePickerShow) viewModel.startLocalDate else viewModel.endLocalDate,
        onCancel = { datePickerShow = false },
        onChange = {
            if (isStartDatePickerShow) {
                viewModel.startDateText.value = "${it.year}-${it.monthValue}-${it.dayOfMonth}"
                viewModel.startLocalDate = it
            } else {
                viewModel.endDateText.value = "${it.year}-${it.monthValue}-${it.dayOfMonth}"
                viewModel.endLocalDate = it
            }
        }
    )
}


@Composable
private fun ArchiveScoreFlipItem(list: List<ScoreRuleData>, indexState: MutableIntState, onMoreClick: () -> Unit = {}) {
    Row(modifier = Modifier
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

        AnimatedContent(
            modifier = Modifier.weight(3f),
            targetState = indexState.intValue,
            transitionSpec = {
                slideInVertically { height -> height } + fadeIn() togetherWith
                        slideOutVertically { height -> -height } + fadeOut()
            }
        ) { targetIndex ->
            Row(modifier = Modifier.fillMaxWidth()) {
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
                        text = list[targetIndex].scoreLevelName?.ifEmpty { "-" } ?: "-",
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
                        text = "${list[targetIndex].monthCount}",
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
                        text = "${list[targetIndex].dayCount}",
                        color = MainTextColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                VerticalDivider(
                    modifier = Modifier.height(36.dp), thickness = 0.5.dp, color = OutlineColor
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .clickableWithoutRipple {
                    onMoreClick.invoke()
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

@PhonePreview
@Composable
private fun ArchiveScoreScreenPreview() {
    YBTheme {
        val isRefreshing = remember { mutableStateOf(false) }
        val isLoadingMore = remember { mutableStateOf(false) }
        val canLoadMore = remember { mutableStateOf(true) }
        val loadingState = rememberTipsDialogState()
        val items = remember { mutableStateOf(listOf<ArchiveListData>()) }
        repeat(10) {
            items.value += ArchiveListData(
                title = "标题$it",
                publishTime = LocalDateTime.now().toMillisecond()
            )
        }
        ArchiveScoreScreen(
            viewModel = ReviewManageViewModel(
                savedStateHandle = SavedStateHandle(),
                retrofit = { previewRetrofit }
            ),
            navController = NavController(LocalContext.current),
            items = items,
            isRefreshing = isRefreshing,
            isLoadingMore = isLoadingMore,
            canLoadMore = canLoadMore,
            loadingState = loadingState,
        )
    }
}
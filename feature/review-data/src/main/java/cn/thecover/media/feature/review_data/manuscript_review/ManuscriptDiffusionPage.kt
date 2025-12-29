package cn.thecover.media.feature.review_data.manuscript_review

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import cn.thecover.media.core.data.DiffusionDataEntity
import cn.thecover.media.core.widget.component.PrimaryItemScoreRow
import cn.thecover.media.core.widget.component.ScoreItemType
import cn.thecover.media.core.widget.component.YBNormalList
import cn.thecover.media.core.widget.component.YBToast
import cn.thecover.media.core.widget.component.picker.DateType
import cn.thecover.media.core.widget.component.picker.YBDatePicker
import cn.thecover.media.core.widget.component.search.FilterSearchTextField
import cn.thecover.media.core.widget.theme.TertiaryTextColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.core.widget.util.formatDecimalString
import cn.thecover.media.feature.review_data.PreviewReviewDataViewModelFactory
import cn.thecover.media.feature.review_data.ReviewDataViewModel
import cn.thecover.media.feature.review_data.basic_widget.intent.ReviewDataIntent
import cn.thecover.media.feature.review_data.basic_widget.intent.ReviewUIIntent
import cn.thecover.media.feature.review_data.basic_widget.widget.DataItemCard
import cn.thecover.media.feature.review_data.basic_widget.widget.DataItemDropMenuView
import cn.thecover.media.feature.review_data.basic_widget.widget.DataItemRankingRow
import cn.thecover.media.feature.review_data.basic_widget.widget.DataItemSelectionView
import cn.thecover.media.feature.review_data.basic_widget.widget.ExpandItemColumn
import cn.thecover.media.feature.review_data.basic_widget.widget.ManuScriptItemHeader
import cn.thecover.media.feature.review_data.data.ManuscriptReviewFilterState
import java.time.LocalDate

/**
 *  Created by Wing at 10:27 on 2025/8/6
 *  稿件传播效果页面
 */

/**
 * 稿件传播数据展示页面的 Composable 函数。
 *
 * 该页面用于展示多个稿件的传播相关数据，包括稿件基本信息、评分以及传播详情。
 * 页面结构包含一个头部组件、记录总数提示文本以及一个列表项组件用于展示每条稿件数据。
 */
@Composable
fun ManuscriptDiffusionPage(
    viewModel: ReviewDataViewModel = hiltViewModel(),
    backStackEntry: NavBackStackEntry? = null
) {
    val focusManager = LocalFocusManager.current

    // 模拟稿件传播数据列表
    val data by viewModel.manuscriptReviewDiffusionPageState.collectAsState()

    val filter by viewModel.manuscriptDiffusionFilterState.collectAsState()

    // 创建列表状态，用于控制滚动位置
    val listState = rememberLazyListState()

    // 创建 MutableState 用于列表组件
    val manus = remember { mutableStateOf(data.dataList ?: emptyList()) }
    val isLoadingMore = remember { mutableStateOf(data.isLoading) }
    val isRefreshing = remember { mutableStateOf(data.isRefreshing) }
    val canLoadMore = remember { mutableStateOf(data.hasNextPage) }

    // Toast 相关状态 - 使用稿件传播排行页面专用toast
    val snackbarHostState = remember { SnackbarHostState() }
    val toastMessage by viewModel.manuscriptDiffusionToastState.collectAsState()
    // 记录上一次显示的toast消息和时间
    var lastShownToastTime by remember { mutableStateOf(0L) }

    // 监听路由切换，当页面首次显示或切换回来时刷新数据
    LaunchedEffect(backStackEntry?.id, filter) {
        // 然后刷新数据
        viewModel.handleReviewDataIntent(ReviewDataIntent.RefreshManuscriptDiffusion)
    }

    // 使用 LaunchedEffect 监听 StateFlow 变化并同步到 MutableState
    LaunchedEffect(data) {
        manus.value = data.dataList ?: emptyList() ?: emptyList()
        isLoadingMore.value = data.isLoading
        isRefreshing.value = data.isRefreshing
        canLoadMore.value = data.hasNextPage

        // 刷新时滚动到顶部
        if (data.isRefreshing) {
            listState.animateScrollToItem(0)
        }
    }

    // 监听Toast消息，只有5秒内的才显示
    LaunchedEffect(toastMessage.time) {
        val currentTime = System.currentTimeMillis()
        if (toastMessage.message.isNotEmpty() &&
            toastMessage.time != 0L &&
            toastMessage.time != lastShownToastTime &&
            currentTime - toastMessage.time <= 5000
        ) {
            snackbarHostState.showSnackbar(toastMessage.message)
            lastShownToastTime = toastMessage.time
        }
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        YBNormalList(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .clickable { focusManager.clearFocus() },
            items = manus,
            listState = listState,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            header = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
                ) {
                    ManuscriptDiffusionHeader(viewModel, filter)

                    Text(
                            text = buildAnnotatedString {
                                append("共 ")
                                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                                    append(data.total.toString())
                                }
                                append(" 条记录")
                            },
                            style = MaterialTheme.typography.labelMedium,
                            color = TertiaryTextColor,
                            modifier = Modifier
                                .padding(start = 12.dp)
                                .offset(y = (-4).dp)
                        )
                    }


            },
            isLoadingMore = isLoadingMore,
            isRefreshing = isRefreshing,
            canLoadMore = canLoadMore,
            onLoadMore = {
                viewModel.handleReviewDataIntent(ReviewDataIntent.LoadMoreManuscriptDiffusion)
            },
            onRefresh = {
                viewModel.handleReviewDataIntent(ReviewDataIntent.RefreshManuscriptDiffusion)
            }
        ) { item, index ->

            DiffusionItem(index + 1, item, filter.sortField)
        }

        // Toast 组件
        YBToast(snackBarHostState = snackbarHostState)
    }
}


/**
 * 用于展示稿件传播数据的UI组件。主要功能包括：
 * 显示稿件的基本信息（标题、作者、编辑）
 * 展示传播评分数据（公式传播分、最终传播分）
 * 折叠显示详细的转载数据（核心媒体、一级媒体、二级媒体转载数）
 * 展示用户互动数据（阅读数、分享数、点赞数、评论数）
 *
 * @param data 稿件的详细传播数据实体，包含基本信息和各项统计数据
 * @param filterChoice 当前选择的排序字段
 */
@Composable
private fun DiffusionItem(rank: Int, data: DiffusionDataEntity, filterChoice: String) {
    // 控制展开/折叠状态
    var isExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(data) {
        isExpanded = false
    }

    // 使用排名卡片包装整个内容区域
    DataItemCard {
        DataItemRankingRow(ranking = rank) {
            // 可折叠的内容区域，包含基础信息和详细数据
            ExpandItemColumn(
                offset = -12,
                expand = isExpanded,
                onExpandChange = { isExpanded = it },
                content = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        // 显示稿件头部信息：标题、作者、编辑
                        ManuScriptItemHeader(
                            title = data.title,
                            author = data.reporter.joinToString("、") { it.name },
                            editor = data.editor.joinToString("、") { it.name },
                        )
                        // 显示传播评分数据行：公式传播分和最终传播分
                        PrimaryItemScoreRow(
                            items = arrayOf(
                                Triple(
                                    "公式传播分",
                                    formatDecimalString(data.formulaSpreadScore.toString()),
                                    if (filterChoice.contains("公式传播分")) ScoreItemType.NORMAL_WITH_BORDER else ScoreItemType.NORMAL
                                ),
                                Triple(
                                    "最终传播分",
                                    formatDecimalString(data.spreadScore.toString()),
                                    if (filterChoice.contains("最终传播分")) ScoreItemType.NORMAL_WITH_BORDER else ScoreItemType.NORMAL
                                ),
                            )
                        )
                    }
                }, foldContent = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        // 显示转载数据：核心媒体、一级媒体、二级媒体转载数
                        PrimaryItemScoreRow(
                            items = arrayOf(
                                Triple(
                                    "核心媒体\n转载数",
                                    data.coreMediaReprintCount.toString(),
                                    if (filterChoice.contains("核心媒体转载数")) ScoreItemType.NORMAL_WITH_BORDER else ScoreItemType.NORMAL
                                ),
                                Triple(
                                    "一级媒体\n转载数",
                                    data.level1MediaReprintCount.toString(),
                                    if (filterChoice.contains("一级媒体转载数")) ScoreItemType.NORMAL_WITH_BORDER else ScoreItemType.NORMAL
                                ),
                                Triple(
                                    "二级媒体\n转载数",
                                    data.level2MediaReprintCount.toString(),
                                    if (filterChoice.contains("二级媒体转载数")) ScoreItemType.NORMAL_WITH_BORDER else ScoreItemType.NORMAL
                                ),
                            )
                        )
                        // 显示用户互动数据：阅读数、分享数、点赞数、评论数
                        PrimaryItemScoreRow(
                            items = arrayOf(
                                Triple(
                                    "阅读数",
                                    data.readCount.toString(),
                                    if (filterChoice.contains("阅读数")) ScoreItemType.NORMAL_WITH_BORDER else ScoreItemType.NORMAL
                                ),
                                Triple(
                                    "分享数",
                                    data.shareCount.toString(),
                                    if (filterChoice.contains("分享数")) ScoreItemType.NORMAL_WITH_BORDER else ScoreItemType.NORMAL
                                ),
                                Triple(
                                    "点赞数",
                                    data.likeCount.toString(),
                                    if (filterChoice.contains("点赞数")) ScoreItemType.NORMAL_WITH_BORDER else ScoreItemType.NORMAL
                                ),
                                Triple(
                                    "评论数",
                                    data.commentCount.toString(),
                                    if (filterChoice.contains("评论数")) ScoreItemType.NORMAL_WITH_BORDER else ScoreItemType.NORMAL
                                ),
                            )
                        )
                    }
                })
        }
    }
}


/**
 * 稿件传播数据筛选头部组件。
 *
 * 该 Composable 函数用于展示和操作稿件传播数据的筛选条件，包括排序方式、时间范围和关键词搜索。
 * 用户可以通过下拉菜单选择排序指数，通过日期选择器选择月份，并通过搜索框输入关键词进行过滤。
 *
 * 注意：此函数为私有函数，仅在当前文件内使用。
 */
@Composable
private fun ManuscriptDiffusionHeader(
    viewModel: ReviewDataViewModel,
    filterState: ManuscriptReviewFilterState
) {

    // 控制日期选择器弹窗的显示状态
    var showDatePicker by remember { mutableStateOf(false) }
    // 存储用户选择的日期文本
    val datePickedText = filterState.selectedDate
    // 存储用户选择的排序字段
    val selectFilterChoice = remember { mutableStateOf(filterState.sortField) }
    // 存储用户选择的搜索字段
    val selectSearchChoice = remember { mutableStateOf(filterState.searchField) }

    // 同步状态
    LaunchedEffect(filterState) {
        // 同步本地状态与filterState
        if (selectFilterChoice.value != filterState.sortField) {
            selectFilterChoice.value = filterState.sortField
        }
        if (selectSearchChoice.value != filterState.searchField) {
            selectSearchChoice.value = filterState.searchField
        }
    }

    LaunchedEffect(selectFilterChoice.value) {
        if (selectFilterChoice.value != filterState.sortField) {
            viewModel.handleUIIntent(
                ReviewUIIntent.UpdateManuscriptDiffusionFilter(
                    state = selectFilterChoice.value,
                    time = filterState.selectedDate
                )
            )
        }
    }

    LaunchedEffect(selectSearchChoice.value) {
        if (selectSearchChoice.value != filterState.searchField) {
            viewModel.handleUIIntent(
                ReviewUIIntent.UpdateManuscriptDiffusionFilter(
                    searchType = selectSearchChoice.value,
                    searchText = filterState.searchText
                )
            )
        }
    }

    // 主体内容卡片容器
    DataItemCard {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // 第一行：排序指数选择 和 时间选择
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 排序字段选择区域
                Column(modifier = Modifier.weight(1f)) {
                    Text("排序指数", style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.height(8.dp))
                    DataItemDropMenuView(
                        data = selectFilterChoice, dataList = listOf(
                            "公式传播分",
                            "最终传播分",
                            "核心媒体转载数",
                            "一级媒体转载数",
                            "二级媒体转载数",
                            "阅读数",
                            "分享数",
                            "点赞数",
                            "评论数",
                        )
                    )
                }
                Spacer(Modifier.width(12.dp))

                // 时间选择区域
                Column(modifier = Modifier.weight(1f)) {
                    Text("时间", style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.height(8.dp))
                    DataItemSelectionView(label = datePickedText, onClick = {
                        showDatePicker = true
                    })
                }
            }

            // 搜索输入区域
            FilterSearchTextField(
                data = selectSearchChoice,
                initialText = filterState.searchText,
                label = "请输入搜索内容",
                dataList = listOf(
                    "稿件名称", "稿件 ID", "记者"
                ),
                onValueChange = { valueType, value ->
//                    // 过滤掉文本末尾的换行符，防止换行字符被带到接口
                    if (value.isEmpty()) {
                        val filteredValue = value.replace(Regex("[\\r\\n]+"), "")
                        viewModel.handleUIIntent(
                            ReviewUIIntent.UpdateManuscriptDiffusionFilter(
                                searchType = valueType,
                                searchText = filteredValue
                            )
                        )
                    }

                },
                onSearch = { valueType, value ->
                    // 过滤掉文本末尾的换行符，防止换行字符被带到接口
                    val filteredValue = value.replace(Regex("[\\r\\n]+"), "")
                    viewModel.handleUIIntent(
                        ReviewUIIntent.UpdateManuscriptDiffusionFilter(
                            searchType = valueType,
                            searchText = filteredValue
                        )
                    )
                })
        }
    }

    // 解析当前选中的日期字符串，转换为LocalDate对象
    val selectedDate = try {
        // 日期格式为 "yyyy年M月"，例如 "2025年8月"
        val regex = """(\d{4})年(\d{1,2})月""".toRegex()
        val matchResult = regex.find(filterState.selectedDate)
        if (matchResult != null) {
            val year = matchResult.groupValues[1].toInt()
            val month = matchResult.groupValues[2].toInt()
            LocalDate.of(year, month, 1)
        } else {
            // 如果解析失败，默认使用上个月
            LocalDate.now().minusMonths(1)
        }
    } catch (e: Exception) {
        // 如果解析失败，默认使用上个月
        LocalDate.now().minusMonths(1)
    }

    // 日期选择器弹窗组件
    YBDatePicker(
        visible = showDatePicker,
        end = LocalDate.now().plusYears(10),
        start = LocalDate.of(2025, 1, 1),
        type = DateType.MONTH,
        onCancel = { showDatePicker = false },
        value = selectedDate,
        onChange = {
            viewModel.handleUIIntent(ReviewUIIntent.UpdateManuscriptDiffusionFilter(time = "${it.year}年${it.monthValue}月"))
        }
    )
}

@Composable
@Preview
private fun ManuscriptDiffusionHeaderPreview() {
    YBTheme {
        ManuscriptDiffusionPage(
            viewModel(
                factory = PreviewReviewDataViewModelFactory()
            )
        )
    }

}


package cn.thecover.media.feature.review_data.department_review

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import cn.thecover.media.core.widget.component.AutoResizeText
import cn.thecover.media.core.widget.component.YBNormalList
import cn.thecover.media.core.widget.component.YBToast
import cn.thecover.media.core.widget.component.picker.DateType
import cn.thecover.media.core.widget.component.picker.YBDatePicker
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.TertiaryTextColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.feature.review_data.PreviewReviewDataViewModelFactory
import cn.thecover.media.feature.review_data.ReviewDataViewModel
import cn.thecover.media.feature.review_data.basic_widget.intent.ReviewDataIntent
import cn.thecover.media.feature.review_data.basic_widget.intent.ReviewUIIntent
import cn.thecover.media.feature.review_data.basic_widget.widget.DataItemCard
import cn.thecover.media.feature.review_data.basic_widget.widget.DataItemRankingRow
import cn.thecover.media.feature.review_data.basic_widget.widget.DataItemSelectionView
import java.time.LocalDate

/**
 *  Created by Wing at 11:40 on 2025/8/5
 *  部门TOP排行
 */


/**
 * 部门排行榜页面
 *
 * 该函数展示部门排名信息，包括各部门的平均分数排名，
 * 并提供月份选择功能用于查看不同时间段的排名数据。
 *
 */
@Composable
internal fun DepartmentTopRankingPage(viewModel: ReviewDataViewModel = hiltViewModel<ReviewDataViewModel>()) {
    // 部门总数据列表，包含部门ID、名称和平均分数
    val departmentTotalData by viewModel.departmentReviewTopPageState.collectAsState()

    // 创建列表状态，用于控制滚动位置
    val listState = rememberLazyListState()

    // 日期选择器显示状态和选中日期文本状态
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickedState by viewModel.departmentTopFilterState.collectAsState()

    // 创建 MutableState 用于列表组件，确保初始状态允许滚动
    val departmentList = remember { mutableStateOf(departmentTotalData.dataList ?: emptyList()) }
    val isLoadingMore = remember { mutableStateOf(departmentTotalData.isLoading) }
    val isRefreshing = remember { mutableStateOf(departmentTotalData.isRefreshing) }
    // 初始化为 true，确保列表可以滚动，后续会根据数据更新
    val canLoadMore = remember { mutableStateOf(true) }

    // Toast 相关状态 - 使用部门TOP榜页面专用toast
    val snackbarHostState = remember { SnackbarHostState() }
    val toastMessage by viewModel.departmentTopToastState.collectAsState()
    // 记录上一次显示的toast消息和时间
    var lastShownToastTime by remember { mutableStateOf(0L) }

    // 使用 LaunchedEffect 监听 StateFlow 变化并同步到 MutableState
    LaunchedEffect(departmentTotalData) {
        departmentList.value = departmentTotalData.dataList ?: emptyList()
        isLoadingMore.value = departmentTotalData.isLoading
        isRefreshing.value = departmentTotalData.isRefreshing
        canLoadMore.value = departmentTotalData.hasNextPage

        // 刷新时滚动到顶部
        if (departmentTotalData.isRefreshing || (departmentTotalData.dataList != null && departmentTotalData.dataList!!.isNotEmpty())) {
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

    LaunchedEffect(datePickedState) {
        viewModel.handleReviewDataIntent(ReviewDataIntent.RefreshDepartmentTopRanking)
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        // 构建页面布局，包含日期选择和部门排名列表
        YBNormalList(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            items = departmentList,
            listState = listState,
            isLoadingMore = isLoadingMore,
            isRefreshing = isRefreshing,
            canLoadMore = canLoadMore,
            onLoadMore = {
                viewModel.handleReviewDataIntent(ReviewDataIntent.LoadMoreDepartmentTopRanking)
            },
            onRefresh = {
                viewModel.handleReviewDataIntent(ReviewDataIntent.RefreshDepartmentTopRanking)
            },
            header = {
                DataItemCard {
                    Column {
                        Text(text = "时间")
                        Spacer(modifier = Modifier.height(8.dp))
                        DataItemSelectionView(label = datePickedState.selectedDate) {
                            showDatePicker = true
                        }
                    }
                }
            }
        ) { item, index ->
            if (index == 0) {
                Row(modifier = Modifier.padding(bottom = 8.dp)) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "排名",
                        style = MaterialTheme.typography.bodySmall,
                        color = TertiaryTextColor
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        "部门名称",
                        style = MaterialTheme.typography.bodySmall,
                        color = TertiaryTextColor
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        "部门人均得分",
                        style = MaterialTheme.typography.bodySmall,
                        color = TertiaryTextColor
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }

            }
            TopRankingItem(
                index + 1,
                item.departmentName,
                if ((item.averageScore % 1).toFloat() == 0f) item.averageScore.toInt()
                    .toDouble() else item.averageScore
            )
        }

        // Toast 组件
        YBToast(snackBarHostState = snackbarHostState)
    }

    // 解析当前选中的日期字符串，转换为LocalDate对象
    val selectedDate = try {
        // 日期格式为 "yyyy年M月"，例如 "2025年8月"
        val regex = """(\d{4})年(\d{1,2})月""".toRegex()
        val matchResult = regex.find(datePickedState.selectedDate)
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

    // 月份选择器组件，用于选择查看排名的月份
    YBDatePicker(
        visible = showDatePicker,
        type = DateType.MONTH,
        onCancel = { showDatePicker = false },
        end = LocalDate.now().plusYears(10),
        start = LocalDate.of(2025, 1, 1),
        value = selectedDate,
        onChange = {
            viewModel.handleUIIntent(ReviewUIIntent.UpdateDepartmentTopFilter("${it.year}年${it.monthValue}月"))
        }
    )
}


/**
 * 顶部排名项的可组合函数，用于显示部门排名信息
 *
 * @param ranking 排名序号
 * @param departmentName 部门名称
 * @param score 部门人均得分
 */
@Composable
private fun TopRankingItem(ranking: Int, departmentName: String, score: Double) {
    // 使用排名卡片包装器显示排名信息
    DataItemCard {
        DataItemRankingRow(ranking, paddingTop = 3) {
            // 水平排列部门名称和得分信息
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = departmentName,
                    color = MainTextColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(start = 16.dp, end = 10.dp).weight(1f)
                )
                Text(
                    if (score % 1 == 0.0) score.toInt().toString() else score.toString(),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun TopRankingItemPreview() {
    YBTheme {
        TopRankingItem(1, "部门名称部门名称部门名称部门名称部门名称部门名称部门名称部门名称部门名称部门名称部门名称部门名称部门名称", 4.5)
    }

}
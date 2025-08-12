package cn.thecover.media.feature.review_data.department_review

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import cn.thecover.media.core.widget.component.YBNormalList
import cn.thecover.media.core.widget.component.picker.DateType
import cn.thecover.media.core.widget.component.picker.YBDatePicker
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.feature.review_data.ReviewDataViewModel
import cn.thecover.media.feature.review_data.basic_widget.intent.ReviewDataIntent
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
    val departmentTotalData by viewModel.departmentReviewTopState.collectAsState()


    // 获取当前日期并格式化为年月文本
    val currentDate = LocalDate.now()
    val currentMonthText = "${currentDate.year}年${currentDate.monthValue}月"

    // 日期选择器显示状态和选中日期文本状态
    var showDatePicker by remember { mutableStateOf(false) }
    var datePickedText by remember { mutableStateOf(currentMonthText) }


    // 创建 MutableState 用于列表组件
    val departmentList = remember { mutableStateOf(departmentTotalData.departments) }
    val isLoadingMore = remember { mutableStateOf(departmentTotalData.isLoading) }
    val isRefreshing = remember { mutableStateOf(departmentTotalData.isRefreshing) }
    val canLoadMore = remember { mutableStateOf(true) }

    // 使用 LaunchedEffect 监听 StateFlow 变化并同步到 MutableState
    LaunchedEffect(departmentTotalData) {
        departmentList.value = departmentTotalData.departments
        isLoadingMore.value = departmentTotalData.isLoading
        isRefreshing.value = departmentTotalData.isRefreshing
    }


    LaunchedEffect(datePickedText) {
        viewModel.handleReviewDataIntent(ReviewDataIntent.RefreshDepartmentTopRanking)
    }
    // 构建页面布局，包含日期选择和部门排名列表
    YBNormalList(
        modifier = Modifier.padding(horizontal = 16.dp).fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        items = departmentList,
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
                    DataItemSelectionView(label = datePickedText) {
                        showDatePicker = true
                    }
                }
            }
        }
    ) {item, index ->
        TopRankingItem(item.departmentRanking, item.departmentName, item.averageScore)
    }

    // 月份选择器组件，用于选择查看排名的月份
    YBDatePicker(
        visible = showDatePicker,
        type = DateType.MONTH,
        onCancel = { showDatePicker = false },
        onChange = {
            datePickedText = "${it.year}年${it.monthValue}月"
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
private fun TopRankingItem(ranking: Int, departmentName: String, score: Int) {
    // 使用排名卡片包装器显示排名信息
    DataItemCard {
        DataItemRankingRow(ranking) {
            // 水平排列部门名称和得分信息
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = departmentName, style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    "部门人均得分",
                    style = MaterialTheme.typography.bodySmall,
                    color = MainTextColor
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    score.toString(),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun TopRankingItemPreview() {
    YBTheme {
        DepartmentTopRankingPage(ReviewDataViewModel(SavedStateHandle()))
    }

}
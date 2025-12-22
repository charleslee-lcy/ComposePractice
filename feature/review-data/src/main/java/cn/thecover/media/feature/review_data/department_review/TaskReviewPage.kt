package cn.thecover.media.feature.review_data.department_review

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cn.thecover.media.core.widget.component.YBNormalList
import cn.thecover.media.core.widget.component.YBToast
import cn.thecover.media.core.widget.component.picker.DateType
import cn.thecover.media.core.widget.component.picker.YBDatePicker
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.SecondaryTextColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.feature.review_data.ReviewDataViewModel
import cn.thecover.media.feature.review_data.basic_widget.intent.ReviewDataIntent
import cn.thecover.media.feature.review_data.basic_widget.intent.ReviewUIIntent
import cn.thecover.media.feature.review_data.basic_widget.widget.DataItemCard
import cn.thecover.media.feature.review_data.basic_widget.widget.DataItemSelectionView
import java.time.LocalDate


/**
 *  Created by Wing at 14:48 on 2025/8/4
 *  部门任务完成情况页面
 */


/**
 * 部门任务审核页面的 Composable 函数。
 * 该页面展示各部门的任务完成情况，并允许用户选择查看的月份数据。
 *
 * 此函数不接受任何参数，也不返回任何值。
 */
@Composable
fun DepartmentTaskReviewPage(viewModel: ReviewDataViewModel = hiltViewModel()) {
    // 控制日期选择器是否显示的状态
    var showDatePicker by remember { mutableStateOf(false) }

    // 存储用户选择的日期文本（格式：yyyy年M月）
    val datePickedState by viewModel.departmentTaskFilterState.collectAsState()

    val taskState by viewModel.departmentTaskPageState.collectAsState()
    // 创建 MutableState 用于列表组件，将可空列表转换为非空列表
    val departmentTaskList = remember { mutableStateOf(taskState.dataList ?: emptyList()) }
    val isLoadingMore = remember { mutableStateOf(taskState.isLoading) }
    val isRefreshing = remember { mutableStateOf(taskState.isRefreshing) }
    val canLoadMore = remember { mutableStateOf(taskState.hasNextPage) }

    // Toast 相关状态
    val snackbarHostState = remember { SnackbarHostState() }
    val toastMessage by viewModel.iconTipsDialogState.collectAsState()

    // 使用 LaunchedEffect 监听 StateFlow 变化并同步到 MutableState
    LaunchedEffect(taskState) {
        departmentTaskList.value = taskState.dataList ?: emptyList()
        isLoadingMore.value = taskState.isLoading
        isRefreshing.value = taskState.isRefreshing
        canLoadMore.value = taskState.hasNextPage

        // 监听错误信息并显示 Toast
        taskState.error?.let { errorMessage ->
            viewModel.handleReviewDataIntent(ReviewDataIntent.ShowToast(errorMessage))
        }
    }

    // 监听Toast消息
    LaunchedEffect(toastMessage.time) {
        if (toastMessage.message.isNotEmpty()) {
            snackbarHostState.showSnackbar(toastMessage.message)
        }
    }

    LaunchedEffect(datePickedState) {
        viewModel.handleReviewDataIntent(ReviewDataIntent.RefreshDepartmentTaskData)
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        YBNormalList(
            items = departmentTaskList,
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            isRefreshing = isRefreshing,
            isLoadingMore = isLoadingMore,
            canLoadMore = canLoadMore,
            header = {
                DataItemCard {
                    Column {
                        Text(text = "时间")
                        Spacer(modifier = Modifier.height(8.dp))
                        DataItemSelectionView(label = datePickedState.selectedDate, onClick = {
                            showDatePicker = !showDatePicker
                        })
                    }
                }
            },
            onRefresh = {
                viewModel.handleReviewDataIntent(ReviewDataIntent.RefreshDepartmentTaskData)
            },
            onLoadMore = {
                viewModel.handleReviewDataIntent(
                    ReviewDataIntent.LoadMoreDepartmentTaskData
                )
            },
        ) { item, position ->
            TaskReviewItemView(
                item.departmentName.toString(),
                item.finishedPersonNum,
                item.taskGoalNum,
                item.finishPercent,
                item.subCoefficient
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

    // 月份选择器弹窗，当 showDatePicker 为 true 时显示
    YBDatePicker(
        visible = showDatePicker,
        type = DateType.MONTH,
        onCancel = { showDatePicker = false },
        end = LocalDate.now().plusYears(10),
        start = LocalDate.of(2025, 1, 1),
        value = selectedDate,
        onChange = {
            viewModel.handleUIIntent(ReviewUIIntent.UpdateDepartmentTaskFilter("${it.year}年${it.monthValue}月"))
        }
    )
}


/**
 * 任务回顾列表项视图，用于展示单个任务的完成情况。
 *
 * @param itemName 任务名称
 * @param finishedPersons 已完成该任务的人数
 * @param totalPersons 总共需要完成该任务的人数
 * @param itemProgress 任务完成进度（0f 到 1f）
 * @param subCoefficient 扣系数，可为空
 */
@Composable
fun TaskReviewItemView(
    itemName: String,
    finishedPersons: Int,
    totalPersons: Int,
    itemProgress: Double,
    subCoefficient: Double? = null
) {
    // 使用卡片容器包装整个内容
    DataItemCard {
        Column {
            // 第一行：显示任务名称和完成人数
            Row {
                Text(
                    text = itemName,
                    style = MaterialTheme.typography.titleSmall,
                    color = MainTextColor, modifier = Modifier.weight(1f)
                )
                Text(
                    text = "完成人数: ",
                    style = MaterialTheme.typography.bodySmall,
                    color = SecondaryTextColor
                )
                Text(
                    text = finishedPersons.toString(),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = " 人",
                    style = MaterialTheme.typography.bodySmall,
                    color = SecondaryTextColor
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // 第二行：显示完成率、任务描述和总人数
            Row {
                Text(
                    "完成率：${(itemProgress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = SecondaryTextColor
                )
                Spacer(Modifier.width(20.dp))

                Text(
                    "扣系数 $subCoefficient",
                    style = MaterialTheme.typography.bodySmall,
                    color = SecondaryTextColor
                )

                Spacer(Modifier.weight(1f))
                Text(
                    "任务: $totalPersons 人",
                    style = MaterialTheme.typography.bodySmall,
                    color = SecondaryTextColor
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // 进度条：可视化展示任务完成进度
            LinearProgressIndicator(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                progress = { itemProgress.toFloat() },
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primary.copy(0.1f),
                drawStopIndicator = {

                },
                gapSize = 0.dp
            )
        }
    }
}


@Composable
@Preview(showSystemUi = true)
fun DepartmentTaskReviewScreenPreview() {
    YBTheme {
        DepartmentTaskReviewPage()
    }

}
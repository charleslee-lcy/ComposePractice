package cn.thecover.media.feature.review_data.department_review

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cn.thecover.media.core.widget.component.picker.DateType
import cn.thecover.media.core.widget.component.picker.YBDatePicker
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.SecondaryTextColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.feature.review_data.basic_widget.DataItemCard
import cn.thecover.media.feature.review_data.basic_widget.DataItemSelectionView
import cn.thecover.media.feature.review_data.data.DepartmentTaskDataEntity
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
fun DepartmentTaskReviewPage() {
    // 获取当前日期并格式化为“年月”文本，用于初始化显示的时间
    val currentDate = LocalDate.now()
    val currentMonthText = "${currentDate.year}年${currentDate.monthValue}月"

    // 控制日期选择器是否显示的状态
    var showDatePicker by remember { mutableStateOf(false) }

    // 存储用户选择的日期文本（格式：yyyy年M月）
    var datePickedText by remember { mutableStateOf(currentMonthText) }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 模拟的部门任务数据列表
        val items = mutableStateListOf(
            DepartmentTaskDataEntity("时政部", 150, 150, 1f, "扣系数0.1"),
            DepartmentTaskDataEntity("社会新闻部", 23, 30, 23.toFloat() / 30, "扣系数0.1"),
            DepartmentTaskDataEntity("教育事业部", 243, 500, 243.toFloat() / 500, "扣系数0.1"),
            DepartmentTaskDataEntity("科创部", 3, 100, 3 / 100.toFloat(), "扣系数0.1"),
        )

        // 顶部时间选择卡片
        item {
            DataItemCard {
                Column {
                    Text(text = "时间")
                    Spacer(modifier = Modifier.height(8.dp))
                    DataItemSelectionView(label = datePickedText, onClick = {
                        showDatePicker = !showDatePicker
                    })
                }
            }
        }

        // 遍历并显示每个部门的任务审核项
        items(items) {
            TaskReviewItemView(
                it.departmentName.toString(),
                it.taskFinishedPersons,
                it.taskTotalPersons,
                it.taskProgress,
                it.taskDesc
            )
        }
    }

    // 月份选择器弹窗，当 showDatePicker 为 true 时显示
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
 * 任务回顾列表项视图，用于展示单个任务的完成情况。
 *
 * @param itemName 任务名称
 * @param finishedPersons 已完成该任务的人数
 * @param totalPersons 总共需要完成该任务的人数
 * @param itemProgress 任务完成进度（0f 到 1f）
 * @param itemDesc 任务描述，可为空
 */
@Composable
fun TaskReviewItemView(
    itemName: String,
    finishedPersons: Int,
    totalPersons: Int,
    itemProgress: Float,
    itemDesc: String? = null
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
                if (itemDesc != null) {
                    Text(
                        itemDesc,
                        style = MaterialTheme.typography.bodySmall,
                        color = SecondaryTextColor
                    )
                }
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
                progress = { itemProgress },
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
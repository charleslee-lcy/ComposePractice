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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.SecondaryTextColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.feature.review_data.basic_widget.DataItemCard
import cn.thecover.media.feature.review_data.basic_widget.DataItemDropMenu
import cn.thecover.media.feature.review_data.data.DepartmentTaskDataEntity

/**
 *  Created by Wing at 14:48 on 2025/8/4
 *  部门任务完成情况页面
 */

@Composable
fun DepartmentTaskReviewPage() {
    LazyColumn(

        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val items = mutableStateListOf(
            DepartmentTaskDataEntity("时政部", 150, 150, 1f, "扣系数0.1"),
            DepartmentTaskDataEntity("社会新闻部", 23, 30, 23.toFloat() / 30, "扣系数0.1"),
            DepartmentTaskDataEntity("教育事业部", 243, 500, 243.toFloat() / 500, "扣系数0.1"),
            DepartmentTaskDataEntity("科创部", 3, 100, 3 / 100.toFloat(), "扣系数0.1"),
        )

        item {
            DataItemCard {
                Column {
                    Text(text = "时间")
                    Spacer(modifier = Modifier.height(8.dp))
                    DataItemDropMenu("2025年5月")
                }
            }
        }
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
}


@Composable
fun TaskReviewItemView(
    itemName: String,
    finishedPersons: Int,
    totalPersons: Int,
    itemProgress: Float,
    itemDesc: String? = null
) {
    DataItemCard {
        Column {
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
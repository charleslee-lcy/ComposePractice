package cn.thecover.media.feature.review_data.department_review

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cn.thecover.media.core.widget.component.picker.DateType
import cn.thecover.media.core.widget.component.picker.YBDatePicker
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.feature.review_data.basic_widget.widget.DataItemCard
import cn.thecover.media.feature.review_data.basic_widget.widget.DataItemRankingCard
import cn.thecover.media.feature.review_data.basic_widget.widget.DataItemSelectionView
import cn.thecover.media.feature.review_data.data.DepartmentTotalDataEntity
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
internal fun DepartmentTopRankingPage() {
    // 部门总数据列表，包含部门ID、名称和平均分数
    val departmentTotalData = remember {
        mutableStateListOf(
            DepartmentTotalDataEntity(1, "部门1", averageScore = 224),
            DepartmentTotalDataEntity(2, "部门2", averageScore = 222),
            DepartmentTotalDataEntity(3, "部门3", averageScore = 220),
            DepartmentTotalDataEntity(4, "部门4", averageScore = 218),
        )
    }

    // 获取当前日期并格式化为年月文本
    val currentDate = LocalDate.now()
    val currentMonthText = "${currentDate.year}年${currentDate.monthValue}月"

    // 日期选择器显示状态和选中日期文本状态
    var showDatePicker by remember { mutableStateOf(false) }
    var datePickedText by remember { mutableStateOf(currentMonthText) }

    // 构建页面布局，包含日期选择和部门排名列表
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        item {
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
        items(departmentTotalData) {
            TopRankingItem(it.departmentRanking, it.departmentName, it.averageScore)
        }
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
    DataItemRankingCard(ranking) {
        // 水平排列部门名称和得分信息
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = departmentName, style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.weight(1f))
            Text("部门人均得分", style = MaterialTheme.typography.bodySmall, color = MainTextColor)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                score.toString(),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}


@Composable
@Preview
fun TopRankingItemPreview() {
    YBTheme {
        DepartmentTopRankingPage()

    }

}
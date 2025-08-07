package cn.thecover.media.feature.review_data.department_review

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import cn.thecover.media.feature.review_data.basic_widget.widget.DataItemDropMenuView
import cn.thecover.media.feature.review_data.basic_widget.widget.DataItemRankingRow
import cn.thecover.media.feature.review_data.basic_widget.widget.DataItemSelectionView
import cn.thecover.media.feature.review_data.basic_widget.widget.ReviewDataItemScoreRow
import cn.thecover.media.feature.review_data.data.DepartmentTotalDataEntity
import java.time.LocalDate


/**
 *  Created by Wing at 10:54 on 2025/8/4
 *  部门总数据排行
 */

/**
 * 部门评审屏幕组件
 *
 * 该函数显示一个部门评审列表界面，包含部门总览头部和各个部门的评审项。
 * 使用LazyColumn实现可滚动的列表布局，展示预定义的部门数据。
 *
 * @param modifier 用于修饰该组件的Modifier，默认为Modifier
 */
@Composable
internal fun DepartmentReviewScreen(
    modifier: Modifier = Modifier
) {
    // 创建部门数据列表
    val depart = mutableListOf(
        DepartmentTotalDataEntity(1, "经济部"),
        DepartmentTotalDataEntity(2, "时政新闻部"),
        DepartmentTotalDataEntity(3, "市场部"),
        DepartmentTotalDataEntity(4, "国际部"),
    )

    // 构建屏幕UI布局
    Surface(modifier = Modifier.padding(horizontal = 16.dp)) {
        LazyColumn(
            modifier = modifier
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // 添加部门总览头部组件
            item { DepartmentTotalHeader() }

            // 为每个部门数据项添加评审项组件
            items(depart) {
                DepartmentReviewItem(it.departmentRanking, it.departmentName)
            }
        }

    }
}


/**
 * 部门总稿费头部筛选组件
 *
 * 该函数创建一个可组合的头部筛选区域，包含排序指数下拉选择和时间月份选择功能。
 * 用户可以通过下拉菜单选择不同的排序方式，通过日期选择器选择目标月份。
 *
 */
@Composable
private fun DepartmentTotalHeader() {
    val currentDate = LocalDate.now()
    val currentMonthText = "${currentDate.year}年${currentDate.monthValue}月"

    var showDatePicker by remember { mutableStateOf(false) }
    var datePickedText by remember { mutableStateOf(currentMonthText) }
    val selectFilterChoice = remember { mutableStateOf("部门总稿费") }

    // 创建数据项卡片容器，包含排序指数和时间选择两个主要功能区域
    DataItemCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 左侧排序指数选择区域
            Column(modifier = Modifier.weight(1f)) {
                Text("排序指数", style = MaterialTheme.typography.labelMedium)
                Spacer(Modifier.height(8.dp))
                DataItemDropMenuView(data = selectFilterChoice)
            }
            Spacer(Modifier.width(12.dp))

            // 右侧时间选择区域
            Column(modifier = Modifier.weight(1f)) {
                Text("时间", style = MaterialTheme.typography.labelMedium)
                Spacer(Modifier.height(8.dp))
                DataItemSelectionView(label = datePickedText, onClick = {
                    showDatePicker = true
                })
            }
        }
    }

    // 月份选择器弹窗组件
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
 * 部门评审项组件
 *
 * @param ranking 部门排名
 * @param name 部门名称
 */
@Composable
private fun DepartmentReviewItem(
    ranking: Int,
    name: String,
) {
    // 显示部门排名卡片
    DataItemCard {
        DataItemRankingRow(ranking = ranking) {
            Column {
                // 显示部门名称
                Text(name, color = MainTextColor, style = MaterialTheme.typography.titleMedium)
                // 显示评审数据项得分行，包含总稿费、总人数、人员平均分和总分
                ReviewDataItemScoreRow(
                    items = arrayOf(
                        Pair(
                            "总稿费", 1000.toString()
                        ),
                        Pair(
                            "总人数", 100.toString()
                        ),
                        Pair(
                            "人员平均分", 100.toString()
                        ),
                        Pair(
                            "总分", 100.toString()
                        )

                    )
                )
            }
        }

    }
}


@Composable
@Preview(showSystemUi = true)
fun DepartmentReviewScreenPreview() {
    YBTheme {
        DepartmentReviewScreen()
    }
}


@Composable
@Preview
fun DepartmentReviewItemPreview() {
    YBTheme {
        DepartmentReviewItem(1, "经济部")
    }
}



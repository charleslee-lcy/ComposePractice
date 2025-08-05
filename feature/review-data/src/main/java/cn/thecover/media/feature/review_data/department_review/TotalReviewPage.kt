package cn.thecover.media.feature.review_data.department_review

import androidx.compose.animation.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
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
import cn.thecover.media.core.widget.theme.SecondaryTextColor
import cn.thecover.media.core.widget.theme.YBShapes
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.feature.review_data.basic_widget.DataItemCard
import cn.thecover.media.feature.review_data.basic_widget.DataItemSelectionView
import cn.thecover.media.feature.review_data.basic_widget.DataItemDropMenuView
import cn.thecover.media.feature.review_data.basic_widget.chooseRankingColor
import cn.thecover.media.feature.review_data.data.DepartmentTotalDataEntity

/**
 *  Created by Wing at 10:54 on 2025/8/4
 *  部门总数据排行
 */


@Composable
internal fun DepartmentReviewScreen(
    modifier: Modifier = Modifier
) {
    val depart = mutableListOf<DepartmentTotalDataEntity>(
        DepartmentTotalDataEntity(1, "经济部"),
        DepartmentTotalDataEntity(2, "时政新闻部"),
        DepartmentTotalDataEntity(3, "市场部"),
        DepartmentTotalDataEntity(4, "国际部"),
    )
    Surface {
        LazyColumn(
            modifier = modifier
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item { DepartmentTotalHeader() }
            items(depart) {
                DepartmentReviewItem(it.departmentRanking, it.departmentName)
            }
        }

    }
}

@Composable
private fun DepartmentTotalHeader() {
    var showDatePicker by remember { mutableStateOf(false) }
    var datePickedText by remember { mutableStateOf("2025年5月") }
    val selectFilterChoice = remember { mutableStateOf("部门总稿费") }
    DataItemCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("排序指数", style = MaterialTheme.typography.labelMedium)
                Spacer(Modifier.height(8.dp))
                DataItemDropMenuView(data = selectFilterChoice)
            }
            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text("时间", style = MaterialTheme.typography.labelMedium)
                Spacer(Modifier.height(8.dp))
                DataItemSelectionView(label = datePickedText, onClick = {
                    showDatePicker = true
                })
            }
        }
    }

    YBDatePicker(
        visible = showDatePicker,
        type = DateType.MONTH,
        onCancel = { showDatePicker = false },
        onChange = {
            datePickedText = "${it.year}年${it.monthValue}月"
        }
    )
}


@Composable
private fun DepartmentReviewItem(
    ranking: Int,
    name: String,
) {
    DataItemCard {

        Row(verticalAlignment = Alignment.Top) {
            Text(
                text = ranking.toString(),
                style = MaterialTheme.typography.titleSmall,
                color = ranking.chooseRankingColor(),
                modifier = Modifier.padding(top = 1.dp, end = 10.dp)
            )
            Column {
                Text(name, color = MainTextColor, style = MaterialTheme.typography.titleMedium)


                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                ) {

                    Box(modifier = Modifier.weight(1f)) {
                        DepartmentTotalDataView("总稿费", "1000", isSelected = true)
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        DepartmentTotalDataView(
                            "总人数",
                            "1000"
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        DepartmentTotalDataView(
                            "人员平均分",
                            "1000"
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) { DepartmentTotalDataView("总分", "1000") }


                }
            }
        }

    }
}



@Composable
private fun DepartmentTotalDataView(
    item: String,
    value: String,
    isSelected: Boolean = false,
    desc: String? = "",

    ) {
    val targetBgColor =
        if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
    val animatedColor = remember { Animatable(targetBgColor) }

    val targetTextColor =
        if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MainTextColor
    val animatedTextColor = remember { Animatable(targetTextColor) }


    val targetItemColor =
        if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer.copy(0.7f) else SecondaryTextColor


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                shape = YBShapes.small
            )
            .background(
                color = animatedColor.value,
                shape = YBShapes.small
            )
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            item,
            style = MaterialTheme.typography.bodySmall,
            color = targetItemColor,
            maxLines = 1,
            modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE)
        )
        Spacer(Modifier.height(5.dp))
        Text(
            value,
            style = MaterialTheme.typography.titleSmall,
            color = animatedTextColor.value,
            maxLines = 1,
            modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE)
        )
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

@Composable
@Preview
fun DepartmentReviewDataViewPreview() {
    YBTheme {
        DepartmentTotalDataView("总稿费", value = "1000")
    }
}

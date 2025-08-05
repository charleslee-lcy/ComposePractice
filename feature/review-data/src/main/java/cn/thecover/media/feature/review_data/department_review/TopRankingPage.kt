package cn.thecover.media.feature.review_data.department_review

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.feature.review_data.basic_widget.DataItemCard
import cn.thecover.media.feature.review_data.basic_widget.DataItemDropMenu
import cn.thecover.media.feature.review_data.basic_widget.chooseRankingColor
import cn.thecover.media.feature.review_data.data.DepartmentTotalDataEntity

/**
 *  Created by Wing at 11:40 on 2025/8/5
 *  部门TOP排行
 */

@Composable
internal fun DepartmentTopRankingPage() {
    val departmentTotalData = remember {
        mutableStateListOf(
            DepartmentTotalDataEntity(1, "部门1", averageScore = 224),
            DepartmentTotalDataEntity(2, "部门2", averageScore = 222),
            DepartmentTotalDataEntity(3, "部门3", averageScore = 220),
            DepartmentTotalDataEntity(4, "部门4", averageScore = 218),
        )
    }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            DataItemCard {
                Column {
                    Text(text = "时间")
                    Spacer(modifier = Modifier.height(8.dp))
                    DataItemDropMenu("2025年5月")
                }
            }
        }
        items(departmentTotalData){
            TopRankingItem(it.departmentRanking,it.departmentName,it.averageScore)
        }
    }
}

@Composable
private fun TopRankingItem(ranking: Int, departmentName: String, score: Int) {
    DataItemCard {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                ranking.toString(),
                style = MaterialTheme.typography.titleSmall,
                color = ranking.chooseRankingColor()
            )
            Spacer(modifier = Modifier.width(12.dp))
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
        TopRankingItem(1, "经济部", 222)
    }

}
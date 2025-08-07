package cn.thecover.media.feature.basis.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cn.thecover.media.core.widget.component.ItemScoreRow
import cn.thecover.media.core.widget.component.YBTab
import cn.thecover.media.core.widget.component.YBTabRow
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.MsgColor
import cn.thecover.media.core.widget.theme.SecondaryAuxiliaryColor
import cn.thecover.media.core.widget.theme.SecondaryTextColor
import cn.thecover.media.core.widget.theme.TertiaryAuxiliaryColor
import cn.thecover.media.core.widget.theme.TertiaryTextColor
import cn.thecover.media.core.widget.theme.YBTheme

/**
 *  Created by Wing at 15:48 on 2025/8/7
 *  稿件组件
 */

@Composable
internal fun ManuscriptTopRankingItem() {
    val titles = listOf("稿件TOP10", "稿件传播力TOP10")
    val currentIndex = remember { mutableIntStateOf(0) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = normalCardElevation),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column {
            YBTabRow(
                selectedTabIndex = currentIndex.intValue,
                modifier = Modifier.padding(horizontal = 30.dp)
            ) {
                titles.forEachIndexed { index, title ->
                    YBTab(
                        selected = index == currentIndex.intValue,
                        onClick = { currentIndex.intValue = index },
                        text = { Text(text = title) }
                    )
                }
            }
            when (currentIndex.intValue) {
                0 -> TopManuscriptPage()
                else -> TopDiffusionPage()
            }
        }

    }
}

@Composable
private fun TopManuscriptPage() {
    Column(modifier = Modifier.padding(horizontal = 12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        for (index in 1..10) {
            DataItemRankingRow(index) {
                ManuScriptItemHeader("稿件标题", "稿件作者", "稿件编辑")
            }

            ItemScoreRow(
                items = arrayOf(
                    Pair("总分", "80"),
                    Pair("基础分", "80"),
                    Pair("传播分", "80"),
                    Pair("质量分", "80")
                )
            )
            if ( index != 10) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.5.dp),
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Composable
private fun TopDiffusionPage() {
    Column(modifier = Modifier.padding(horizontal = 12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        for (index in 1..10) {
            DataItemRankingRow(index) {
                ManuScriptItemHeader("稿件标题", "稿件作者", "稿件编辑")
            }

            ItemScoreRow(
                items = arrayOf(
                    Pair("公式传播分", "80"),
                    Pair("最终传播分", "80"),
                )
            )
            if ( index != 10) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.5.dp),
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}


/**
 * 带有排名信息的数据卡片组件。
 *
 * @param ranking 当前排名数值，默认为 0。
 * @param content 排名之后的主要内容组件。
 */
@Composable
internal fun DataItemRankingRow(ranking: Int = 0, content: @Composable () -> Unit) {
    Row(verticalAlignment = Alignment.Top) {
        Text(
            text = ranking.toString(),
            style = MaterialTheme.typography.titleSmall,
            color = when (ranking) {
                1 -> MsgColor
                2 -> SecondaryAuxiliaryColor
                3 -> TertiaryAuxiliaryColor
                else -> TertiaryTextColor
            },
            modifier = Modifier.padding(top = 0.dp, end = 10.dp)
        )
        content()
    }
}

@Composable
internal fun ManuScriptItemHeader(
    title: String = "",
    author: String = "",
    editor: String = "",
) {
    // 显示稿件标题
    Column {
        Text(title, style = MaterialTheme.typography.titleSmall, color = MainTextColor)
        Spacer(Modifier.height(8.dp))
        // 显示作者和编辑信息
        Row {
            Text(
                "作者：${author}",
                style = MaterialTheme.typography.bodySmall,
                color = SecondaryTextColor
            )
            Spacer(Modifier.width(20.dp))
            Text(
                "编辑：${editor}",
                style = MaterialTheme.typography.bodySmall,
                color = SecondaryTextColor
            )
        }
    }
}


@Composable
@Preview
fun ManuScriptTopRankingItemPreview() {
    YBTheme {
        ManuscriptTopRankingItem()
    }

}
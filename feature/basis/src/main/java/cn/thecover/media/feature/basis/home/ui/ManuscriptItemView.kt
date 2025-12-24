package cn.thecover.media.feature.basis.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cn.thecover.media.core.data.DiffusionDataEntity
import cn.thecover.media.core.data.ManuscriptReviewDataEntity
import cn.thecover.media.core.data.PaginatedResult
import cn.thecover.media.core.widget.component.PrimaryItemScoreRow
import cn.thecover.media.core.widget.component.ScoreItemType
import cn.thecover.media.core.widget.component.YBTab
import cn.thecover.media.core.widget.component.YBTabRow
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.MsgColor
import cn.thecover.media.core.widget.theme.SecondaryAuxiliaryColor
import cn.thecover.media.core.widget.theme.SecondaryTextColor
import cn.thecover.media.core.widget.theme.TertiaryAuxiliaryColor
import cn.thecover.media.core.widget.theme.TertiaryTextColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.feature.basis.home.HomeViewModel

/**
 *  Created by Wing at 15:48 on 2025/8/7
 *  稿件组件
 */

@Composable
internal fun ManuscriptTopRankingItem(
    homeManuscript: PaginatedResult<ManuscriptReviewDataEntity>,
    homeManuscriptDiffusion: PaginatedResult<DiffusionDataEntity>,
    viewModel: HomeViewModel
) {
    // 始终显示 tab，无论数据是否为空
    val availableTabs = mutableListOf<String>()
    val availablePages = mutableListOf<@Composable () -> Unit>()

    // 添加稿件TOP10 tab
    availableTabs.add("稿件TOP10")
    availablePages.add { TopManuscriptPage(viewModel) }

    // 添加稿件传播力TOP10 tab
    availableTabs.add("稿件传播力TOP10")
    availablePages.add { TopDiffusionPage(viewModel) }
    
    val currentIndex = remember { mutableIntStateOf(0) }

    // 组件初始化时加载第一个tab的数据
    LaunchedEffect(Unit) {
        viewModel.getHomeManuscript()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column {
            YBTabRow(
                selectedTabIndex = currentIndex.intValue,
                modifier = Modifier.padding()
            ) {
                availableTabs.forEachIndexed { index, title ->
                    YBTab(
                        selected = index == currentIndex.intValue,
                        onClick = {
                            currentIndex.intValue = index
                            // 修复：切换 tab 时调用正确接口
                            if (index == 0) {
                                // 稿件TOP10
                                viewModel.getHomeManuscript()
                            } else if (index == 1) {
                                // 稿件传播力TOP10
                                viewModel.getHomeManuscriptDiffusion()
                            }
                        },
                        text = { Text(text = title) }
                    )
                }
            }
            // 显示当前选中的页面
            if (currentIndex.intValue < availablePages.size) {
                availablePages[currentIndex.intValue]()
            }
        }
    }
}

@Composable
private fun TopManuscriptPage(
    viewModel: HomeViewModel
) {
    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

        val uiState by viewModel.homeManuscriptUiState.collectAsState()

        // 如果正在加载，显示加载提示
        if (uiState.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "正在加载...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SecondaryTextColor
                )
            }
            return@Column
        }

        // 检查是否有错误
        if (!uiState.error.isNullOrEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = uiState.error ?: "请求错误",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SecondaryTextColor
                )
            }
            return@Column
        }

        // 如果数据为空，显示暂无数据提示
        if (uiState.dataList?.isEmpty() != false) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "暂无数据",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SecondaryTextColor
                )
            }
            return@Column
        }

        // 显示稿件列表数据
        uiState.dataList?.forEachIndexed { index, item ->
            DataItemRankingRow(index + 1) {
                ManuScriptItemHeader(
                    item.title,
                    author = item.reporter.joinToString("、") { it.name },
                )
            }

            PrimaryItemScoreRow(
                items = arrayOf(
                    Triple("总分",
                        if (item.score % 1 == 0.0) item.score.toInt()
                            .toString() else item.score.toString(),
                        ScoreItemType.PRIMARY
                    ),
                    Triple("基础分",
                        if (item.basicScore % 1 == 0.0) item.basicScore.toInt()
                            .toString() else item.basicScore.toString(),
                        ScoreItemType.NORMAL
                    ),
                    Triple("传播分",
                        if (item.diffusionScore % 1 == 0.0) item.diffusionScore.toInt()
                            .toString() else item.diffusionScore.toString(),
                        ScoreItemType.NORMAL
                    ),
                    Triple("质量分",
                        if (item.qualityScore % 1 == 0.0) item.qualityScore.toInt()
                            .toString() else item.qualityScore.toString(),
                        ScoreItemType.NORMAL
                    )
                )
            )
            if (index != uiState.dataList?.lastIndex) {
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
private fun TopDiffusionPage(
    viewModel: HomeViewModel
) {
    val uiState by viewModel.homeManuscriptDiffusionUiState.collectAsState()
    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

        // 如果正在加载，显示加载提示
        if (uiState.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "正在加载...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SecondaryTextColor
                )
            }
            return@Column
        }

        // 检查是否有错误
        if (!uiState.error.isNullOrEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = uiState.error ?: "请求错误",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SecondaryTextColor
                )
            }
            return@Column
        }

        // 如果数据为空，显示暂无数据提示
        if (uiState.dataList?.isEmpty() != false) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "暂无数据",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SecondaryTextColor
                )
            }
            return@Column
        }

        // 显示稿件传播力数据
        uiState.dataList?.forEachIndexed { index, item ->

            DataItemRankingRow(index + 1) {
                ManuScriptItemHeader(
                    item.title,
                    author = item.reporter.joinToString("、") { it.name },
                )
            }

            PrimaryItemScoreRow(
                items = arrayOf(
                    Triple(
                        "公式传播分",
                        if (item.formulaSpreadScore % 1 == 0.0) item.formulaSpreadScore.toInt()
                            .toString() else item.formulaSpreadScore.toString(),
                        ScoreItemType.NORMAL
                    ),
                    Triple(
                        "最终传播分",
                        if (item.spreadScore % 1 == 0.0) item.spreadScore.toInt()
                            .toString() else item.spreadScore.toString(),
                        ScoreItemType.PRIMARY
                    ),
                )
            )
            if (index != uiState.dataList?.lastIndex) {
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
) {
    // 稿件名称
    Column {
        Text(
            title,
            style = MaterialTheme.typography.titleSmall,
            color = MainTextColor,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        // 显示作者和编辑信息
        Row {
            if (author.isNotEmpty()) {
                Text(
                    "记者：${author}",
                    style = MaterialTheme.typography.bodySmall,
                    color = SecondaryTextColor
                )
            }
            Spacer(Modifier.width(20.dp))
//            if (editor.isNotEmpty()) {
//                Text(
//                    "编辑：${editor}",
//                    style = MaterialTheme.typography.bodySmall,
//                    color = SecondaryTextColor
//                )
//            }

        }
    }
}


@Composable
@Preview
fun ManuScriptTopRankingItemPreview() {
    YBTheme {

    }

}
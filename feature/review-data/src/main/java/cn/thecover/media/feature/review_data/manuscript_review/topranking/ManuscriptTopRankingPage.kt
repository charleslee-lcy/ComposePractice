package cn.thecover.media.feature.review_data.manuscript_review.topranking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import cn.thecover.media.core.widget.component.ItemScoreRow
import cn.thecover.media.core.widget.component.YBNormalList
import cn.thecover.media.core.widget.component.picker.DateType
import cn.thecover.media.core.widget.component.picker.YBDatePicker
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.feature.review_data.ReviewDataViewModel
import cn.thecover.media.feature.review_data.basic_widget.intent.ReviewDataIntent
import cn.thecover.media.feature.review_data.basic_widget.widget.DataItemCard
import cn.thecover.media.feature.review_data.basic_widget.widget.DataItemDropMenuView
import cn.thecover.media.feature.review_data.basic_widget.widget.DataItemRankingRow
import cn.thecover.media.feature.review_data.basic_widget.widget.DataItemSelectionView
import cn.thecover.media.feature.review_data.basic_widget.widget.ManuScriptItemHeader
import cn.thecover.media.feature.review_data.data.entity.ManuscriptReviewDataEntity
import java.time.LocalDate

/**
 *  Created by Wing at 09:42 on 2025/8/6
 *  稿件TOP排行页
 */

@Composable
fun ManuscriptTopRankingPage(viewModel: ReviewDataViewModel = hiltViewModel()) {

    val data by viewModel.manuscriptTopRankingState.collectAsState()
    // 创建 MutableState 用于列表组件
    val manus = remember { mutableStateOf(data.manuscripts) }
    val isLoadingMore = remember { mutableStateOf(data.isLoading) }
    val isRefreshing = remember { mutableStateOf(data.isRefreshing) }
    val canLoadMore = remember { mutableStateOf(true) }

    // 使用 LaunchedEffect 监听 StateFlow 变化并同步到 MutableState
    LaunchedEffect(data) {
        manus.value = data.manuscripts
        isLoadingMore.value = data.isLoading
        isRefreshing.value = data.isRefreshing
    }


    YBNormalList(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        items = manus,
        isLoadingMore = isLoadingMore,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        isRefreshing = isRefreshing,
        canLoadMore = canLoadMore,
        header = {
            ManuscriptTopRankingHeader(viewModel)
        },
        onLoadMore = {
            viewModel.handleReviewDataIntent(ReviewDataIntent.LoadMoreManuscriptReviewData)
        },
        onRefresh = {
            viewModel.handleReviewDataIntent(ReviewDataIntent.RefreshManuscriptReviewData)
        }
    ) { item, index ->
        ManuscriptTopRankingItem(num = index + 1, data = data.manuscripts[index])

    }
}


/**
 * 稿件排行榜项的可组合函数
 * 用于显示单个稿件在排行榜中的信息，包括排名、标题、作者、编辑和各项评分数据
 *
 * @param num 排名序号，用于显示在排行榜卡片上的排名标识
 * @param data 稿件评审数据实体，包含标题、作者、编辑和各项评分信息
 */
@Composable
private fun ManuscriptTopRankingItem(num: Int, data: ManuscriptReviewDataEntity) {
    // 使用排名数据卡片包装内容
    DataItemCard {
        DataItemRankingRow(ranking = num) {
            Column(
                modifier = Modifier,
            ) {
                // 显示稿件头部信息（标题、作者、编辑）
                ManuScriptItemHeader(title = data.title, author = data.author, editor = data.editor)
                Spacer(Modifier.height(8.dp))
                // 显示稿件的各项评分数据行
                ItemScoreRow(
                    items = arrayOf(
                        Pair("总分", data.score.toString()),
                        Pair("基础分", data.basicScore.toString()),
                        Pair("传播分", data.diffusionScore.toString()),
                        Pair("质量分", data.qualityScore.toString()),
                    )
                )
            }

        }
    }
}


/**
 * 稿件排行榜顶部筛选栏的可组合函数
 *
 * 该函数显示一个包含排序指数和时间筛选的卡片组件，用户可以通过下拉菜单选择排序方式，
 * 通过日期选择器选择目标月份来查看对应时间段的排行榜数据。
 *
 */
@Composable
private fun ManuscriptTopRankingHeader(viewModel: ReviewDataViewModel) {
    val currentDate = LocalDate.now()
    val currentMonthText = "${currentDate.year}年${currentDate.monthValue}月"

    var showDatePicker by remember { mutableStateOf(false) }
    var datePickedText by remember { mutableStateOf(currentMonthText) }
    val selectFilterChoice = remember { mutableStateOf("质量分") }

    LaunchedEffect(datePickedText, selectFilterChoice) {
        viewModel.handleReviewDataIntent(ReviewDataIntent.RefreshManuscriptTopRanking)
    }
    // 显示筛选条件卡片，包含排序指数下拉菜单和时间选择器
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

    // 月份选择器弹窗，用于选择目标月份
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
@Preview()
fun ManuscriptTopRankingPagePreview() {
    YBTheme {
        ManuscriptTopRankingPage(ReviewDataViewModel(SavedStateHandle()))
    }

}

@Composable
@Preview()
fun ManuscriptTopRankingPreview() {
    YBTheme {
        ManuscriptTopRankingItem(
            1, ManuscriptReviewDataEntity(
                title = "2025年12月份的云南省让“看一种云南生活”富饶世界云南生活富饶世界",
                author = "张明明",
                editor = "李华",
                score = 22,
                basicScore = 3,
                qualityScore = 4,
                diffusionScore = 5
            )
        )
    }
}
 
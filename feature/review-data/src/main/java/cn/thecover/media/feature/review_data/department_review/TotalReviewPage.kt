package cn.thecover.media.feature.review_data.department_review

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import cn.thecover.media.core.widget.component.PrimaryItemScoreRow
import cn.thecover.media.core.widget.component.ScoreItemType
import cn.thecover.media.core.widget.component.YBNormalList
import cn.thecover.media.core.widget.component.YBToast
import cn.thecover.media.core.widget.component.picker.DateType
import cn.thecover.media.core.widget.component.picker.YBDatePicker
import cn.thecover.media.core.widget.component.showToast
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.feature.review_data.PreviewReviewDataViewModelFactory
import cn.thecover.media.feature.review_data.ReviewDataViewModel
import cn.thecover.media.feature.review_data.basic_widget.intent.ReviewDataIntent
import cn.thecover.media.feature.review_data.basic_widget.intent.ReviewUIIntent
import cn.thecover.media.feature.review_data.basic_widget.widget.DataItemCard
import cn.thecover.media.feature.review_data.basic_widget.widget.DataItemDropMenuView
import cn.thecover.media.feature.review_data.basic_widget.widget.DataItemRankingRow
import cn.thecover.media.feature.review_data.basic_widget.widget.DataItemSelectionView
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
    modifier: Modifier = Modifier,
    viewmodel: ReviewDataViewModel = hiltViewModel<ReviewDataViewModel>()
) {
    val depart by viewmodel.departmentReviewPageState.collectAsState()

    // 创建列表状态，用于控制滚动位置
    val listState = rememberLazyListState()

    // 创建部门数据列表
    var snackBarHostState  by remember { mutableStateOf(SnackbarHostState()) }
    // 使用部门总数据排行页面专用toast
    val toastMessage by viewmodel.departmentReviewToastState.collectAsState()
    // 创建 MutableState 用于列表组件
    val departmentList = remember { mutableStateOf(depart.dataList ?: emptyList()) }
    val isLoadingMore = remember { mutableStateOf(depart.isLoading) }
    val isRefreshing = remember { mutableStateOf(depart.isRefreshing) }
    val canLoadMore = remember { mutableStateOf(depart.hasNextPage) }

    // 使用 LaunchedEffect 监听 StateFlow 变化并同步到 MutableState
    LaunchedEffect(depart) {
        departmentList.value = depart.dataList ?: emptyList()
        isLoadingMore.value = depart.isLoading
        isRefreshing.value = depart.isRefreshing
        canLoadMore.value = depart.hasNextPage

        // 刷新时滚动到顶部
        if (depart.isRefreshing || (depart.dataList != null && depart.dataList!!.isNotEmpty())) {
            listState.animateScrollToItem(0)
        }
    }

    // 监听Toast消息
    LaunchedEffect(toastMessage.time) {
        if (toastMessage.message.isNotEmpty()) {
            snackBarHostState.showToast(toastMessage.message)
        }
    }


    YBNormalList(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        items = departmentList,
        listState = listState,
        isLoadingMore = isLoadingMore,
        isRefreshing = isRefreshing,
        canLoadMore = canLoadMore,
        onLoadMore = {
            viewmodel.handleReviewDataIntent(
                ReviewDataIntent.LoadMoreDepartmentReviewData
            )
        },
        onRefresh = {
            viewmodel.handleReviewDataIntent(
                ReviewDataIntent.RefreshDepartmentReviewData
            )
        },
        header = {
            DepartmentTotalHeader(viewmodel)
        }
    ) { it, index ->
        DepartmentReviewItem(
            index + 1, // 使用本地计算的排名，从1开始
            it.departmentName,
            it.totalPayment,
            it.totalPersons,
            it.totalScore,
            it.averageScore,
            viewmodel.departmentDataFilterState.collectAsState().value.sortField
        )
    }

    YBToast(snackBarHostState = snackBarHostState)
}


/**
 * 部门总稿费头部筛选组件
 *
 * 该函数创建一个可组合的头部筛选区域，包含排序指数下拉选择和时间月份选择功能。
 * 用户可以通过下拉菜单选择不同的排序方式，通过日期选择器选择目标月份。
 *
 */
@Composable
private fun DepartmentTotalHeader(
    viewModel: ReviewDataViewModel,

    ) {

    val filterState by viewModel.departmentDataFilterState.collectAsState()

    var showDatePicker by remember { mutableStateOf(false) }

    val selectFilterChoice = remember(filterState) {
        mutableStateOf(filterState.sortField)
    }

    // 当用户更改选择时更新 ViewModel
    LaunchedEffect(filterState) {
        // 确保只有当值发生变化时才发送 intent
        viewModel.handleReviewDataIntent(ReviewDataIntent.RefreshDepartmentReviewData)
    }

    LaunchedEffect(selectFilterChoice.value) {
        if (selectFilterChoice.value != filterState.sortField) {
            viewModel.handleUIIntent(
                ReviewUIIntent.UpdateDepartmentDataFilter(
                    selectFilterChoice.value,
                    filterState.selectedDate
                )
            )
        }
    }

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
                DataItemDropMenuView(
                    data = selectFilterChoice,
                    dataList = listOf("部门总稿费", "部门总人数", "部门人员平均分", "部门总分")
                )
            }
            Spacer(Modifier.width(12.dp))

            // 右侧时间选择区域
            Column(modifier = Modifier.weight(1f)) {
                Text("时间", style = MaterialTheme.typography.labelMedium)
                Spacer(Modifier.height(8.dp))
                DataItemSelectionView(label = filterState.selectedDate, onClick = {
                    showDatePicker = true
                })
            }
        }
    }

    // 解析当前选中的日期字符串，转换为LocalDate对象
    val selectedDate = try {
        // 日期格式为 "yyyy年M月"，例如 "2025年8月"
        val regex = """(\d{4})年(\d{1,2})月""".toRegex()
        val matchResult = regex.find(filterState.selectedDate)
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

    // 月份选择器弹窗组件
    YBDatePicker(
        visible = showDatePicker,
        type = DateType.MONTH,
        onCancel = { showDatePicker = false },
        end = LocalDate.now().plusYears(10),
        start = LocalDate.of(2025, 1, 1),
        value = selectedDate,
        onChange = {
            viewModel.handleUIIntent(
                ReviewUIIntent.UpdateDepartmentDataFilter(
                    selectFilterChoice.value,
                    "${it.year}年${it.monthValue}月"
                )
            )
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
    totalPayment: Double = 0.0,
    totalPersonNumber: Int = 0,
    totalScore: Double = 0.0,
    averageScore: Double = 0.0,
    filterText: String = ""
) {
    // 显示部门排名卡片
    DataItemCard {
        DataItemRankingRow(ranking = ranking) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // 显示部门名称
                Text(name, color = MainTextColor, style = MaterialTheme.typography.titleMedium)
                // 显示评审数据项得分行，包含总稿费、总人数、人员平均分和总分
                PrimaryItemScoreRow(
                    items = arrayOf(
                        Triple(
                            "部门总稿费",
                            if (totalPayment % 1 == 0.0) totalPayment.toInt()
                                .toString() else totalPayment.toString(),
                            if (filterText.contains("总稿费")) ScoreItemType.PRIMARY_WITH_BORDER else ScoreItemType.PRIMARY
                        ),
                        Triple(
                            "总人数",
                            totalPersonNumber.toString(),
                            if (filterText.contains("总人数")) ScoreItemType.NORMAL_WITH_BORDER else ScoreItemType.NORMAL
                        ),


                        )
                )
                PrimaryItemScoreRow(
                    items = arrayOf(

                        Triple(
                            "人员平均分",
                            if (averageScore % 1 == 0.0) averageScore.toInt()
                                .toString() else averageScore.toString(),
                            if (filterText.contains("人员平均分")) ScoreItemType.NORMAL_WITH_BORDER else ScoreItemType.NORMAL
                        ),
                        Triple(
                            "总分",
                            if (totalScore % 1 == 0.0) totalScore.toInt()
                                .toString() else totalScore.toString(),
                            if (filterText.contains("总分")) ScoreItemType.NORMAL_WITH_BORDER else ScoreItemType.NORMAL
                        ),

                        )
                )
            }
        }

    }
}


@Composable
@Preview(showSystemUi = true)
fun DepartmentReviewScreenPreview() {
    // 手动创建ViewModel实例，用于预览

    YBTheme {
        DepartmentReviewScreen(viewmodel = viewModel(
            factory = PreviewReviewDataViewModelFactory()
        ))
    }
}


@Composable
@Preview
fun DepartmentReviewItemPreview() {
    YBTheme {
        DepartmentReviewItem(1, "经济部")
    }
}



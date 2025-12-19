package cn.thecover.media.feature.review_data.manuscript_review

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastCoerceAtLeast
import androidx.lifecycle.viewmodel.compose.viewModel
import cn.thecover.media.core.data.ManuscriptReviewDataEntity
import cn.thecover.media.core.widget.R
import cn.thecover.media.core.widget.component.YBButton
import cn.thecover.media.core.widget.component.YBInput
import cn.thecover.media.core.widget.component.YBNormalList
import cn.thecover.media.core.widget.component.YBToast
import cn.thecover.media.core.widget.component.picker.DateType
import cn.thecover.media.core.widget.component.picker.YBDatePicker
import cn.thecover.media.core.widget.component.popup.YBAlignDropdownMenu
import cn.thecover.media.core.widget.component.popup.YBDialog
import cn.thecover.media.core.widget.component.search.FilterSearchTextField
import cn.thecover.media.core.widget.event.clickableWithoutRipple
import cn.thecover.media.core.widget.icon.YBIcons
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.PageBackgroundColor
import cn.thecover.media.core.widget.theme.SecondaryTextColor
import cn.thecover.media.core.widget.theme.TertiaryTextColor
import cn.thecover.media.core.widget.theme.YBShapes
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.feature.review_data.PreviewReviewDataViewModelFactory
import cn.thecover.media.feature.review_data.ReviewDataViewModel
import cn.thecover.media.feature.review_data.basic_widget.ReviewDataImages
import cn.thecover.media.feature.review_data.basic_widget.intent.ReviewDataIntent
import cn.thecover.media.feature.review_data.basic_widget.intent.ReviewUIIntent
import cn.thecover.media.feature.review_data.basic_widget.widget.DataItemCard
import cn.thecover.media.feature.review_data.basic_widget.widget.DataItemDropMenuView
import cn.thecover.media.feature.review_data.basic_widget.widget.DataItemRankingRow
import cn.thecover.media.feature.review_data.basic_widget.widget.DataItemSelectionView
import cn.thecover.media.feature.review_data.basic_widget.widget.ExpandItemColumn
import cn.thecover.media.feature.review_data.basic_widget.widget.ManuScriptItemHeader
import java.time.LocalDate

/**
 *  Created by Wing at 10:26 on 2025/8/6
 *  稿件总分排行
 */

@Composable
internal fun ManuscriptReviewPage(
    modifier: Modifier = Modifier,
    viewModel: ReviewDataViewModel
) {
    val data by viewModel.manuscriptReviewPageState.collectAsState()
    // 计算splitsNum为data.dataList中第一个score等于0的数据的索引
    val splitsNum = remember(data.dataList) {
        if (viewModel.manuscriptReviewFilterState.value.sortField.contains("分割线以下")) 0 else data.dataList?.indexOfFirst { it.id.toLong() == data.firstCutNewsId }
            ?.let {
            if (it == -1) data.dataList?.size ?: 0 else it
        } ?: 0
    }

    val showEditScorePop = remember { mutableStateOf(false) }

    var editId by remember { mutableIntStateOf(0) }
    // 创建 MutableState 用于列表组件
    val manus = remember { mutableStateOf(data.dataList ?: emptyList()) }
    val isLoadingMore = remember { mutableStateOf(data.isLoading) }
    val isRefreshing = remember { mutableStateOf(data.isRefreshing) }
    val canLoadMore = remember { mutableStateOf(true) }
    // 使用 LaunchedEffect 监听 StateFlow 变化并同步到 MutableState
    LaunchedEffect(data) {
        manus.value = data.dataList ?: emptyList()
        isLoadingMore.value = data.isLoading
        isRefreshing.value = data.isRefreshing
        canLoadMore.value = data.hasNextPage
    }

    YBNormalList(
        modifier = modifier
            .fillMaxWidth(),
        items = manus,
        isLoadingMore = isLoadingMore,
        isRefreshing = isRefreshing,

        canLoadMore = canLoadMore,
        header = {
            Column(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background)
            ) {
                ManuscriptTotalRankingHeader(viewModel = viewModel)
                Text(
                    text = buildAnnotatedString {
                        append("共 ")
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                            append(data.total.toString())
                        }
                        append(" 条记录")
                    },
                    style = MaterialTheme.typography.labelMedium,
                    color = TertiaryTextColor,
                    modifier = Modifier
                        .padding(start = 24.dp, top = 8.dp)

                )
            }
        },
        onLoadMore = {
            viewModel.handleReviewDataIntent(ReviewDataIntent.LoadMoreManuscriptReviewData)
        },
        onRefresh = {
            viewModel.handleReviewDataIntent(ReviewDataIntent.RefreshManuscriptReviewData)
        }
    ) { item, index ->
        val itemBg = when (index) {
            in 0..<splitsNum -> MaterialTheme.colorScheme.background
            splitsNum -> Color.Transparent
            else -> Color(0xFFF5F8FF)
        }
        Spacer(modifier = Modifier.height(if (index == splitsNum) 36.dp else 0.dp))
        Box {
            if (index == splitsNum) {
                Image(
                    painter = painterResource(ReviewDataImages.Background.ManuSplitsLine),
                    contentDescription = "分割线背景",
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(y = (-32).dp),
                    contentScale = ContentScale.FillBounds
                )
            }
            Column(
                modifier = Modifier
                    .background(color = itemBg)
                    .padding(horizontal = 12.dp)
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                TotalRankingItem(index + 1, rankLine = splitsNum + 1, item, onItemClick = {
                    showEditScorePop.value = true
                    editId = it
                })
            }

        }
    }
    var textFiledState by remember { mutableStateOf("") }

    LaunchedEffect(showEditScorePop) {
        if (showEditScorePop.value) {
            val item = manus.value.firstOrNull { it.id == editId }
            if (item != null) {
                textFiledState = item.score.toString()
            }
        }
    }
    YBDialog (
        dialogState = showEditScorePop,
        title = "修改稿分",
        onConfirm = {
            showEditScorePop.value = false

            if (textFiledState.isNotEmpty()) {
                viewModel.handleReviewDataIntent(
                    ReviewDataIntent.EditManuscriptScore(
                        editId,
                        textFiledState.toDouble()
                    )
                )
            }
            textFiledState = ""
        },
        content = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                YBInput(
                    text = textFiledState,
                    onValueChange = {
                        textFiledState = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .border(
                            0.5.dp,
                            color = MaterialTheme.colorScheme.outlineVariant,
                            shape = YBShapes.medium
                        )
                        .background(
                            PageBackgroundColor,
                            shape = YBShapes.medium
                        ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    hint = "请输入稿分",
                    maxLines = 1,
                    textStyle = TextStyle(

                        fontSize = 14.sp, color = MainTextColor
                    ),
                    contentAlignment = Alignment.CenterStart,
                    contentPadding = 8.dp
                )
            }
        },
        onDismissRequest = {
            showEditScorePop.value = false
            textFiledState = ""
        }
    )

    val snackBarHostState = remember { SnackbarHostState() }
    YBToast(snackBarHostState)

    val toastState by viewModel.iconTipsDialogState.collectAsState()
    LaunchedEffect(toastState.time) {
        if (toastState.message.isNotEmpty()) {
            snackBarHostState.showSnackbar(toastState.message)
        }
    }



}

@Composable
private fun TotalRankingItem(
    rank: Int,
    rankLine: Int,
    data: ManuscriptReviewDataEntity,
    onItemClick: (Int) -> Unit
) {
    DataItemCard(
        containerColor = if (rank < rankLine) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.tertiaryContainer
    ) {
        DataItemRankingRow(
            ranking = rank,
        ) {
            ExpandItemColumn(
                offset = -12,
                content = {
                    Column {
                        // 显示稿件头部信息：标题、作者、编辑
                        ManuScriptItemHeader(
                            title = data.title,
                            author = data.reporter.joinToString(", ") { it.name },
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                if (data.score % 1 == 0.0) data.score.toInt()
                                    .toString() else data.score.toString() + if (rank >= rankLine) "(0)" else "",
                                style = MaterialTheme.typography.titleLarge,
                                color = if (rank >= rankLine) MaterialTheme.colorScheme.primary.copy(
                                    0.6f
                                ) else MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.width(8.dp))
                            AnimatedVisibility(data.leaderScoreModified) {
                                Text(
                                    "改",
                                    modifier = Modifier
                                        .background(
                                            shape = MaterialTheme.shapes.extraSmall,
                                            color = MaterialTheme.colorScheme.error.copy(0.1f)
                                        )
                                        .size(16.dp)
                                        .padding(top = 2.dp),
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.labelSmall,
                                    textAlign = TextAlign.Center
                                )
                            }

                            Spacer(modifier = Modifier.weight(1f))
                            YBButton(
                                content = {
                                    Text(
                                        "修改稿分",
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                },
                                onClick = {
                                    onItemClick.invoke(data.id)
                                },

                                )
                        }
                    }
                },
                foldContent = {
                    ItemFoldedView(
                        basicScore = data.basicScore,
                        qualityScore = data.qualityScore,
                        diffusionScore = data.diffusionScore
                    )
                },
                expandIconRes = if (rank < rankLine) YBIcons.Custom.Expand else YBIcons.Custom.ExpandVariant
            )
        }
    }
}

@Composable
private fun ItemFoldedView(
    basicScore: Double = 0.0,
    qualityScore: Double = 0.0,
    diffusionScore: Double = 0.0
) {
    Column {
        HorizontalDivider(
            modifier = Modifier
                .padding(bottom = 15.dp)
                .fillMaxWidth()
                .height(0.5.dp),
            color = MaterialTheme.colorScheme.outline
        )

        Row(verticalAlignment = Alignment.Bottom) {
            Text("基础分", style = MaterialTheme.typography.bodySmall, color = SecondaryTextColor)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                if (basicScore % 1 == 0.0) basicScore.toInt().toString() else basicScore.toString(),
                style = MaterialTheme.typography.titleSmall,
                color = MainTextColor.copy(0.5f)
            )

            Spacer(modifier = Modifier.weight(1f))
            Text("质量分", style = MaterialTheme.typography.bodySmall, color = SecondaryTextColor)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                if (qualityScore % 1 == 0.0) qualityScore.toInt()
                    .toString() else qualityScore.toString(),
                style = MaterialTheme.typography.titleSmall,
                color = MainTextColor.copy(0.5f)
            )
            Spacer(modifier = Modifier.weight(1f))

            Text("传播分", style = MaterialTheme.typography.bodySmall, color = SecondaryTextColor)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                if (diffusionScore % 1 == 0.0) diffusionScore.toInt()
                    .toString() else diffusionScore.toString(),
                style = MaterialTheme.typography.titleSmall,
                color = MainTextColor.copy(0.5f)
            )

        }

    }
}


/**
 * 稿件总排行筛选头部组件。
 *
 * 该 Composable 函数用于展示和操作稿件传播数据的筛选条件，包括排序方式、时间范围和关键词搜索。
 * 用户可以通过下拉菜单选择排序指数，通过日期选择器选择月份，并通过搜索框输入关键词进行过滤。
 *
 * 注意：此函数为私有函数，仅在当前文件内使用。
 */
@Composable
private fun ManuscriptTotalRankingHeader(viewModel: ReviewDataViewModel) {

    val filterState by viewModel.manuscriptReviewFilterState.collectAsState()
    // 控制日期选择器弹窗的显示状态
    var showDatePicker by remember { mutableStateOf(false) }

    // 存储用户选择的排序字段
    val selectFilterChoice = remember { mutableStateOf(filterState.sortField) }
    // 存储用户选择的搜索字段
    val selectSearchChoice = remember { mutableStateOf(filterState.searchField) }


    LaunchedEffect(filterState) {
        viewModel.handleReviewDataIntent(ReviewDataIntent.RefreshManuscriptReviewData)
    }

    LaunchedEffect(selectFilterChoice.value) {
        if (selectFilterChoice.value != filterState.sortField) {
            viewModel.handleUIIntent(
                ReviewUIIntent.UpdateManuscriptReviewFilter(
                    state = selectFilterChoice.value,
                )
            )
        }
    }
    // 主体内容卡片容器
    DataItemCard {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // 第一行：排序指数选择 和 时间选择
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 排序字段选择区域
                Column(modifier = Modifier.weight(1f)) {
                    Text("稿件排名分割线", style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.height(8.dp))
                    DataItemDropMenuView(
                        data = selectFilterChoice, dataList = listOf(
                            "全部", "分割线以上", "分割线以下(清零)"
                        )
                    )
                }
                Spacer(Modifier.width(12.dp))

                // 时间选择区域
                Column(modifier = Modifier.weight(1f)) {
                    Text("时间", style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.height(8.dp))
                    DataItemSelectionView(label = filterState.selectedDate, onClick = {
                        showDatePicker = true
                    })
                }
            }

            // 搜索输入区域
            FilterSearchTextField(
                data = selectSearchChoice, label = "请输入搜索内容", dataList = listOf(
                    "稿件名称", "稿件 ID", "记者"
                ), onValueChange = { valueType, value ->
                    viewModel.handleUIIntent(
                        ReviewUIIntent.UpdateManuscriptReviewFilter(
                            searchType = valueType,
                            searchText = value
                        )
                    )
                },
                onSearch = { valueType, value ->
                    viewModel.handleUIIntent(
                        ReviewUIIntent.UpdateManuscriptReviewFilter(
                            searchType = valueType,
                            searchText = value
                        )
                    )
                },
            )
        }
    }

    // 日期选择器弹窗组件
    YBDatePicker(
        visible = showDatePicker,
        type = DateType.MONTH,
        onCancel = { showDatePicker = false },
        end = LocalDate.now().plusYears(10),
        start = LocalDate.of(2025, 1, 1),
        value = LocalDate.now().minusMonths(1),
        onChange = {
            viewModel.handleUIIntent(ReviewUIIntent.UpdateManuscriptReviewFilter(time = "${it.year}年${it.monthValue}月"))
        }
    )
}


/**
 * 筛选搜索组合项，包含一个下拉筛选菜单和一个文本输入框。
 *
 * @param dataList 下拉菜单的数据列表，默认为 ["稿件名称", "记者", "稿件ID"]
 * @param data 当前选中的下拉菜单项，使用 MutableState 包装以便响应式更新
 * @param label 输入框的提示文本，默认显示当前选中的下拉项文本
 */
@Composable
private fun FilterSearchView(
    dataList: List<String> = listOf(
        "稿件名称", "稿件 ID", "记者",
    ), data: MutableState<String>, label: String = data.value
) {
    // 控制下拉菜单是否展开的状态
    val showDrop = remember { mutableStateOf(false) }
    // 动画旋转值，用于下拉箭头图标旋转效果
    val animRotate = remember { Animatable(0f) }
    // 文本输入框的内容状态
    val textState = remember { mutableStateOf("") }

    // 当菜单展开状态改变时，触发动画旋转箭头图标
    LaunchedEffect(showDrop.value) {
        animRotate.animateTo(
            targetValue = if (showDrop.value) 180f else 0f,
            animationSpec = tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing
            )
        )
    }

    // 主容器布局：包含下拉菜单和输入框
    Row(
        modifier = Modifier
            .padding(bottom = 2.dp)
            .border(
                width = 0.5.dp,
                shape = MaterialTheme.shapes.extraSmall,
                color = MaterialTheme.colorScheme.outlineVariant
            )
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.background, shape = YBShapes.extraSmall
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 下拉菜单组件，用于选择筛选类型
        YBAlignDropdownMenu(
            data = dataList,
            expanded = showDrop,
            initialIndex = dataList.indexOf(data.value).fastCoerceAtLeast(0),
            isItemWidthAlign = true,
            offset = DpOffset(0.dp, 0.dp),
            onItemClick = { text, index ->
                data.value = text
            }
        ) {
            // 触发下拉菜单展开的点击区域
            Row(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.background,
                        shape = YBShapes.extraSmall
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .clickableWithoutRipple {
                        showDrop.value = !showDrop.value
                    },
                verticalAlignment = Alignment.CenterVertically,

                ) {
                // 显示当前选中的筛选项文本
                Text(
                    data.value,
                    style = MaterialTheme.typography.labelMedium,
                    color = MainTextColor
                )
                Spacer(Modifier.width(4.dp))
                // 下拉箭头图标，根据菜单状态旋转
                Icon(
                    painterResource(R.mipmap.ic_arrow_down),
                    contentDescription = "${label}下拉筛选按钮",
                    tint = TertiaryTextColor,
                    modifier = Modifier.rotate(animRotate.value)
                )

            }

        }

        // 分割线，分隔下拉菜单和输入框
        VerticalDivider(
            modifier = Modifier
                .padding(end = 12.dp)
                .width(0.5.dp)
                .height(20.dp),
            color = MaterialTheme.colorScheme.outline,
        )

        // 文本输入框，用于输入搜索关键词
        BasicTextField(
            value = textState.value,
            onValueChange = { textState.value = it },
            modifier = Modifier.fillMaxWidth(),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Box(modifier = Modifier.weight(1f)) {
                            // 当输入框为空时显示提示文本
                            if (textState.value.isEmpty()) {
                                Text(
                                    label,
                                    color = TertiaryTextColor,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                            // 实际的输入框内容
                            innerTextField()
                        }

                    }
                }
            }
        )
    }
}

@Composable
@Preview(showSystemUi = true)
fun ManuscriptReviewScreenPreview() {
    YBTheme {
        ManuscriptReviewPage(viewModel = viewModel(
            factory = PreviewReviewDataViewModelFactory()
        ))
    }
}

@Composable
@Preview(showSystemUi = false)
fun ManuscriptFoldItemPreview() {
    YBTheme {
        ItemFoldedView()
    }
}
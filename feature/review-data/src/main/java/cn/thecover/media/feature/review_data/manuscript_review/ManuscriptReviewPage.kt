package cn.thecover.media.feature.review_data.manuscript_review

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastCoerceAtLeast
import androidx.lifecycle.viewmodel.compose.viewModel
import cn.thecover.media.core.data.ManuscriptReviewDataEntity
import cn.thecover.media.core.data.UserInfo
import cn.thecover.media.core.widget.R
import cn.thecover.media.core.widget.component.ItemScoreRow
import cn.thecover.media.core.widget.component.YBButton
import cn.thecover.media.core.widget.component.YBInput
import cn.thecover.media.core.widget.component.YBNormalList
import cn.thecover.media.core.widget.component.YBToast
import cn.thecover.media.core.widget.component.picker.DateType
import cn.thecover.media.core.widget.component.picker.YBDatePicker
import cn.thecover.media.core.widget.component.popup.YBAlignDropdownMenu
import cn.thecover.media.core.widget.component.popup.YBDialog
import cn.thecover.media.core.widget.component.search.FilterSearchTextField
import cn.thecover.media.core.widget.datastore.Keys
import cn.thecover.media.core.widget.datastore.rememberDataStoreState
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
import com.google.gson.Gson
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
    val focusManager = LocalFocusManager.current
    val data by viewModel.manuscriptReviewPageState.collectAsState()
    val filterState by viewModel.manuscriptReviewFilterState.collectAsState()

    // 创建列表状态，用于控制滚动位置
    val listState = rememberLazyListState()

    // 观察稿分修改操作的状态
    val editScoreSuccess by viewModel.editManuscriptScoreSuccess.collectAsState()
    val userInfoJson = rememberDataStoreState(Keys.USER_INFO, "")
    val showEditScorePop = remember { mutableStateOf(false) }
    // 弹窗内的错误提示信息
    var errorMessage by remember { mutableStateOf<String?>(null) }
    // 数据计算运行中弹窗状态
    val showBudgetCutProcessDialog = remember { mutableStateOf(false) }
    // 文本输入框的状态
    var textFiledState by remember { mutableStateOf("") }
    val showModifyState = remember { mutableStateOf(false) }
    LaunchedEffect(userInfoJson) {
        val userInfo = Gson().fromJson(userInfoJson, UserInfo::class.java)
        showModifyState.value = userInfo?.hasModifyNewsScoreAuth == true
    }
    var editId by remember { mutableIntStateOf(0) }
    // 创建 MutableState 用于列表组件
    val manus = remember { mutableStateOf(data.dataList ?: emptyList()) }
    val isLoadingMore = remember { mutableStateOf(data.isLoading) }
    val isRefreshing = remember { mutableStateOf(data.isRefreshing) }
    val canLoadMore = remember { mutableStateOf(true) }
    val splitsNum = remember { mutableIntStateOf(0) }
    // 记录上一次是否在刷新状态，用于检测刷新完成
    val wasRefreshing = remember { mutableStateOf(false) }

    // 单独监听刷新完成事件，显示 budgetCutProcess 弹窗
    LaunchedEffect(data.isRefreshing) {
        // 当刷新状态从 true 变为 false 时，说明刷新完成
        if (wasRefreshing.value && !data.isRefreshing) {
            // 确保不是加载更多（加载更多时 isLoading 为 true）
            if (!data.isLoading && data.budgetCutProcess) {
                showBudgetCutProcessDialog.value = true
            }
        }
        // 更新刷新状态
        wasRefreshing.value = data.isRefreshing
    }

    // 使用 LaunchedEffect 监听 StateFlow 变化并同步到 MutableState
    LaunchedEffect(data, filterState) {
        manus.value = data.dataList ?: emptyList()
        isLoadingMore.value = data.isLoading
        isRefreshing.value = data.isRefreshing
        canLoadMore.value = data.hasNextPage

        // 更新分割线位置
        splitsNum.intValue = if (filterState.sortField.contains("分割线以下")) {
            0
        } else {
            if (data.dataList?.indexOfFirst { it.isCutNews } == -1) {
                data.dataList?.size ?: 0
            } else {
                data.dataList?.indexOfFirst { it.isCutNews } ?: 0
            }
        }

        // 刷新时滚动到顶部
        if (data.isRefreshing || (data.dataList != null && data.dataList!!.isNotEmpty())) {
            listState.animateScrollToItem(0)
        }
    }

    YBNormalList(
        modifier = modifier
            .fillMaxWidth()
            .clickable { focusManager.clearFocus() },
        items = manus,
        listState = listState,
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
            in 0..<splitsNum.intValue -> MaterialTheme.colorScheme.background
            splitsNum.intValue -> Color.Transparent
            else -> Color(0xFFF5F8FF)
        }
        Spacer(modifier = Modifier.height(if (index == splitsNum.intValue) 36.dp else 0.dp))
        Box {
            if (index == splitsNum.intValue) {
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

                TotalRankingItem(
                    index + 1,
                    rankLine = (splitsNum.intValue + 1),
                    item,
                    showModifyState.value,
                    onItemClick = {
                    // 先清除错误信息，再打开弹窗
                    errorMessage = null
                    editId = it
                    // 立即设置文本框的值为当前稿分
                    val currentItem = manus.value.firstOrNull { it.id == editId }
                    if (currentItem != null) {
                        textFiledState = if (currentItem.score % 1 == 0.0) {
                            currentItem.score.toInt().toString()
                        } else {
                            currentItem.score.toString()
                        }
                    }
                    showEditScorePop.value = true
                })
            }

        }
    }

    LaunchedEffect(showEditScorePop) {
        if (showEditScorePop.value) {
            // 只有在文本框为空时才设置默认值，避免覆盖用户可能的输入
            if (textFiledState.isEmpty()) {
                val item = manus.value.firstOrNull { it.id == editId }
                if (item != null) {
                    textFiledState = if (item.score % 1 == 0.0) {
                        item.score.toInt().toString()
                    } else {
                        item.score.toString()
                    }
                }
            }
            // 清除错误信息，确保每次打开弹窗时都是干净的
            errorMessage = null
        } else {
            // 当弹窗关闭时，也清除错误信息
            errorMessage = null
        }
    }

    // 监听稿分修改操作的结果
    LaunchedEffect(editScoreSuccess) {
        // 只有在弹窗打开且editScoreSuccess不为null时才处理
        if (showEditScorePop.value && editScoreSuccess != null) {
            if (editScoreSuccess == true) {
                // 修改成功，关闭弹窗并清空文本框
                showEditScorePop.value = false
                textFiledState = ""
            } else {
                // 修改失败，显示错误信息
                val toastState = viewModel.iconTipsDialogState.value
                errorMessage = toastState.message
            }
            // 无论是成功还是失败，都重置状态
            viewModel.resetEditScoreSuccess()
        }
    }
    YBDialog (
        dialogState = showEditScorePop,
        handleConfirmDismiss = true,
        title = "修改稿分",
        onConfirm = {
            if (textFiledState.isNotEmpty()) {
                try {
                    // 确保输入可以正确转换为Double类型
                    val score = if (textFiledState.endsWith(".")) {
                        // 如果以小数点结尾，添加0
                        "${textFiledState}0".toDouble()
                    } else {
                        textFiledState.toDouble()
                    }
                    viewModel.handleReviewDataIntent(
                        ReviewDataIntent.EditManuscriptScore(
                            editId,
                            score
                        )
                    )
                } catch (e: NumberFormatException) {
                    // 处理可能的数字格式异常
                    viewModel.handleReviewDataIntent(
                        ReviewDataIntent.EditManuscriptScore(
                            editId,
                            0.0
                        )
                    )
                }
            } else {
                // 如果输入为空，显示错误提示
                errorMessage = "请输入有效的稿分"
            }
            // 不立即关闭弹窗，等待接口返回结果后再关闭
            // 不在这里清空textFiledState，只在成功时才清空
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
                        // 处理小数输入，限制为小数点后两位
                        if (it.isEmpty()) {
                            textFiledState = ""
                        } else if (it == ".") {
                            // 单独的小数点，转换为"0."
                            textFiledState = "0."
                        } else if (it.matches(Regex("^\\d+\\.\\d{0,2}$")) || it.matches(Regex("^\\d+$"))) {
                            // 验证是否为有效的数字格式（最多两位小数）
                            textFiledState = it
                        } else if (it.contains(".")) {
                            // 如果包含小数点，但格式不正确，截取小数点后两位
                            val parts = it.split(".")
                            if (parts.size >= 2) {
                                val integerPart = parts[0].ifEmpty { "0" }
                                val decimalPart = parts[1].take(2)
                                textFiledState = "$integerPart.$decimalPart"
                            } else {
                                textFiledState = parts[0]
                            }
                        } else {
                            // 其他情况只保留数字
                            val digitsOnly = it.filter { it.isDigit() }
                            if (digitsOnly.isNotEmpty()) {
                                textFiledState = digitsOnly
                            }
                        }
                        // 用户输入时清除错误信息
                        errorMessage = null
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
                    keyboardOptions = KeyboardOptions.Default,
                    hint = "请输入稿分",
                    maxLines = 1,
                    textStyle = TextStyle(

                        fontSize = 14.sp, color = MainTextColor
                    ),
                    contentAlignment = Alignment.CenterStart,
                    contentPadding = 8.dp
                )

                // 显示错误信息
                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        onDismissRequest = {
            showEditScorePop.value = false
            // 不立即清空文本框，保留用户可能需要重新编辑的值
            errorMessage = null
            // 重置编辑状态
            viewModel.resetEditScoreSuccess()
        }
    )

    val snackBarHostState = remember { SnackbarHostState() }
    YBToast(snackBarHostState)

    // 数据计算运行中弹窗
    YBDialog(
        dialogState = showBudgetCutProcessDialog,
        handleConfirmDismiss = true,
        title = "数据计算运行中...",
        onConfirm = {
            showBudgetCutProcessDialog.value = false
        },
        content = {
            Text(
                text = "清零（被一刀切）数据暂不展示，请稍后再试",
                style = MaterialTheme.typography.bodyMedium,
                color = SecondaryTextColor
            )
        },
        onDismissRequest = {
            showBudgetCutProcessDialog.value = false
        },
        cancelText = null
    )

    // 使用稿件总排行页面专用toast
    val toastState by viewModel.manuscriptReviewToastState.collectAsState()
    // 记录上一次显示的toast消息和时间
    var lastShownToastTime by remember { mutableStateOf(0L) }
    LaunchedEffect(toastState.time) {
        val currentTime = System.currentTimeMillis()
        if (toastState.message.isNotEmpty() &&
            toastState.time != 0L &&
            toastState.time != lastShownToastTime &&
            currentTime - toastState.time <= 5000
        ) {
            snackBarHostState.showSnackbar(toastState.message)
            lastShownToastTime = toastState.time
        }
    }



}

@Composable
private fun TotalRankingItem(
    rank: Int,
    rankLine: Int,
    data: ManuscriptReviewDataEntity,
    showModify: Boolean,
    onItemClick: (Int) -> Unit,

) {
    // 控制展开/折叠状态
    var isExpanded by remember { mutableStateOf(false) }
    LaunchedEffect(data) {
        isExpanded = false
    }
    DataItemCard(
        containerColor = if (rank < rankLine) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.tertiaryContainer
    ) {
        DataItemRankingRow(
            ranking = rank,
        ) {
            ExpandItemColumn(
                offset = -12,
                expand = isExpanded,
                onExpandChange = { isExpanded = it },
                content = {
                    Column {
                        // 显示稿件头部信息：标题、作者、编辑
                        ManuScriptItemHeader(
                            title = data.title,
                            author = data.reporter.joinToString("、 ") { it.name },
                            editor = data.editor.joinToString("、 ") { it.name }
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
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .background(
                                            shape = MaterialTheme.shapes.extraSmall,
                                            color = MaterialTheme.colorScheme.error.copy(0.1f)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "改",
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.labelSmall,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.weight(1f))
                            if (showModify) {
                                YBButton(
                                    shape = RoundedCornerShape(2.dp),
                                    modifier = Modifier.padding(top = 4.dp),
                                    contentPadding = PaddingValues(
                                        horizontal = 12.dp,
                                        vertical = 8.5.dp
                                    ),
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
                    }
                },
                foldContent = {
                    ItemFoldedView(
                        addSubScore = data.addSubScore,
                        basicScore = data.basicScore,
                        qualityScore = data.qualityScore,
                        diffusionScore = data.diffusionScore,
                        (rank >= rankLine)
                    )
                },
                expandIconRes = if (rank < rankLine) YBIcons.Custom.Expand else YBIcons.Custom.ExpandVariant
            )
        }
    }
}

@Composable
private fun ItemFoldedView(
    addSubScore: Double = 0.0,
    basicScore: Double = 0.0,
    qualityScore: Double = 0.0,
    diffusionScore: Double = 0.0,
    isCut: Boolean = false
) {
    Column {
        HorizontalDivider(
            modifier = Modifier
                .padding(bottom = 12.dp)
                .fillMaxWidth()
                .height(0.5.dp),
            color = MaterialTheme.colorScheme.outline
        )

        Row(verticalAlignment = Alignment.Bottom) {
            ItemScoreRow(
                modifier = Modifier,
                backgroundColor = if (isCut) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.surface,
                items = arrayOf(
                    Pair(
                        "稿件加减分",
                        if (addSubScore % 1 == 0.0) addSubScore.toInt()
                            .toString() else addSubScore.toString()
                    ),
                    Pair(
                        "基础分",
                        if (basicScore % 1 == 0.0) basicScore.toInt()
                            .toString() else basicScore.toString()
                    ),
                    Pair(
                        "质量分",
                        if (qualityScore % 1 == 0.0) qualityScore.toInt()
                            .toString() else qualityScore.toString()
                    ),
                    Pair(
                        "传播分",
                        if (diffusionScore % 1 == 0.0) diffusionScore.toInt()
                            .toString() else diffusionScore.toString()
                    )
                )
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


    // 同步状态
    LaunchedEffect(filterState) {
        if (selectFilterChoice.value != filterState.sortField) {
            selectFilterChoice.value = filterState.sortField
        }
        if (selectSearchChoice.value != filterState.searchField) {
            selectSearchChoice.value = filterState.searchField
        }
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
                data = selectSearchChoice,
                initialText = filterState.searchText,
                label = "请输入搜索内容",
                dataList = listOf(
                    "稿件名称", "稿件 ID", "记者"
                ),
                onValueChange = { valueType, value ->
//                    // 过滤掉文本末尾的换行符，防止换行字符被带到接口
                    if (value.isEmpty()) {
                        val filteredValue = value.replace(Regex("[\\r\\n]+"), "")
                        viewModel.handleUIIntent(
                            ReviewUIIntent.UpdateManuscriptReviewFilter(
                                searchType = valueType,
                                searchText = filteredValue
                            )
                        )
                    }
                },
                onSearch = { valueType, value ->
                    // 过滤掉文本末尾的换行符，防止换行字符被带到接口
                    val filteredValue = value.replace(Regex("[\\r\\n]+"), "")
                    viewModel.handleUIIntent(
                        ReviewUIIntent.UpdateManuscriptReviewFilter(
                            searchType = valueType,
                            searchText = filteredValue
                        )
                    )
                },
            )
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

    // 日期选择器弹窗组件
    YBDatePicker(
        visible = showDatePicker,
        type = DateType.MONTH,
        onCancel = { showDatePicker = false },
        end = LocalDate.now().plusYears(10),
        start = LocalDate.of(2025, 1, 1),
        value = selectedDate,
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
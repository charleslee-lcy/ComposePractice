package cn.thecover.media.feature.review_manager.assign

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import cn.thecover.media.core.data.DepartmentAssignListData
import cn.thecover.media.core.network.previewRetrofit
import cn.thecover.media.core.widget.component.YBButton
import cn.thecover.media.core.widget.component.YBInput
import cn.thecover.media.core.widget.component.YBLabel
import cn.thecover.media.core.widget.component.YBNormalList
import cn.thecover.media.core.widget.component.picker.DateType
import cn.thecover.media.core.widget.component.picker.SingleColumnPicker
import cn.thecover.media.core.widget.component.picker.YBDatePicker
import cn.thecover.media.core.widget.component.popup.YBDialog
import cn.thecover.media.core.widget.component.popup.YBPopup
import cn.thecover.media.core.widget.theme.DividerColor
import cn.thecover.media.core.widget.theme.MainColor
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.PageBackgroundColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.core.widget.ui.PhonePreview
import cn.thecover.media.feature.review_manager.ReviewManageViewModel
import cn.thecover.media.feature.review_manager.appeal.FilterSearchBar
import cn.thecover.media.feature.review_manager.appeal.FilterType
import java.time.LocalDate


/**
 * 部门内分配
 * <p> Created by CharlesLee on 2025/7/28
 * 15708478830@163.com
 */
@Composable
internal fun DepartmentAssignScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ReviewManageViewModel = hiltViewModel()
) {
    val isRefreshing = remember { mutableStateOf(false) }
    val isLoadingMore = remember { mutableStateOf(false) }
    val canLoadMore = remember { mutableStateOf(true) }
    val focusManager = LocalFocusManager.current
    val items = remember { mutableStateOf(listOf<DepartmentAssignListData>()) }

    val showAssignDialog = remember { mutableStateOf(false) }
    var checkedItem by remember { mutableStateOf<DepartmentAssignListData?>(null) }

    val departmentListUiState by viewModel.departmentListDataState.collectAsStateWithLifecycle()
    var isFirstLaunch by remember { mutableStateOf(true) }

    LaunchedEffect(viewModel.departYear.intValue) {
        if (isFirstLaunch) {
            // 首次初始化逻辑
            viewModel.getDepartmentList()
            isFirstLaunch = false
        } else {
            // 后续值变化逻辑
            viewModel.getDepartmentAssignRemain()
            viewModel.getDepartmentAssignList(isRefresh = true)
        }
    }

    LaunchedEffect(departmentListUiState) {
        isRefreshing.value = departmentListUiState.isRefreshing
        isLoadingMore.value = departmentListUiState.isLoading
        canLoadMore.value = departmentListUiState.canLoadMore
        items.value = departmentListUiState.list
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp)
    ) {
        DepartmentAssignHeader(viewModel) {
            viewModel.getDepartmentAssignRemain()
            viewModel.getDepartmentAssignList(isRefresh = true)
            focusManager.clearFocus()
        }

        YBNormalList(
            modifier = Modifier.fillMaxSize(),
            items = items,
            isRefreshing = isRefreshing,
            isLoadingMore = isLoadingMore,
            canLoadMore = canLoadMore,
            onRefresh = {
                viewModel.getDepartmentAssignRemain()
                viewModel.getDepartmentAssignList(isRefresh = true)
            },
            onLoadMore = {
                viewModel.getDepartmentAssignList(isRefresh = false)
            }) { item, index ->
            AssignListItem(
                modifier = Modifier.fillMaxWidth(),
                item = item,
                onAssignClick = {
                    checkedItem = item
                    showAssignDialog.value = true
                }
            )
        }
    }

    YBPopup(
        visible = showAssignDialog.value,
        isShowTopActionBar = true,
        draggable = false,
        onClose = {
            showAssignDialog.value = false
            checkedItem = null
        }
    ) {
        val currentDate = LocalDate.now()
        val currentMonthText = "${currentDate.monthValue}月"
        var showMonthPicker by remember { mutableStateOf(false) }
        var monthPickedText by remember { mutableStateOf(currentMonthText) }
        val departmentRemainStatus by viewModel.assignRemainStatus.collectAsStateWithLifecycle()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp)
        ) {
            checkedItem?.apply {
                LabelText("年度：", viewModel.departYear.intValue.toString())
                LabelText("部门：", departmentName)
                LabelText("部门人员：", userName)
                LabelText("部门年度预算剩余：", "${formatDecimalString(departmentRemainStatus.yearBudget)}分")
                LabelText("年度已分配总分：", "${formatDecimalString(departmentRemainStatus.yearTotalBudget)}分")

                HorizontalDivider(
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .fillMaxWidth(),
                    thickness = 1.dp,
                    color = DividerColor
                )
                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "选择月份",
                        fontSize = 14.sp,
                        color = MainTextColor
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 10.dp)
                    ) {
                        DateSelectionView(label = monthPickedText, textAlignCenter = true, onClick = {
                            showMonthPicker = true
                        })
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "分配分数",
                        fontSize = 14.sp,
                        color = MainTextColor
                    )
                    Box(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .weight(1f)
                            .height(36.dp)
                            .border(0.5.dp, Color(0xFFEAEAEB), RoundedCornerShape(4.dp))
                            .background(
                                PageBackgroundColor
                            )
                    )
                    {
                        Box(modifier = Modifier
                            .align(Alignment.Center)
                            .width(80.dp)
                        ) {
                            YBInput(
                                textStyle = TextStyle(
                                    fontSize = 14.sp,
                                    color = MainTextColor
                                ),
                                hint = "暂未输入",
                                hintTextSize = 14.sp,
                                singleLine = true,
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Number
                                ),
                                onValueChange = {

                                }
                            )
                        }
                        Text(
                            modifier = Modifier.align(Alignment.CenterEnd).padding(end = 15.dp),
                            text = "分",
                            fontSize = 14.sp,
                            color = MainTextColor
                        )
                    }
                }
                Text(
                    modifier = Modifier.padding(top = 20.dp),
                    text = "部门月度预算剩余",
                    fontSize = 14.sp,
                    color = MainTextColor
                )
                DepartmentAnnualAssign(this)
                Spacer(modifier = Modifier.height(15.dp))
            }
        }

        // 单独的月份选择器弹窗组件
        val months = listOf(
            "1月",
            "2月",
            "3月",
            "4月",
            "5月",
            "6月",
            "7月",
            "8月",
            "9月",
            "10月",
            "11月",
            "12月"
        )
        SingleColumnPicker(
            visible = showMonthPicker,
            range = months,
            value = months.indexOf(monthPickedText),
            onChange = {
                monthPickedText = months[it]
            },
            onCancel = {
                showMonthPicker = false
            }
        )
    }
}

@Composable
private fun LabelText(prefix: String, label: String) {
    YBLabel(
        modifier = Modifier.padding(top = 5.dp),
        leadingIcon = {
            Text(
                text = prefix,
                color = MainTextColor,
                fontSize = 16.sp
            )
        },
        label = {
            Text(
                text = label,
                color = MainTextColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    )
}

@Composable
private fun DepartmentAssignHeader(viewModel: ReviewManageViewModel, onSearch: (String) -> Unit = {}) {
    val searchFilters = listOf(
        FilterType(type = 1, desc = "部门人员"),
        FilterType(type = 2, desc = "人员ID")
    )

    val showBudgetDialog = remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var datePickedText by remember { mutableStateOf("${viewModel.departYear.intValue}年") }
    val departmentRemainState by viewModel.assignRemainStatus.collectAsStateWithLifecycle()
    val departmentListStatus by viewModel.departmentListState.collectAsStateWithLifecycle()
    val curDepartmentData by viewModel.curDepartmentData.collectAsStateWithLifecycle()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 12.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 左侧排序指数选择区域
            Column(modifier = Modifier.weight(1.2f)) {
                DepartmentMultiDropMenuView(curDepartItem = curDepartmentData, filterData = departmentListStatus) { item ->
                    viewModel.curDepartmentData.value = item
                    viewModel.getDepartmentAssignRemain()
                    viewModel.getDepartmentAssignList(true)
                }
            }
            Spacer(Modifier.width(12.dp))
            // 右侧时间选择区域
            Column(modifier = Modifier.weight(1f)) {
                DateSelectionView(label = datePickedText, onClick = {
                    showDatePicker = true
                })
            }
            Spacer(Modifier.width(12.dp))
            YBButton(
                modifier = Modifier
                    .height(36.dp)
                    .weight(0.9f),
                text = { Text("预算剩余", fontSize = 13.sp) },
                contentPadding = PaddingValues(0.dp),
                onClick = {
                    showBudgetDialog.value = true
                }
            )
        }

        FilterSearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .border(0.5.dp, Color(0xFFEAEAEB), RoundedCornerShape(4.dp))
                .background(PageBackgroundColor)
                .height(36.dp),
            initialIndex = viewModel.departSearchType.intValue,
            initialSearchText = viewModel.departSearchKeyword.value,
            filterData = searchFilters,
            filterClick = { text, index ->
                viewModel.departSearchType.intValue = index
            }
        ) {
            viewModel.departSearchKeyword.value = it
            onSearch.invoke(it)
        }
    }

    // 年份选择器弹窗组件
    YBDatePicker(
        visible = showDatePicker,
        type = DateType.YEAR,
        value = LocalDate.of(viewModel.departYear.intValue, 1, 1),
        start = LocalDate.of(LocalDate.now().year - 5, 1, 1),
        end = LocalDate.of(LocalDate.now().year + 4, 1, 1),
        onCancel = { showDatePicker = false },
        onChange = {
            datePickedText = "${it.year}年"
            viewModel.departYear.intValue = it.year
        }
    )

    YBDialog(
        dialogState = showBudgetDialog,
        onDismissRequest = { showBudgetDialog.value = false },
        title = "预算剩余",
        widthRate = 0.9f,
        content = {
            Column {
                YBLabel(
                    modifier = Modifier,
                    leadingIcon = {
                        VerticalDivider(
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .size(4.dp, 16.dp)
                                .background(MainColor)
                        )
                    },
                    label = {
                        Text(text = "年度预算剩余：", fontSize = 16.sp, color = MainTextColor)
                    },
                    trailingIcon = {
                        Text(
                            text = "${departmentRemainState.yearBudget}",
                            fontSize = 16.sp,
                            color = MainColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                )
                Spacer(modifier = Modifier.height(15.dp))
                YBLabel(
                    modifier = Modifier,
                    leadingIcon = {
                        VerticalDivider(
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .size(4.dp, 16.dp)
                                .background(MainColor)
                        )
                    },
                    label = {
                        Text(text = "月度预算剩余：", fontSize = 16.sp, color = MainTextColor)
                    }
                )
                DepartmentAnnualAssign(departmentRemainState)
            }

        },
        confirmText = null,
        cancelText = null
    )
}


@PhonePreview
@Composable
private fun DepartmentAssignPreview() {
    YBTheme {
        DepartmentAssignScreen(
            viewModel = ReviewManageViewModel(
                savedStateHandle = SavedStateHandle(),
                retrofit = { previewRetrofit }
            ),
            navController = NavController(LocalContext.current)
        )
    }
}
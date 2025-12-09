package cn.thecover.media.feature.review_manager.appeal

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import cn.thecover.media.core.data.AppealListData
import cn.thecover.media.core.data.ArchiveListData
import cn.thecover.media.core.network.previewRetrofit
import cn.thecover.media.core.widget.R
import cn.thecover.media.core.widget.component.YBImage
import cn.thecover.media.core.widget.component.YBInput
import cn.thecover.media.core.widget.component.YBNormalList
import cn.thecover.media.core.widget.component.popup.YBAlignDropdownMenu
import cn.thecover.media.core.widget.event.clickableWithoutRipple
import cn.thecover.media.core.widget.theme.EditHintTextColor
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.OutlineColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.core.widget.ui.PhonePreview
import cn.thecover.media.feature.review_manager.ReviewManageViewModel
import cn.thecover.media.feature.review_manager.navigation.navigateToAppealDetail


/**
 * 我的申诉Tab页卡
 * <p> Created by CharlesLee on 2025/8/4
 * 15708478830@163.com
 */
@Composable
fun MyAppealContent(viewModel: ReviewManageViewModel, navController: NavController) {
    val filters = listOf(
        FilterType(type = 0, desc = "稿件标题"),
        FilterType(type = 1, desc = "人员姓名"),
        FilterType(type = 2, desc = "申诉理由")
    )

    val items = remember { mutableStateOf(listOf<AppealListData>()) }
    val isRefreshing = remember { mutableStateOf(false) }
    val isLoadingMore = remember { mutableStateOf(false) }
    val canLoadMore = remember { mutableStateOf(true) }
    val focusManager = LocalFocusManager.current
    val myAppealListUiState by viewModel.myAppealListDataState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getMyAppealList(isRefresh = true)
    }

    LaunchedEffect(myAppealListUiState) {
        isRefreshing.value = myAppealListUiState.isRefreshing
        isLoadingMore.value = myAppealListUiState.isLoading
        canLoadMore.value = myAppealListUiState.canLoadMore
        items.value = myAppealListUiState.list
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp)
    ) {
        FilterSearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .border(0.5.dp, Color(0xFFEAEAEB), RoundedCornerShape(4.dp))
                .background(Color.White)
                .height(36.dp),
            initialIndex = viewModel.myAppealSearchType.intValue,
            initialSearchText = viewModel.myAppealSearchKeyword.value,
            filterData = filters,
            filterClick = { _, index ->
                viewModel.myAppealSearchType.intValue = index
            }
        ) {
            viewModel.myAppealSearchKeyword.value = it
            viewModel.getMyAppealList(isRefresh = true)
            focusManager.clearFocus()
        }

        YBNormalList(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            items = items,
            isRefreshing = isRefreshing,
            isLoadingMore = isLoadingMore,
            canLoadMore = canLoadMore,
            onRefresh = {
                viewModel.getMyAppealList(isRefresh = true)
            },
            onLoadMore = {
                viewModel.getMyAppealList(isRefresh = true)
            }) { item, index ->
            AppealListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickableWithoutRipple {
                        // 跳转到申诉详情页
                        navController.navigateToAppealDetail(index % 2 == 0)
                    }, item = item
            )
        }
    }
}

@PhonePreview
@Composable
private fun AppealTabContentPreview() {
    YBTheme {
        MyAppealContent(
            viewModel = ReviewManageViewModel(
                savedStateHandle = SavedStateHandle(),
                retrofit = { previewRetrofit }
            ),
            navController = NavController(LocalContext.current)
        )
    }
}
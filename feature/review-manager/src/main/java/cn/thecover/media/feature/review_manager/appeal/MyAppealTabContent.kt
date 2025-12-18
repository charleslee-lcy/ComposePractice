package cn.thecover.media.feature.review_manager.appeal

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import cn.thecover.media.core.data.AppealListData
import cn.thecover.media.core.network.previewRetrofit
import cn.thecover.media.core.widget.component.YBNormalList
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

    val listState = rememberLazyListState()
    val items = remember { mutableStateOf(listOf<AppealListData>()) }
    val isRefreshing = remember { mutableStateOf(false) }
    val isLoadingMore = remember { mutableStateOf(false) }
    val canLoadMore = remember { mutableStateOf(true) }
    val focusManager = LocalFocusManager.current
    val myAppealListUiState by viewModel.myAppealListDataState.collectAsStateWithLifecycle()

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
            listState = listState,
            isRefreshing = isRefreshing,
            isLoadingMore = isLoadingMore,
            canLoadMore = canLoadMore,
            onRefresh = {
                viewModel.getMyAppealList(isRefresh = true)
            },
            onLoadMore = {
                viewModel.getMyAppealList(isRefresh = false)
            }) { item, _ ->
            AppealListItem(
                viewModel = viewModel,
                modifier = Modifier.fillMaxWidth(),
                item = item
            ) {
                // 跳转到申诉详情页
                navController.navigateToAppealDetail(item.id, false)
            }
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
package cn.thecover.media.feature.review_manager.appeal

import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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
import cn.thecover.media.feature.review_manager.navigation.navigateToAppealDetail
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * 申诉管理Tab页卡
 * <p> Created by CharlesLee on 2025/8/4
 * 15708478830@163.com
 */
@Composable
fun MyAppealContent(navController: NavController) {
    val filters = listOf(
        FilterType(type = 0, desc = "稿件标题"),
        FilterType(type = 1, desc = "稿件ID"),
        FilterType(type = 2, desc = "申诉内容")
    )

    // 模拟数据
    var items = remember { mutableStateOf((1..20).toList()) }
    val scope = rememberCoroutineScope()
    var isRefreshing = remember { mutableStateOf(false) }
    var isLoadingMore = remember { mutableStateOf(false) }
    var canLoadMore = remember { mutableStateOf(true) }

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
            initialIndex = 0,
            filterData = filters,
            filterClick = { text, index ->
                Log.d("CharlesLee", "filterType: ${filters[index].type}")
            }
        ) {

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
                scope.launch {
                    // 模拟网络
                    delay(1000)
                    items.value = (1..20).toList()
                    canLoadMore.value = true
                    isRefreshing.value = false
                }
            },
            onLoadMore = {
                scope.launch {
                    // 模拟网络
                    delay(1000)
                    val next = items.value.lastOrNull() ?: 0
                    items.value = items.value + (next + 1..next + 10).toList()
                    isLoadingMore.value = false
                    // 模拟最后一页
                    if (items.value.size >= 50) canLoadMore.value = false
                }
            }) { item, index ->
            AppealListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickableWithoutRipple {
                        // 跳转到申诉详情页
                        navController.navigateToAppealDetail()
                    }, index = index
            )
        }
    }
}

data class FilterType(val type: Int, val desc: String)

@Composable
fun FilterSearchBar(
    modifier: Modifier = Modifier,
    initialIndex: Int = 0,
    filterData: List<FilterType>,
    filterClick: (String, Int) -> Unit = { _, _ -> },
    onSearch: (String) -> Unit = {}
) {
    var expanded = remember { mutableStateOf(false) }
    val animRotate = remember { Animatable(0f) }
    var title by remember { mutableStateOf(filterData[initialIndex].desc) }
    var searchText by remember { mutableStateOf("") }

    // 当菜单状态改变时触发动画
    LaunchedEffect(expanded.value) {
        animRotate.animateTo(
            targetValue = if (expanded.value) 180f else 0f,
            animationSpec = tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing
            )
        )
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        YBAlignDropdownMenu(
            data = filterData.map { it.desc },
            expanded = expanded,
            initialIndex = initialIndex,
            isItemWidthAlign = true,
            modifier = Modifier.weight(0.28f),
            offset = DpOffset(0.dp, 0.dp),
            onItemClick = { text, index ->
                title = text
                filterClick.invoke(text, index)
            }) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterHorizontally)
                    .clickableWithoutRipple {
                        expanded.value = !expanded.value
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(start = 12.dp),
                    text = title,
                    color = MainTextColor,
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(Modifier.weight(1f))
                YBImage(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(18.dp)
                        .rotate(animRotate.value),
                    placeholder = painterResource(R.mipmap.ic_arrow_down_light_grey)
                )
            }
        }
        VerticalDivider(modifier = Modifier.height(20.dp), thickness = 0.5.dp, color = OutlineColor)
        YBInput(
            text = searchText,
            modifier = Modifier
                .weight(0.7f)
                .padding(horizontal = 15.dp),
            textStyle = TextStyle(
                fontSize = 13.sp, color = MainTextColor
            ),
            hint = "请输入搜索内容",
            hintTextSize = 13.sp,
            hintTextColor = EditHintTextColor,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch.invoke(searchText)
                }
            ),
            onValueChange = {
                searchText = it
            })
    }
}

@PhonePreview
@Composable
private fun AppealTabContentPreview() {
    YBTheme {
        MyAppealContent(navController = NavController(LocalContext.current))
    }
}
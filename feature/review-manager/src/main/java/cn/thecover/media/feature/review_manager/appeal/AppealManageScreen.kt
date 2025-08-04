package cn.thecover.media.feature.review_manager.appeal

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cn.thecover.media.core.widget.component.YBImage
import cn.thecover.media.core.widget.component.YBTab
import cn.thecover.media.core.widget.component.YBTabRow
import cn.thecover.media.core.widget.component.popup.YBDropdownMenu
import cn.thecover.media.core.widget.event.clickableWithoutRipple
import cn.thecover.media.core.widget.theme.DividerColor
import cn.thecover.media.core.widget.theme.EditHintTextColor
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.OutlineColor
import cn.thecover.media.core.widget.theme.PageBackgroundColor
import cn.thecover.media.core.widget.theme.TertiaryTextColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.feature.review_manager.ReviewManageViewModel


/**
 * 申诉管理
 * <p> Created by CharlesLee on 2025/7/28
 * 15708478830@163.com
 */
@Composable
internal fun AppealManageScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val scrollState = rememberScrollState()
    val titles = listOf("我的申诉", "申诉审批")
    val currentTabIndex = remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp)
    ) {
        YBTabRow(
            selectedTabIndex = currentTabIndex.intValue,
            modifier = Modifier.background(Color.White).padding(horizontal = 30.dp),
            indicatorWidth = 64.dp,
            indicatorBottomMargin = 0.dp
        ) {
            titles.forEachIndexed { index, title ->
                YBTab(
                    modifier = Modifier.height(44.dp),
                    selected = index == currentTabIndex.intValue,
                    normalTextColor = TertiaryTextColor,
                    onClick = { currentTabIndex.intValue = index },
                    text = { Text(text = title, fontSize = 16.sp) }
                )
            }
        }
        FilterSearchBar { text, index ->

        }
    }
}

private data class FilterType(val title: String, val type: Int)

@Composable
private fun FilterSearchBar(titleClick: (String, Int) -> Unit = {_, _ -> }) {
    val list = listOf("稿件标题", "稿件ID", "申诉内容")
    var expanded = remember { mutableStateOf(false) }
    var title by remember { mutableStateOf(list[0]) }
    val userNameState = rememberTextFieldState("")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .height(36.dp)
            .background(Color.White)
            .border(1.dp, Color(0xFFEAEAEB), RoundedCornerShape(4.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        YBDropdownMenu(
            data = list,
            expanded = expanded,
            modifier = Modifier.weight(0.28f),
            offset = DpOffset(0.dp, 6.dp),
            onItemClick = { text, index ->
                title = text
                titleClick.invoke(text, index)
            }
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .align(Alignment.CenterHorizontally)
                    .clickableWithoutRipple {
                        expanded.value = !expanded.value
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = MainTextColor,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
                YBImage(
                    modifier = Modifier.size(18.dp),
                    placeholder = painterResource(cn.thecover.media.core.widget.R.mipmap.ic_arrow_down)
                )
            }
        }
        VerticalDivider(modifier = Modifier.height(20.dp), thickness = 0.5.dp, color = OutlineColor)
        TextField(
            state = userNameState,
            lineLimits = TextFieldLineLimits.MultiLine(maxHeightInLines = 1),
            placeholder = {
                Text(
                    text = "请输入搜索内容",
                    color = EditHintTextColor,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(bottom = 1.dp)
                )
            },
            textStyle = TextStyle(color = Color(0xFF333333), fontSize = 15.sp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(
                start = 0.dp, end = 0.dp
            ),
            modifier = Modifier.weight(0.7f)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun AppealManagePreview() {
    YBTheme {
        AppealManageScreen(navController = NavController(LocalContext.current))
    }
}
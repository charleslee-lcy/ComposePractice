package cn.thecover.media.feature.review_manager.appeal

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.thecover.media.core.widget.component.YBImage
import cn.thecover.media.core.widget.component.popup.YBAlignDropdownMenu
import cn.thecover.media.core.widget.event.clickableWithoutRipple
import cn.thecover.media.core.widget.theme.EditHintTextColor
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.OutlineColor
import cn.thecover.media.core.widget.ui.PhonePreview


/**
 * 申诉管理Tab页卡
 * <p> Created by CharlesLee on 2025/8/4
 * 15708478830@163.com
 */
@Composable
fun MyAppealContent() {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 15.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val filters = listOf(
            AppealFilterType.ARCHIVE_TITLE,
            AppealFilterType.ARCHIVE_ID,
            AppealFilterType.ARCHIVE_CONTENT
        )

        FilterSearchBar(initialIndex = 1) { text, index ->
            Log.d("CharlesLee", "filterType: ${filters[index].type}")
        }
    }
}

internal enum class AppealFilterType(val type: Int) {
    ARCHIVE_TITLE(0),
    ARCHIVE_ID(1),
    ARCHIVE_CONTENT(2)
}

@Composable
private fun FilterSearchBar(initialIndex: Int = 0, filterClick: (String, Int) -> Unit = {_, _ -> }) {
    val list = listOf("稿件标题", "稿件ID", "申诉内容")
    var expanded = remember { mutableStateOf(false) }
    var title by remember { mutableStateOf(list[initialIndex]) }
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
        YBAlignDropdownMenu(
            data = list,
            expanded = expanded,
            initialIndex = initialIndex,
            isItemWidthAlign = true,
            modifier = Modifier.weight(0.28f),
            offset = DpOffset(0.dp, 0.dp),
            onItemClick = { text, index ->
                title = text
                filterClick.invoke(text, index)
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterHorizontally)
                    .clickableWithoutRipple {
                        expanded.value = !expanded.value
                    },
                horizontalArrangement = Arrangement.Center,
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

@PhonePreview
@Composable
private fun AppealTabContentPreview() {
    MyAppealContent()
}
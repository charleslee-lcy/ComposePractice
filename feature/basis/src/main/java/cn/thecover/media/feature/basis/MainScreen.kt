package cn.thecover.media.feature.basis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.thecover.media.core.widget.component.SingleLineInput
import cn.thecover.media.core.widget.icon.CommonIcons
import cn.thecover.media.core.widget.theme.HintTextColor
import cn.thecover.media.core.widget.theme.MainColor
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.CommonTheme
import cn.thecover.media.core.widget.ui.ComponentPreview


/**
 *
 * <p> Created by CharlesLee on 2025/12/30
 * 15708478830@163.com
 */
@Composable
fun MainScreen() {
    CommonTheme {
        Column(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                stickyHeader {
                    SearchHeader()
                }
                items((1..50).toList()) { number ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "数字: $number",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchHeader() {
    Column(modifier = Modifier.fillMaxWidth().background(Color.White)) {
        // 搜索框
        var searchText by remember { mutableStateOf("") }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 5.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            SingleLineInput(
                text = searchText,
                textStyle = TextStyle(fontSize = 14.sp, color = MainTextColor),
                onValueChange = { searchText = it },
                modifier = Modifier.weight(1f).height(56.dp),
                hint = "请输入搜索内容",
                hintTextStyle = TextStyle(fontSize = 14.sp, color = HintTextColor)
            )

            IconButton(
                onClick = {
                    // TODO: 处理搜索逻辑
                    println("搜索: $searchText")
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = CommonIcons.Search,
                    contentDescription = "搜索",
                    tint = MainColor
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { /* TODO */ },
                modifier = Modifier.weight(1f)
            ) {
                Text("按钮 1")
            }

            Button(
                onClick = { /* TODO */ },
                modifier = Modifier.weight(1f)
            ) {
                Text("按钮 2")
            }

            Button(
                onClick = { /* TODO */ },
                modifier = Modifier.weight(1f)
            ) {
                Text("按钮 3")
            }
        }
    }
}

@ComponentPreview
@Composable
fun MainScreenPreview() {
    MainScreen()
}
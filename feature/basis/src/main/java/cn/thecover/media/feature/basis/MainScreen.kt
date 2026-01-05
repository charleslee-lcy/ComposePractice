package cn.thecover.media.feature.basis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.thecover.media.core.widget.component.SingleLineInput
import cn.thecover.media.core.widget.icon.CommonIcons
import cn.thecover.media.core.widget.theme.HintTextColor
import cn.thecover.media.core.widget.theme.MainColor
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.CommonTheme
import cn.thecover.media.core.widget.theme.TertiaryTextColor
import cn.thecover.media.core.widget.ui.ComponentPreview


/**
 *
 * <p> Created by CharlesLee on 2025/12/30
 * 15708478830@163.com
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val tabs = listOf(
        "首页",
        "发现",
        "消息",
        "我的",
    )
    var selected by remember { mutableIntStateOf(0) }
    CommonTheme {
        Scaffold(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .background(Color.White),
                    title = {
                        Text("首页")
                    },
//            navigationIcon = {
//                Icon(
//                    modifier = Modifier.padding(start = 8.dp),
//                    painter = painterResource(cn.thecover.media.core.widget.R.drawable.icon_back_arrow),
//                    contentDescription = "返回"
//                )
//            }
                )
            },
            bottomBar = {
                NavigationBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .height(60.dp)
                ) {
                    tabs.forEachIndexed { index, title ->
                        NavigationBarItem(
                            modifier = Modifier.padding(vertical = 12.dp),
                            icon = {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    painter = painterResource(when (index) {
                                        0 -> {
                                            if (index == selected) R.mipmap.ic_tab_home_checked else R.mipmap.ic_tab_home_normal
                                        }
                                        1 -> {
                                            if (index == selected) R.mipmap.ic_tab_review_manage_checked else R.mipmap.ic_tab_review_manage_normal
                                        }
                                        2 -> {
                                            if (index == selected) R.mipmap.ic_tab_review_data_checked else R.mipmap.ic_tab_review_data_normal
                                        }
                                        else -> {
                                            if (index == selected) R.mipmap.ic_tab_mine_checked else R.mipmap.ic_tab_mine_normal
                                        }
                                    }),
                                    contentDescription = title
                                )
                            },
                            label = {
                                Text(text = title, fontSize = 12.sp)
                            },
                            selected = index == selected,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MainColor,
                                unselectedIconColor = TertiaryTextColor,
                                selectedTextColor = MainColor,
                                unselectedTextColor = TertiaryTextColor,
                                indicatorColor = Color.Transparent,
                            ),
                            onClick = {
                                selected = index
                            }
                        )
                    }
                }
            }
        ) { padding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .consumeWindowInsets(padding)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal,
                        ),
                    ),
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchHeader() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)) {
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
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
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

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { /* TODO */ },
            ) {
                Text("按钮 1")
            }

            Button(
                onClick = { /* TODO */ },
            ) {
                Text("按钮 2")
            }

            Button(
                onClick = { /* TODO */ },
            ) {
                Text("按钮 3")
            }

            Button(
                onClick = { /* TODO */ },
            ) {
                Text("按钮 4")
            }

            Button(
                onClick = { /* TODO */ },
            ) {
                Text("按钮 5")
            }
        }
    }
}

@ComponentPreview
@Composable
fun MainScreenPreview() {
    MainScreen()
}
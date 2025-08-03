package cn.thecover.media.feature.basis.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Poll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cn.thecover.media.core.widget.component.YBBadge
import cn.thecover.media.core.widget.component.YBBanner
import cn.thecover.media.core.widget.component.YBImage
import cn.thecover.media.core.widget.component.YBTab
import cn.thecover.media.core.widget.component.YBTabRow
import cn.thecover.media.core.widget.component.YBToast
import cn.thecover.media.core.widget.component.picker.DateType
import cn.thecover.media.core.widget.component.picker.YBDatePicker
import cn.thecover.media.core.widget.component.showToast
import cn.thecover.media.core.widget.event.clickableWithoutRipple
import cn.thecover.media.core.widget.icon.YBIcons
import cn.thecover.media.core.widget.theme.HintTextColor
import cn.thecover.media.core.widget.theme.MainColor
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.OutlineColor
import cn.thecover.media.core.widget.theme.PageBackgroundColor
import cn.thecover.media.core.widget.theme.TertiaryTextColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.core.widget.ui.ComponentPreview
import cn.thecover.media.feature.basis.R
import cn.thecover.media.feature.basis.home.ui.LeaderUserContent
import cn.thecover.media.feature.basis.home.ui.ReporterUserContent
import kotlinx.coroutines.launch


/**
 *
 * <p> Created by CharlesLee on 2025/7/28
 * 15708478830@163.com
 */
@Composable
internal fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
) {
//    val feedState by viewModel..collectAsStateWithLifecycle()
    YBTheme {
        HomeScreen()
    }
}


@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier
) {
    val listData = remember {
        mutableStateListOf(
            "https://gips3.baidu.com/it/u=119870705,2790914505&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=720",
            "https://gips2.baidu.com/it/u=195724436,3554684702&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960",
            "https://gips0.baidu.com/it/u=1490237218,4115737545&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=720",
            "https://gips2.baidu.com/it/u=207216414,2485641185&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=720",
            "https://gips2.baidu.com/it/u=828570294,3060139577&fm=3028&app=3028&f=JPEG&fmt=auto?w=1024&h=1024",
        )
    }

    val mainScreenScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()
    var roleState by remember { mutableIntStateOf(1) }
    val titles = listOf("稿件TOP10", "稿件传播力TOP10")
    val currentIndex = remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TopBar {
            roleState = if (roleState == 1) 2 else 1
        }

        Column(
            modifier = modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .background(PageBackgroundColor),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(1.dp))
            AnimatedContent(roleState) {
                if (it == 1) {
                    ReporterUserContent()
                } else {
                    LeaderUserContent()
                }
            }
            YBBanner(
                modifier = Modifier.height(150.dp),
                items = listData,
                autoScroll = true,
                autoScrollDelay = 3000L
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Poll,
                    tint = MainColor,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "稿件TOP榜单",
                    color = MainTextColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 5.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .clickable {
                            mainScreenScope.launch {
                                snackBarHostState.showToast("查看更多排行数据")
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "查看更多",
                        color = TertiaryTextColor,
                        lineHeight = 14.sp,
                        fontSize = 14.sp,
                    )
                    Icon(
                        painterResource(YBIcons.Custom.RightArrow),
                        contentDescription = "Localized description",
                        Modifier
                            .size(18.dp)
                            .padding(2.dp),
                        tint = TertiaryTextColor
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
            ) {
                YBTabRow(
                    selectedTabIndex = currentIndex.intValue,
                    modifier = Modifier.padding(horizontal = 30.dp)
                ) {
                    titles.forEachIndexed { index, title ->
                        YBTab(
                            selected = index == currentIndex.intValue,
                            onClick = { currentIndex.intValue = index },
                            text = { Text(text = title) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(500.dp))
            }
        }
    }

    YBToast(snackBarHostState = snackBarHostState)
}

@Composable
private fun TopBar(titleClick: () -> Unit = {}) {
    var datePickerShow by remember { mutableStateOf(false) }
    var datePickedText by remember { mutableStateOf("2025年8月") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .statusBarsPadding()
            .height(40.dp)
    ) {
        Text(
            text = "CharlesLee",
            color = MainTextColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .align(Alignment.CenterStart)
                .clickableWithoutRipple {
                    titleClick.invoke()
                }
        )
        Row(
            modifier = Modifier
                .clickableWithoutRipple {
                    datePickerShow = true
                }
                .padding(horizontal = 10.dp)
                .align(Alignment.Center), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = datePickedText,
                color = MainTextColor,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            YBImage(
                modifier = Modifier.size(20.dp),
                placeholder = painterResource(cn.thecover.media.core.widget.R.mipmap.ic_arrow_down)
            )
        }
        YBBadge(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .align(Alignment.CenterEnd),
            msgCount = 10,
            showNumber = false
        ) {
            YBImage(
                modifier = Modifier.size(20.dp),
                placeholder = painterResource(R.mipmap.ic_home_msg)
            )
        }
    }

    YBDatePicker(
        visible = datePickerShow,
        type = DateType.MONTH,
        onCancel = { datePickerShow = false },
        onChange = {
            datePickedText = "${it.year}年${it.monthValue}月"
        }
    )
}


@ComponentPreview
@Composable
private fun HomeScreenPreview() {
    YBTheme {
        HomeScreen()
    }
}
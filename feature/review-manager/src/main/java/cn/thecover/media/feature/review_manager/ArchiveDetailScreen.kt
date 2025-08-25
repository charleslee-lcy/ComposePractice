package cn.thecover.media.feature.review_manager

import android.R.attr.data
import android.R.attr.text
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cn.thecover.media.core.widget.component.YBBanner
import cn.thecover.media.core.widget.component.YBImage
import cn.thecover.media.core.widget.component.YBTitleBar
import cn.thecover.media.core.widget.event.clickableWithoutRipple
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.SecondaryTextColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.core.widget.ui.PhonePreview
import com.mohamedrejeb.calf.ui.web.WebView
import com.mohamedrejeb.calf.ui.web.WebViewState
import com.mohamedrejeb.calf.ui.web.rememberWebViewState
import java.lang.System.exit

@Composable
internal fun ArchiveDetailRoute(
    modifier: Modifier = Modifier,
    data: ArchiveListData,
    navController: NavController
) {
    ArchiveDetailScreen(modifier, data, navController)
}

/**
 *
 * <p> Created by CharlesLee on 2025/8/8
 * 15708478830@163.com
 */
@Composable
fun ArchiveDetailScreen(
    modifier: Modifier = Modifier,
    data: ArchiveListData,
    navController: NavController
) {
    val webViewState = rememberWebViewState(
        url = data.link
    )
    val showImages = remember { mutableStateOf(false) }
    val imagesData = remember {
        mutableStateListOf(
            "https://gips2.baidu.com/it/u=2655957002,493323304&fm=3074&app=3074&f=PNG?w=1440&h=2560",
            "https://q0.itc.cn/q_70/images03/20241216/c64a9e4d9e8c4f33ad0a7cb6ac050120.jpeg",
            "https://gips2.baidu.com/it/u=195724436,3554684702&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960",
            "https://gips2.baidu.com/it/u=207216414,2485641185&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=720",
            "https://gips2.baidu.com/it/u=828570294,3060139577&fm=3028&app=3028&f=JPEG&fmt=auto?w=1024&h=1024",
        )
    }

    LaunchedEffect(Unit) {
        // Get the current loading state
        // Enable JavaScript
        webViewState.settings.javaScriptEnabled = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        WebViewContent(showImages, webViewState, data, navController)
        AnimatedVisibility(
            showImages.value,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut(),
        ) {
            PreviewImages(imagesData, showImages)
        }
    }
}

@Composable
private fun WebViewContent(
    showImages: MutableState<Boolean>,
    webViewState: WebViewState,
    data: ArchiveListData,
    navController: NavController
) {
    val scrollState = rememberScrollState()

    BackHandler(showImages.value) {
        showImages.value = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .statusBarsPadding()
        )
        YBTitleBar(title = "稿件详情", leftOnClick = {
            navController.popBackStack()
        })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickableWithoutRipple {
                    showImages.value = true
                }
                .verticalScroll(scrollState),
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp),
                text = data.title,
                color = MainTextColor,
                fontSize = 24.sp,
                lineHeight = 24.sp * 1.3f
            )
            Row(
                modifier = Modifier.padding(horizontal = 15.dp)
            ) {
                Text(
                    text = "周国超",
                    color = SecondaryTextColor,
                    fontSize = 14.sp,
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = data.niceDate,
                    color = SecondaryTextColor,
                    fontSize = 14.sp,
                )
            }
            WebView(
                state = webViewState,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun PreviewImages(
    imagesData: List<String>,
    showImages: MutableState<Boolean>,
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { imagesData.size })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MainTextColor)
    ) {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { pageIndex ->
            YBImage(
                imageUrl = imagesData[pageIndex],
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
        Icon(
            modifier = Modifier
                .statusBarsPadding()
                .clickableWithoutRipple {
                    showImages.value = false
                }.padding(10.dp),
            imageVector = Icons.Outlined.Close,
            tint = Color.White,
            contentDescription = "关闭"
        )
        Text(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(vertical = 15.dp),
            text = "${pagerState.currentPage + 1}/${imagesData.size}",
            style = TextStyle(
                color = Color.White,
                fontSize = 14.sp,
                shadow = Shadow(
                    color = Color(0x99000000),
                    offset = Offset(4f, 4f),   // x, y 偏移
                    blurRadius = 8f            // 模糊半径
                )
            )
        )
    }
}

@PhonePreview
@Composable
private fun ArchiveScoreScreenPreview() {
    YBTheme {
        ArchiveDetailScreen(
            data = ArchiveListData(),
            navController = NavController(LocalContext.current)
        )
    }
}
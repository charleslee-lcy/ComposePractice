package cn.thecover.media.feature.review_manager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cn.thecover.media.core.widget.component.YBTitleBar
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.SecondaryTextColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.core.widget.ui.PhonePreview
import com.mohamedrejeb.calf.ui.web.WebView
import com.mohamedrejeb.calf.ui.web.rememberWebViewState

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
    val scrollState = rememberScrollState()
    val state = rememberWebViewState(
        url = data.link
    )

    LaunchedEffect(Unit) {
        // Get the current loading state
        // Enable JavaScript
        state.settings.javaScriptEnabled = true
    }

    Column(
        modifier = Modifier.fillMaxSize()
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
                state = state,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@PhonePreview
@Composable
private fun ArchiveScoreScreenPreview() {
    YBTheme {
        ArchiveDetailScreen(data = ArchiveListData(), navController = NavController(LocalContext.current))
    }
}
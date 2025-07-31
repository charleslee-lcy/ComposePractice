package cn.thecover.media.feature.basis.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cn.thecover.media.core.widget.component.YBBanner
import cn.thecover.media.core.widget.event.EventConstants
import cn.thecover.media.core.widget.event.FlowBus
import cn.thecover.media.core.widget.event.FlowEvent
import cn.thecover.media.core.widget.ui.ComponentPreview
import com.google.samples.apps.nowinandroid.core.designsystem.theme.YBTheme


/**
 *
 * <p> Created by CharlesLee on 2025/7/28
 * 15708478830@163.com
 */
@Composable
internal fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
//    val feedState by viewModel..collectAsStateWithLifecycle()
    HomeScreen(modifier)
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

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Spacer(Modifier.size(50.dp))
        Text(
            text = "首页",
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                FlowBus.post(FlowEvent(EventConstants.ACTION_HOME, "我传了消息"))
            }
        )
        Spacer(Modifier.size(20.dp))
        YBBanner(items = listData, autoScroll = true, autoScrollDelay = 2000L)

    }
}

@ComponentPreview
@Composable
private fun HomeScreenPreview() {
    YBTheme {
        HomeScreen()
    }
}
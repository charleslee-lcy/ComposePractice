package cn.thecover.media.feature.basis.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import cn.thecover.media.core.widget.event.EventConstants
import cn.thecover.media.core.widget.event.FlowBus
import cn.thecover.media.core.widget.event.FlowEvent
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
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "首页",
            modifier = modifier.clickable {
                FlowBus.post(FlowEvent(EventConstants.ACTION_HOME, "我传了消息"))
            }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    YBTheme {
        HomeScreen()
    }
}
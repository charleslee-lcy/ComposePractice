package cn.thecover.media.feature.basis.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cn.thecover.media.core.widget.component.YBImage
import cn.thecover.media.core.widget.event.EventConstants
import cn.thecover.media.core.widget.event.FlowBus
import cn.thecover.media.core.widget.event.FlowEvent
import cn.thecover.media.feature.basis.R
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
        YBImage(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            placeholder = painterResource(R.mipmap.icon)
        )
        Spacer(Modifier.size(20.dp))
        YBImage(
            imageUrl = "https://ww2.sinaimg.cn/mw690/007ut4Uhly1hx4v37mpxcj30u017cgrv.jpg",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            placeholder = painterResource(R.mipmap.icon)
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
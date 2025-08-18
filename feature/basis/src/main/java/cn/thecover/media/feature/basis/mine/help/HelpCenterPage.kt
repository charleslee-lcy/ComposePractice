package cn.thecover.media.feature.basis.mine.help

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cn.thecover.media.core.widget.component.YBWebViewPage

/**
 *  Created by Wing at 16:21 on 2025/8/18
 *
 */
@Composable
internal fun HelpCenterRoute(
    modifier: Modifier = Modifier,
    routeToDetail: (String) -> Unit = {},
    onPopBack: () -> Unit = {}
) {
    HelpCenterPage(onPopBack)
}

@Composable
private fun HelpCenterPage(onPopBack: () -> Unit) {
    var pageTitle by remember { mutableStateOf("") }

    YBWebViewPage(
        modifier = Modifier.fillMaxSize(),
        url = "https://www.ithome.com/0/876/220.htm",
            onReceivedTitle = { title ->
            pageTitle = title
        },
        onBackRequested = {
            // 处理返回逻辑
            onPopBack()
        }
    )
}
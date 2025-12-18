package cn.thecover.media.feature.basis.mine.help

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import cn.thecover.media.core.widget.component.YBWebViewPage
import cn.thecover.media.feature.basis.mine.MineIntent
import cn.thecover.media.feature.basis.mine.MineViewModel

/**
 *  Created by Wing at 16:21 on 2025/8/18
 *
 */
@Composable
internal fun HelpCenterRoute(
    modifier: Modifier = Modifier,
    viewModel: MineViewModel = hiltViewModel(),
    onPopBack: () -> Unit = {}
) {
    LaunchedEffect(Unit) {
        viewModel.handleIntent(MineIntent.GetHelpCenterUrl)
    }

    HelpCenterPage(viewModel,onPopBack)
}

@Composable
private fun HelpCenterPage(viewModel: MineViewModel,onPopBack: () -> Unit) {
    val htmlContent by viewModel.helpCenterUrlUiData.collectAsState()
    if (htmlContent == null) return
    YBWebViewPage(
        modifier = Modifier.fillMaxSize(),
        defaultTitle = "帮助中心",
        htmlContent = htmlContent?.content,
        onBackRequested = {
            // 处理返回逻辑
            onPopBack()
        }
    )
}
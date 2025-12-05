package cn.thecover.media.feature.basis.mine.help

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import cn.thecover.media.core.widget.component.YBWebViewPage
import cn.thecover.media.feature.basis.mine.MineIntent
import cn.thecover.media.feature.basis.mine.MineViewModel
import cn.thecover.media.feature.basis.mine.intent.MineNavigationIntent

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
    var pageTitle by remember { mutableStateOf(viewModel.helpCenterUrlUiData.value) }

    YBWebViewPage(
        modifier = Modifier.fillMaxSize(),
        url = viewModel.helpCenterUrlUiData.value ?: "",
            onReceivedTitle = { title ->
            pageTitle = title
        },
        onBackRequested = {
            // 处理返回逻辑
            onPopBack()
        }
    )
}
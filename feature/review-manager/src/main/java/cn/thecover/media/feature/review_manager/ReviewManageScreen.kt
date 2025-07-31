package cn.thecover.media.feature.review_manager

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import cn.thecover.media.core.widget.theme.YBTheme


/**
 *
 * <p> Created by CharlesLee on 2025/7/28
 * 15708478830@163.com
 */

@Composable
internal fun ReviewManageRoute(
    modifier: Modifier = Modifier,
    viewModel: ReviewManageViewModel = hiltViewModel()
) {
    ReviewManageScreen(modifier)
}

@Composable
internal fun ReviewManageScreen(
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text("绩效管理")
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ReviewManagePreview() {
    YBTheme {
        ReviewManageScreen()
    }
}
package cn.thecover.media.feature.review_data

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
internal fun ReviewDataRoute(
    modifier: Modifier = Modifier,
    viewModel: ReviewDataViewModel = hiltViewModel()
) {
    ReviewDataScreen(modifier)
}

@Composable
internal fun ReviewDataScreen(
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text("考核数据")
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ReviewDataPreview() {
    YBTheme {
        ReviewDataScreen()
    }
}
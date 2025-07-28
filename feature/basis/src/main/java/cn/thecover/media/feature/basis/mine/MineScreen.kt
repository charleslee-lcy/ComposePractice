package cn.thecover.media.feature.basis.mine

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.samples.apps.nowinandroid.core.designsystem.theme.YBTheme


/**
 *
 * <p> Created by CharlesLee on 2025/7/28
 * 15708478830@163.com
 */
@Composable
internal fun MineRoute(
    modifier: Modifier = Modifier,
    viewModel: MineViewModel = hiltViewModel()
) {
    MineScreen(modifier)
}

@Composable
internal fun MineScreen(
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text("我的")
    }
}

@Preview(showSystemUi = true)
@Composable
private fun MineScreenPreview() {
    YBTheme {
        MineScreen()
    }
}
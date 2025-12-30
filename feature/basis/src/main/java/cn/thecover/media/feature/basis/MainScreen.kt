package cn.thecover.media.feature.basis

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.core.widget.ui.ComponentPreview


/**
 *
 * <p> Created by CharlesLee on 2025/12/30
 * 15708478830@163.com
 */
@Composable
fun MainScreen() {
    YBTheme {
        Column(modifier = Modifier.safeContentPadding().fillMaxSize()) {
            Text("Hello World.")
        }
    }
}

@ComponentPreview
@Composable
fun MainScreenPreview() {
    MainScreen()
}
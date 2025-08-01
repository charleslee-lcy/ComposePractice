package cn.thecover.media.core.widget.component.popup

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.core.widget.ui.ComponentPreview


/**
 *
 * <p> Created by CharlesLee on 2025/7/31
 * 15708478830@163.com
 */
@Composable
fun YBDropdownMenu(
    visible: Boolean,
    enterTransition: EnterTransition = slideInVertically(
        animationSpec = tween(150),
        initialOffsetY = { it }
    ),
    exitTransition: ExitTransition = slideOutVertically(
        animationSpec = tween(150),
        targetOffsetY = { it }
    ),
    onClose: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {

}

@ComponentPreview
@Composable
fun YBDropdownMenuPreview() {
    YBTheme {
        YBDropdownMenu(visible = true) {

        }
    }
}
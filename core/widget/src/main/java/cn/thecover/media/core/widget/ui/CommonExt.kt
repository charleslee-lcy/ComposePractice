package cn.thecover.media.core.widget.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha


/**
 *
 * <p> Created by CharlesLee on 2025/7/31
 * 15708478830@163.com
 */
// 定义可见性枚举
enum class Visibility {
    Visible,
    Gone,
    Invisible // 仅隐藏但保留空间
}

/**
 * 包裹组件设置可见性
 */
@Composable
fun ComponentVisibility(
    visibility: Visibility = Visibility.Visible,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    when (visibility) {
        Visibility.Visible -> {
            content()
        }
        Visibility.Gone -> {
            // 不渲染内容，类似 GONE
        }
        Visibility.Invisible -> {
            // 渲染但不可见，类似 INVISIBLE
            Box(modifier = modifier.alpha(0f)) {
                content()
            }
        }
    }
}
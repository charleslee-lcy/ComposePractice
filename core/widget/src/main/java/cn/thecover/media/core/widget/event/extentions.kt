package cn.thecover.media.core.widget.event

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

/**
 *  Created by Wing at 10:18 on 2025/7/31
 *  一些compose相关扩展方法
 */


/**
 * 点击无水波纹
 */
fun Modifier.clickableWithoutRipple(enabled: Boolean = true, onClick: () -> Unit) = composed {
    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        enabled
    ) {
        onClick()
    }
}
package cn.thecover.media.core.widget.event

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

/**
 *  Created by Wing at 10:18 on 2025/7/31
 *  一些compose相关扩展方法
 */


/**
 * 点击无水波纹
 *
 * @param waitTime 防重复点击间隔时间
 */
fun Modifier.clickableWithoutRipple(waitTime: Long = 500, enabled: Boolean = true, onClick: () -> Unit) = composed {
    var lastClick by remember { mutableLongStateOf(0L) }

    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        enabled
    ) {
        val current = System.currentTimeMillis()
        if (current - lastClick >= waitTime) {
            lastClick = current
            onClick()
        }
    }
}
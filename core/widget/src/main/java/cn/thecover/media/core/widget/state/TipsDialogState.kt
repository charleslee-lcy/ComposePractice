package cn.thecover.media.core.widget.state

import androidx.annotation.DrawableRes
import cn.thecover.media.core.widget.R
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/**
 *  Created by Wing at 11:05 on 2025/7/30
 *  加载状态管理器,用于处理加载弹窗显示与隐藏逻辑
 */


/**
 * 加载状态管理器
 */
@Composable
fun rememberTipsDialogState(): TipsDialogState {
    val tipState = remember { TipsDialogState() }
    return tipState
}

@Composable
fun rememberIconTipsDialogState(): IconDialogState {
    val tipState = remember { IconDialogState() }
    return tipState
}

class IconDialogState : TipsDialogState() {
    var iconResource by mutableStateOf(R.drawable.icon_checked)
        private set

    fun show(message: String = "加载中...", @DrawableRes iconResource: Int) {
        this.iconResource = iconResource
        super.show(message)
    }
}

open class TipsDialogState {
    var isVisible by mutableStateOf(false)
        private set

    var message by mutableStateOf("")
        private set

    fun show(message: String = "加载中...") {
        this.message = message
        isVisible = true
    }

    fun hide() {
        isVisible = false
        message = ""
    }
}
 
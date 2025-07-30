package cn.thecover.media.core.widget.state

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
fun rememberLoadingState(): LoadingState {
    val loadingState = remember { LoadingState() }
    return loadingState
}

class LoadingState {
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
 
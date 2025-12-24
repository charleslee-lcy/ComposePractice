package cn.thecover.media.core.widget.event

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import cn.thecover.media.core.data.ToastEvent


/**
 *
 * <p> Created by CharlesLee on 2025/12/23
 * 15708478830@163.com
 */

/**
 * 使用全局页面toast
 */
fun showToast(msg: String, action: String? = null) {
    FlowBus.post(FlowEvent(action = "toast", data = ToastEvent(message = msg, action = action)))
}

/**
 * 使用页面内定义toast
 */
suspend fun SnackbarHostState.showToast(msg: String, action: String? = null) {
    showSnackbar(
        message = msg,
        actionLabel = action,
        duration = SnackbarDuration.Short
    )
}
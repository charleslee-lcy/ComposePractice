package cn.thecover.media.core.widget.event

import cn.thecover.media.core.data.ToastEvent


/**
 *
 * <p> Created by CharlesLee on 2025/12/23
 * 15708478830@163.com
 */

fun showToast(msg: String, action: String? = null) {
    FlowBus.post(FlowEvent(action = "toast", data = ToastEvent(message = msg, action = action)))
}
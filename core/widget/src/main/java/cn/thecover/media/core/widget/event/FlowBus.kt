package cn.thecover.media.core.widget.event

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch


/**
 *
 * <p> Created by CharlesLee on 2025/7/29
 * 15708478830@163.com
 */
object FlowBus {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val _events = MutableSharedFlow<FlowEvent<*>>()
    private val events = _events.asSharedFlow()

    /**
     * 发送消息
     */
    fun post(event: FlowEvent<*>) {
        scope.launch {
            _events.emit(event)
        }
    }

    /**
     * 发送消息-延时
     */
    fun postDelay(event: FlowEvent<*>, time: Long) {
        scope.launch {
            delay(time)
            _events.emit(event)
        }
    }

    /**
     * 接受消息
     */
    fun <T> observeEvent(action: String, callback: (FlowEvent<T>) -> Unit) {
        scope.launch {
            events.collect {
                if (it.action == action) {
                    callback.invoke(it as FlowEvent<T>)
                }
            }
        }
    }
}
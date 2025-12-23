package cn.thecover.media.core.widget.event


/**
 *
 * <p> Created by CharlesLee on 2025/7/29
 * 15708478830@163.com
 */
object EventConstants {
    const val ACTION_HOME = "action_home"
}

data class FlowEvent<T>(
    val action: String,
    val data: T,
    val timestamp: Long = System.currentTimeMillis(),
    val extras: List<Param> = emptyList(),
) {
    data class Param(val key: String, val value: String)
}
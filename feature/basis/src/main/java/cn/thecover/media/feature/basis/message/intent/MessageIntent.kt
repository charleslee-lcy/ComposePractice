package cn.thecover.media.feature.basis.message.intent

/**
 *  Created by Wing at 16:24 on 2025/8/14
 *
 */

sealed class MessageIntent {
    data class FetchMessageList(val loadMore: Boolean = false) : MessageIntent()
    data class UpdateMessageFilter(val type: Int) : MessageIntent()

    data class ReadMessage(val id: Long) : MessageIntent()
    data object GetUnreadMessageCount : MessageIntent()

}
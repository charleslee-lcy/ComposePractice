package cn.thecover.media.feature.basis.message

import cn.thecover.media.core.data.NetworkResponse
import cn.thecover.media.core.data.PaginatedResult
import cn.thecover.media.feature.basis.message.data.entity.MessageDataEntity
import cn.thecover.media.feature.basis.message.data.entity.MessageListRequest
import retrofit2.http.GET
import retrofit2.http.POST

/**
 *  Created by Wing at 10:12 on 2025/12/5
 *
 */

interface MessageApi {
    @POST(value = "notification/list")
    suspend fun getMessageList(
        messageListRequest: MessageListRequest
    ): NetworkResponse<PaginatedResult<MessageDataEntity>?>

    @POST(value = "readNotification")
    suspend fun readMessage(messageId: Long): NetworkResponse<Nothing?>

    @GET(value = "unReadNotificationCount")
    suspend fun getUnreadMessageCount(): NetworkResponse<Int?>
}
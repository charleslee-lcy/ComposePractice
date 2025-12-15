package cn.thecover.media.feature.basis.message

import cn.thecover.media.core.data.NetworkResponse
import cn.thecover.media.core.data.PaginatedResult
import cn.thecover.media.feature.basis.message.data.entity.MessageDataEntity
import cn.thecover.media.feature.basis.message.data.entity.MessageListRequest
import cn.thecover.media.feature.basis.message.data.entity.ReadMessageRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 *  Created by Wing at 10:12 on 2025/12/5
 *
 */

interface MessageApi {
    @POST(value = "mgr/notification/list?client=android")
    suspend fun getMessageList(
        @Body messageListRequest: MessageListRequest
    ): NetworkResponse<PaginatedResult<MessageDataEntity>?>

    @POST(value = "mgr/readNotification")
    suspend fun readMessage(@Body readMessageRequest: ReadMessageRequest): NetworkResponse<Nothing?>

    @GET(value = "mgr/unReadNotificationCount")
    suspend fun getUnreadMessageCount(): NetworkResponse<Int?>
}
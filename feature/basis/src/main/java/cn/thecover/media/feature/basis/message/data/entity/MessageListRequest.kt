package cn.thecover.media.feature.basis.message.data.entity

/**
 *  Created by Wing at 10:31 on 2025/12/5
 *
 */

data class MessageListRequest(
    val pageSize: Int,
    val type: String? = null,
    val allUser: Int = 2,
    val lastId: Long? = null
)

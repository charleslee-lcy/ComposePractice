package cn.thecover.media.feature.basis.message.data.entity

import kotlinx.serialization.Serializable

/**
 *  Created by Wing at 11:12 on 2025/8/8
 *
 */

@Serializable
data class MessageDataEntity(
    val type: Int = 0,
    val id: Long = 0L,
    val senderId: Long = 0L,
    val receiverId: Long = 0L,
    val read: Boolean = false,
    val relatedId: Long = 0L,
    val relatedType: Int = 0,
    val disable: Int = 0,
    val createTime: String? = "",
    val updateTime: String? = "",
    val content: String? = ""
) : java.io.Serializable
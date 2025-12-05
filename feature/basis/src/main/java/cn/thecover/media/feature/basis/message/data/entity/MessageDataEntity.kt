package cn.thecover.media.feature.basis.message.data.entity

import kotlinx.serialization.Serializable

/**
 *  Created by Wing at 11:12 on 2025/8/8
 *
 */

@Serializable
data class MessageDataEntity(
    val type: Int = 0,
    val id: Long = 0,
    val senderId: Long = 0,
    val receiverId: Long = 0,
    val read: Boolean = false,
    val relatedId: Long = 0,
    val relatedType: Int = 0,
    val disable: Int = 0,
    val title: String = "",
    val createTime: String = "",
    val updateTime: String = "",
    val content: String = ""
)
package cn.thecover.media.feature.basis.message.data.entity

import kotlinx.serialization.Serializable

/**
 *  Created by Wing at 11:12 on 2025/8/8
 *
 */

@Serializable
data class MessageDataEntity(
    val type: Int = 0,
    val messageId: Long = 0,
    val title: String = "",
    val time: String = "",
    val content: String = ""
)
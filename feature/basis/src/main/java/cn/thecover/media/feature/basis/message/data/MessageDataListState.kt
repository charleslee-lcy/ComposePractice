package cn.thecover.media.feature.basis.message.data

import cn.thecover.media.feature.basis.message.data.entity.MessageDataEntity

/**
 *  Created by Wing at 15:11 on 2025/8/14
 *  消息列表状态
 */

data class MessageDataListState(
    val messageDataList: List<MessageDataEntity> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null
)
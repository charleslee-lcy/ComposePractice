package cn.thecover.media.feature.review_data.data

/**
 *  Created by Wing at 09:40 on 2025/12/18
 *
 */

data class UiToastState(
    val time: Long = System.currentTimeMillis(),
    val message: String = "",
)

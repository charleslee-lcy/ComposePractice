package cn.thecover.media.core.data

import kotlinx.serialization.Serializable


/**
 *
 * <p> Created by CharlesLee on 2025/12/3
 * 15708478830@163.com
 */
@Serializable
data class HomeRequest(
    val year: Int,
    val month: Int
)

package cn.thecover.media.core.data

import kotlinx.serialization.Serializable


/**
 *
 * <p> Created by CharlesLee on 2025/7/23
 * 15708478830@163.com
 */
@Serializable
data class NetworkResponse<T>(
    val data: T? = null,
    val status: Int = 0,
    val message: String = ""){
    fun isSuccess(): Boolean = status == 0
}



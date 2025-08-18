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
    val errorCode: Int = 0,
    val errorMsg: String = "",
)

data class PageData<T>(
    val curPage: Int,
    val datas: List<T>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int,
)

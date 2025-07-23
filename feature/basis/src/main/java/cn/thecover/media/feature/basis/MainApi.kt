package cn.thecover.media.feature.basis

import cn.thecover.media.core.data.NetworkResponse
import retrofit2.http.GET


/**
 *
 * <p> Created by CharlesLee on 2025/7/23
 * 15708478830@163.com
 */
interface MainApi {
    @GET(value = "article/list/0/json")
    suspend fun getHome(): NetworkResponse<Any>
}
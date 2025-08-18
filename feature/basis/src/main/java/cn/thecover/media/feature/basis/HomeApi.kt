package cn.thecover.media.feature.basis

import cn.thecover.media.core.data.NetworkResponse
import retrofit2.http.GET


/**
 *
 * <p> Created by CharlesLee on 2025/8/15
 * 15708478830@163.com
 */
interface HomeApi {
    @GET(value = "article/list/0/json?page_size=20")
    suspend fun login(): NetworkResponse<Any>
}
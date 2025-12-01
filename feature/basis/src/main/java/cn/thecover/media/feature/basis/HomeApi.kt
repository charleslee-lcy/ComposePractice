package cn.thecover.media.feature.basis

import cn.thecover.media.core.data.LoginRequest
import cn.thecover.media.core.data.LoginResponse
import cn.thecover.media.core.data.NetworkResponse
import retrofit2.http.Body
import retrofit2.http.POST


/**
 *
 * <p> Created by CharlesLee on 2025/8/15
 * 15708478830@163.com
 */
interface HomeApi {
    @POST(value = "api/user/login")
    suspend fun login(@Body requestData: LoginRequest): NetworkResponse<LoginResponse>

    @POST(value = "api/user/logout")
    suspend fun logout(): NetworkResponse<Any>
}
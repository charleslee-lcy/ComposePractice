package cn.thecover.media.feature.basis

import cn.thecover.media.core.data.HomeInfo
import cn.thecover.media.core.data.HomeRequest
import cn.thecover.media.core.data.LoginRequest
import cn.thecover.media.core.data.LoginResponse
import cn.thecover.media.core.data.NetworkRequest
import cn.thecover.media.core.data.NetworkResponse
import cn.thecover.media.core.data.UserInfo
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
    suspend fun logout(@Body requestData: NetworkRequest): NetworkResponse<Any>

    @POST(value = "api/user/getUserInfo")
    suspend fun getUserInfo(): NetworkResponse<UserInfo>

    @POST(value = "api/data/personal/detail")
    suspend fun getHomeInfo(@Body requestData: HomeRequest): NetworkResponse<HomeInfo>
}
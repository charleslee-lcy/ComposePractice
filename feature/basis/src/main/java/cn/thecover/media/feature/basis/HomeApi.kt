package cn.thecover.media.feature.basis

import cn.thecover.media.core.data.DiffusionDataEntity
import cn.thecover.media.core.data.HomeInfo
import cn.thecover.media.core.data.HomeRequest
import cn.thecover.media.core.data.LoginRequest
import cn.thecover.media.core.data.LoginResponse
import cn.thecover.media.core.data.ManuscriptDiffusionRequest
import cn.thecover.media.core.data.ManuscriptReviewDataEntity
import cn.thecover.media.core.data.ManuscriptTopRequest
import cn.thecover.media.core.data.NetworkRequest
import cn.thecover.media.core.data.NetworkResponse
import cn.thecover.media.core.data.PaginatedResult
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

    //稿件数据-稿件TOP榜单
    @POST(value = "api/data/news/top")
    suspend fun getManuscriptReviewTopData(@Body requestBody: ManuscriptTopRequest): NetworkResponse<PaginatedResult<ManuscriptReviewDataEntity>>

    //稿件数据-传播效果
    @POST(value = "api/data/news/spreadInfo")
    suspend fun getManuscriptDiffusionData(@Body requestBody: ManuscriptDiffusionRequest): NetworkResponse<PaginatedResult<DiffusionDataEntity>>


}
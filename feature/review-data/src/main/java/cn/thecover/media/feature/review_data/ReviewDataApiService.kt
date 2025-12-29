package cn.thecover.media.feature.review_data

import cn.thecover.media.core.data.DepartmentReviewRequest
import cn.thecover.media.core.data.DepartmentTaskRequest
import cn.thecover.media.core.data.DepartmentTopRequest
import cn.thecover.media.core.data.DiffusionDataEntity
import cn.thecover.media.core.data.ManuscriptDiffusionRequest
import cn.thecover.media.core.data.ManuscriptReviewDataEntity
import cn.thecover.media.core.data.ManuscriptReviewRequest
import cn.thecover.media.core.data.ManuscriptTopRequest
import cn.thecover.media.core.data.ModifyManuscriptScoreRequest
import cn.thecover.media.core.data.NetworkResponse
import cn.thecover.media.core.data.PaginatedResult
import cn.thecover.media.core.data.UserInfo
import cn.thecover.media.feature.review_data.data.entity.DepartmentTaskDataEntity
import cn.thecover.media.feature.review_data.data.entity.DepartmentTotalDataEntity
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


/**
 *  Created by Wing at 14:51 on 2025/8/21
 *
 */

interface ReviewDataApiService {
    //稿件数据-稿件总数据排行
    @POST(value = "api/data/news/rank")
    suspend fun getManuscriptReviewData(@Body requestBody: ManuscriptReviewRequest): NetworkResponse<PaginatedResult<ManuscriptReviewDataEntity>>

    //稿件数据-稿件TOP榜单
    @POST(value = "api/data/news/top")
    suspend fun getManuscriptReviewTopData(@Body requestBody: ManuscriptTopRequest): NetworkResponse<PaginatedResult<ManuscriptReviewDataEntity>>

    //稿件数据-传播效果
    @POST(value = "api/data/news/spreadInfo")
    suspend fun getManuscriptDiffusionData(@Body requestBody: ManuscriptDiffusionRequest): NetworkResponse<PaginatedResult<DiffusionDataEntity>>

    //部门数据-稿件总数据排行
    @POST(value = "api/data/department/rank")
    suspend fun getDepartmentReviewData(@Body requestBody: DepartmentReviewRequest): NetworkResponse<PaginatedResult<DepartmentTotalDataEntity>>

    //部门数据-稿件TOP榜单
    @POST(value = "api/data/department/top")
    suspend fun getDepartmentTopData(@Body requestBody: DepartmentTopRequest): NetworkResponse<PaginatedResult<DepartmentTotalDataEntity>>

    //部门数据-任务完成情况
    @POST(value = "api/data/department/taskInfo")
    suspend fun getDepartmentTaskData(@Body requestBody: DepartmentTaskRequest): NetworkResponse<List<DepartmentTaskDataEntity>>

    //修改稿分
    @POST(value = "api/data/news/modifyScore")
    suspend fun modifyManuscriptScore(@Body requestBody: ModifyManuscriptScoreRequest): NetworkResponse<Nothing>

    //获取未读消息数
    @GET(value = "api/mgr/unReadNotificationCount")
    suspend fun getUnreadMessageCount(): NetworkResponse<Int>

    @POST(value = "api/user/getUserInfo")
    suspend fun getUserInfo(): NetworkResponse<UserInfo>
}
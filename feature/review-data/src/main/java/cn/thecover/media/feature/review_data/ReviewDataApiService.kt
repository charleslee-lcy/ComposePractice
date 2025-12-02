package cn.thecover.media.feature.review_data

import cn.thecover.media.core.data.NetworkResponse

import cn.thecover.media.feature.review_data.data.entity.DepartmentTotalDataEntity
import cn.thecover.media.feature.review_data.data.entity.ManuscriptReviewDataEntity
import cn.thecover.media.core.data.PaginatedResult
import cn.thecover.media.feature.review_data.data.entity.DepartmentTaskDataEntity
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


/**
 *  Created by Wing at 14:51 on 2025/8/21
 *
 */

interface ReviewDataApiService {
    @GET(value = "article/list/{page}/json?page_size=20")
    suspend fun getManuscriptReviewData(@Path("page") page: Int = 0): NetworkResponse<PaginatedResult<ManuscriptReviewDataEntity>>

    //部门数据-稿件总数据排行
    @POST(value = "/api/data/department/rank")
    suspend fun getDepartmentReviewData(@Body requestBody: Map<String, Any>): NetworkResponse<PaginatedResult<DepartmentTotalDataEntity>>

    //部门数据-稿件TOP榜单
    @POST(value = "/api/data/department/top")
    suspend fun getDepartmentTopData(@Body requestBody: Map<String, Any>): NetworkResponse<PaginatedResult<DepartmentTotalDataEntity>>

    //部门数据-任务完成情况
    @POST(value = "/api/data/department/taskInfo")
    suspend fun getDepartmentTaskData(@Body requestBody: Map<String, Any>): NetworkResponse<PaginatedResult<DepartmentTaskDataEntity>>
}
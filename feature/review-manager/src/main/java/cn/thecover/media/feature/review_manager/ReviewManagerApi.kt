package cn.thecover.media.feature.review_manager

import cn.thecover.media.core.data.AppealDetailRequest
import cn.thecover.media.core.data.AppealListData
import cn.thecover.media.core.data.AppealManageRequest
import cn.thecover.media.core.data.ArchiveListData
import cn.thecover.media.core.data.DepartmentAssignListData
import cn.thecover.media.core.data.DepartmentAssignRequest
import cn.thecover.media.core.data.NetworkResponse
import cn.thecover.media.core.data.PaginatedResult
import cn.thecover.media.core.data.ScoreArchiveListRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


/**
 *
 * <p> Created by CharlesLee on 2025/7/23
 * 15708478830@163.com
 */
interface ReviewManagerApi {
    //获取未读消息数
    @GET(value = "mgr/unReadNotificationCount")
    suspend fun getUnreadMessageCount(): NetworkResponse<Int>

    /**
     * 获取稿件打分列表数据
     */
    @POST(value = "api/mgr/score/news/list")
    suspend fun getScoreArchiveList(@Body request: ScoreArchiveListRequest): NetworkResponse<PaginatedResult<ArchiveListData>>

    /**
     * 部门内分配管理列表数据
     */
    @POST(value = "api/mgr/department/employee/budgetList")
    suspend fun getDepartmentAssignList(@Body request: DepartmentAssignRequest): NetworkResponse<PaginatedResult<DepartmentAssignListData>>

    /**
     * 我的申诉列表数据
     */
    @POST(value = "api/mgr/appeal/mgmtList")
    suspend fun getMyAppealList(@Body request: AppealManageRequest): NetworkResponse<PaginatedResult<AppealListData>>

    /**
     * 申诉审批列表数据
     */
    @POST(value = "api/mgr/appeal/mgmtAuditList")
    suspend fun getAppealManageList(@Body request: AppealManageRequest): NetworkResponse<PaginatedResult<AppealListData>>

    /**
     * 申诉详情数据
     */
    @POST(value = "api/mgr/appeal/detail")
    suspend fun getAppealDetailInfo(@Body request: AppealDetailRequest): NetworkResponse<AppealListData>
}
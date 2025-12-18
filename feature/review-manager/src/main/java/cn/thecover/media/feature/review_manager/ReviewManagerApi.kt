package cn.thecover.media.feature.review_manager

import cn.thecover.media.core.data.AppealDetailRequest
import cn.thecover.media.core.data.AppealListData
import cn.thecover.media.core.data.AppealManageRequest
import cn.thecover.media.core.data.AppealNewsData
import cn.thecover.media.core.data.AppealNewsRequest
import cn.thecover.media.core.data.AppealSwitchInfo
import cn.thecover.media.core.data.ArchiveListData
import cn.thecover.media.core.data.AuditDetailRequest
import cn.thecover.media.core.data.DepartmentAssignListData
import cn.thecover.media.core.data.DepartmentAssignRequest
import cn.thecover.media.core.data.DepartmentListData
import cn.thecover.media.core.data.DepartmentRemainRequest
import cn.thecover.media.core.data.NetworkRequest
import cn.thecover.media.core.data.NetworkResponse
import cn.thecover.media.core.data.NextNodeRequest
import cn.thecover.media.core.data.PaginatedResult
import cn.thecover.media.core.data.ScoreArchiveListRequest
import cn.thecover.media.core.data.ScoreLevelData
import cn.thecover.media.core.data.ScoreRuleData
import cn.thecover.media.core.data.UpdateAssignRequest
import cn.thecover.media.core.data.UpdateScoreRequest
import cn.thecover.media.core.data.UserScoreGroup
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
    @GET(value = "api/mgr/unReadNotificationCount")
    suspend fun getUnreadMessageCount(): NetworkResponse<Int>

    /**
     * 稿件打分等级情况
     */
    @POST(value = "api/mgr/score/news/statistics")
    suspend fun getScoreRuleInfo(@Body request: NetworkRequest): NetworkResponse<List<ScoreRuleData>>

    /**
     * 获取打分等级数据
     */
    @POST(value = "api/mgr/score/news/level")
    suspend fun getScoreLevelInfo(@Body request: NetworkRequest = NetworkRequest()): NetworkResponse<List<ScoreLevelData>>

    /**
     * 获取用户打分组权限
     */
    @POST(value = "api/mgr/score/news/userGroup")
    suspend fun getUserGroupInfo(@Body request: NetworkRequest = NetworkRequest()): NetworkResponse<List<UserScoreGroup>>

    /**
     * 获取稿件打分列表数据
     */
    @POST(value = "api/mgr/score/news/list")
    suspend fun getScoreArchiveList(@Body request: ScoreArchiveListRequest): NetworkResponse<PaginatedResult<ArchiveListData>>

    /**
     * 稿件打分
     */
    @POST(value = "api/mgr/news/score")
    suspend fun updateScore(@Body request: UpdateScoreRequest): NetworkResponse<Any>

    /**
     * 获取部门数据
     */
    @POST(value = "api/mgr/department/employee/deptListAuth")
    suspend fun getDepartmentList(@Body request: NetworkRequest = NetworkRequest()): NetworkResponse<List<DepartmentListData>>

    /**
     * 部门年度和月度预算剩余
     */
    @POST(value = "api/mgr/department/employee/budgetRemain")
    suspend fun getDepartmentAssignRemain(@Body request: DepartmentRemainRequest): NetworkResponse<DepartmentAssignListData>

    /**
     * 部门内分配管理列表数据
     */
    @POST(value = "api/mgr/department/employee/budgetList")
    suspend fun getDepartmentAssignList(@Body request: DepartmentAssignRequest): NetworkResponse<PaginatedResult<DepartmentAssignListData>>

    /**
     * 获取不能操作的月份
     */
    @POST(value = "api/mgr/department/employee/notEditMonth")
    suspend fun getCannotEditMonth(@Body request: DepartmentRemainRequest): NetworkResponse<List<Int>>

    /**
     * 更新预算分配
     */
    @POST(value = "api/mgr/department/employee/updateBudget")
    suspend fun updateDepartmentAssign(@Body request: UpdateAssignRequest): NetworkResponse<Any>

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
     * 申诉列表-查看稿件
     */
    @POST(value = "api/mgr/appeal/newsInfo")
    suspend fun getAppealNewsInfo(@Body request: AppealNewsRequest): NetworkResponse<AppealNewsData>

    /**
     * 申诉详情数据
     */
    @POST(value = "api/mgr/appeal/detail")
    suspend fun getAppealDetailInfo(@Body request: AppealDetailRequest): NetworkResponse<AppealListData>

    /**
     * 获取下一个结点信息
     */
    @POST(value = "api/mgr/appeal/getNextNodeId")
    suspend fun getNextNodeInfo(@Body request: NextNodeRequest): NetworkResponse<Long>

    /**
     * 申诉时间节点开关
     */
    @POST(value = "api/mgr/appeal/timelineSwitch")
    suspend fun getAuditEnable(@Body request: NetworkRequest = NetworkRequest()): NetworkResponse<AppealSwitchInfo>

    /**
     * 操作申诉
     */
    @POST(value = "api/mgr/appeal/audit")
    suspend fun auditAppealDetailInfo(@Body request: AuditDetailRequest): NetworkResponse<Any>
}
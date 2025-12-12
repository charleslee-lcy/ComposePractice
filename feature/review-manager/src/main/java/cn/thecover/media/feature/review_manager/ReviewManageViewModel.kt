package cn.thecover.media.feature.review_manager

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.thecover.media.core.common.util.formatToDateString
import cn.thecover.media.core.common.util.toMillisecond
import cn.thecover.media.core.data.AppealDetailRequest
import cn.thecover.media.core.data.AppealListData
import cn.thecover.media.core.data.AppealManageRequest
import cn.thecover.media.core.data.AppealSwitchInfo
import cn.thecover.media.core.data.ArchiveListData
import cn.thecover.media.core.data.AuditDetailRequest
import cn.thecover.media.core.data.DepartmentAssignListData
import cn.thecover.media.core.data.DepartmentAssignRequest
import cn.thecover.media.core.data.DepartmentListData
import cn.thecover.media.core.data.DepartmentRemainRequest
import cn.thecover.media.core.data.NetworkRequest
import cn.thecover.media.core.data.NextNodeRequest
import cn.thecover.media.core.data.ScoreArchiveListRequest
import cn.thecover.media.core.data.ScoreLevelData
import cn.thecover.media.core.data.ScoreRuleData
import cn.thecover.media.core.data.UpdateAssignRequest
import cn.thecover.media.core.data.UpdateScoreRequest
import cn.thecover.media.core.data.UserScoreGroup
import cn.thecover.media.core.network.BaseUiState
import cn.thecover.media.core.network.HttpStatus
import cn.thecover.media.core.network.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import retrofit2.Retrofit
import java.time.LocalDate
import javax.inject.Inject

/**
 *
 * <p> Created by CharlesLee on 2025/7/28
 * 15708478830@163.com
 */
@HiltViewModel
class ReviewManageViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle, val retrofit: dagger.Lazy<Retrofit>
) : ViewModel() {
    private val apiService = retrofit.get().create(ReviewManagerApi::class.java)
    var pageType by mutableIntStateOf(ReviewManageType.ARCHIVE_SCORE.index)
    // ======================================= 稿件打分 start ========================================
    // 开始时间
    var startLocalDate by mutableStateOf(LocalDate.now().minusMonths(1))
    val startDateText = mutableStateOf("${startLocalDate.year}-${startLocalDate.monthValue}-${startLocalDate.dayOfMonth}")
    // 结束时间
    var endLocalDate by mutableStateOf(LocalDate.now())
    val endDateText = mutableStateOf("${endLocalDate.year}-${endLocalDate.monthValue}-${endLocalDate.dayOfMonth}")
    // 本人打分状态
    val userScoreStatus = mutableIntStateOf(0)
    // 稿件打分状态
    val newsScoreStatus = mutableIntStateOf(0)
    // 搜索类型
    val searchType = mutableIntStateOf(2)
    // 搜索关键词
    val searchKeyword = mutableStateOf("")
    private val pageSize = 10
    var lastId: Long? = null
    // 稿件列表数据
    private val _archiveListDataState = MutableStateFlow(ArchiveListUiState())
    val archiveListDataState: StateFlow<ArchiveListUiState> = _archiveListDataState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ArchiveListUiState(),
    )
    private val archiveRequest = ScoreArchiveListRequest()
    val scoreRuleStatus = MutableStateFlow(listOf<ScoreRuleData>())
    val scoreLevelState = MutableStateFlow(listOf(ScoreLevelData(levelCode = "A", levelNum = 0, qualityScore = 0.0, qualityType = 0)))
    val scoreGroupState = MutableStateFlow(listOf<UserScoreGroup>())
    val updateScoreState = MutableStateFlow(BaseUiState<Any>())
    // ======================================== 稿件打分 end =========================================

    // ====================================== 部门内分配 start =======================================
    // 年度
    val departYear = mutableIntStateOf(LocalDate.now().year)
    var curDepartmentData = MutableStateFlow(DepartmentListData())
    var departmentListState = MutableStateFlow(listOf<DepartmentListData>())
    var cannotEditMonthState = MutableStateFlow(listOf<Int>())
    // 搜索类型
    val departSearchType = mutableIntStateOf(0)
    // 搜索关键词
    val departSearchKeyword = mutableStateOf("")
    var departmentLastId: Long? = null
    private val _departmentListDataState = MutableStateFlow(DepartmentAssignListUiState())
    val departmentListDataState: StateFlow<DepartmentAssignListUiState> = _departmentListDataState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DepartmentAssignListUiState(),
    )
    private val departmentRequest = DepartmentAssignRequest()
    val assignRemainStatus = MutableStateFlow(DepartmentAssignListData())
    val updateAssignState = MutableStateFlow(BaseUiState<Any>())
    // ====================================== 部门内分配 end =========================================

    // ======================================== 申诉管理 start =======================================
    val myAppealSearchType = mutableIntStateOf(0)
    val myAppealSearchKeyword = mutableStateOf("")
    val appealManageSearchType = mutableIntStateOf(0)
    var currentPos by mutableIntStateOf(0)
    val appealManageSearchKeyword = mutableStateOf("")
    var myAppealLastId: Long? = null
    var appealManageLastId: Long? = null
    private val _myAppealListDataState = MutableStateFlow(AppealListUiState())
    val myAppealListDataState: StateFlow<AppealListUiState> = _myAppealListDataState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AppealListUiState(),
    )
    private val _appealManageListDataState = MutableStateFlow(AppealListUiState())
    val appealManageListDataState: StateFlow<AppealListUiState> = _appealManageListDataState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AppealListUiState(),
    )
    val tabInfoState = MutableStateFlow(mutableListOf(0, 0, 0))

    private val myAppealRequest = AppealManageRequest()
    private val appealManageRequest = AppealManageRequest()

    val appealDetailUiState = MutableStateFlow(BaseUiState<AppealListData>())
    val auditEnableState = MutableStateFlow(BaseUiState<AppealSwitchInfo>())
    val auditDetailUiState = MutableStateFlow(BaseUiState<Any>())
    // ======================================== 申诉管理 end ========================================

    private val _unreadMessageCount = MutableStateFlow(0)
    val unreadMessageCount: StateFlow<Int> = _unreadMessageCount

    fun getScoreRuleInfo() {
        viewModelScope.launch {
            flow {
                val result = apiService.getScoreRuleInfo(NetworkRequest())
                emit(result)
            }.asResult()
                .collect { result ->
                    scoreRuleStatus.value = result.data ?: emptyList()
                }
        }
    }

    fun getScoreLevelInfo() {
        viewModelScope.launch {
            flow {
                val result = apiService.getScoreLevelInfo(NetworkRequest())
                emit(result)
            }.asResult()
                .collect { result ->
                    scoreLevelState.value = result.data ?: emptyList()
                }
        }
    }

    fun getUserGroupInfo() {
        viewModelScope.launch {
            flow {
                val result = apiService.getUserGroupInfo(NetworkRequest())
                emit(result)
            }.asResult()
                .collect { result ->
                    scoreGroupState.value = result.data ?: emptyList()
                }
        }
    }

    fun updateScore(scoreGroupId: Int, scoreLevel: Int, newsId: Long? = null) {
        viewModelScope.launch {
            flow {
                val request = UpdateScoreRequest(newsId, scoreGroupId, scoreLevel)
                val result = apiService.updateScore(request)
                emit(result)
            }.asResult()
                .collect { result ->
                    updateScoreState.value = result
                    if (result.status == HttpStatus.SUCCESS) {
                        getArchiveList(isRefresh = true)
                    }
                }
        }
    }

    fun getArchiveList(isRefresh: Boolean = true, request: ScoreArchiveListRequest = archiveRequest) {
        if (isRefresh) {
            request.lastId = null
        } else {
            request.lastId = lastId
        }
        request.startPublishDate = if (startDateText.value == "开始时间") "" else startLocalDate.toMillisecond().formatToDateString("yyyy-MM-dd")
        request.endPublishDate = if (endDateText.value == "结束时间") "" else endLocalDate.toMillisecond().formatToDateString("yyyy-MM-dd")
        when(userScoreStatus.intValue) {
            1 -> { request.userScoreStatus = "0" }
            2 -> { request.userScoreStatus = "1" }
            else -> {}
        }
        when(newsScoreStatus.intValue) {
            1 -> { request.newsScoreStatus = "0" }
            2 -> { request.newsScoreStatus = "1" }
            else -> {}
        }
        when(searchType.intValue) {
            0 -> { request.searchType = 1 }
            1 -> { request.searchType = 2 }
            else -> { request.searchType = 3 }
        }
        request.searchKeyword = searchKeyword.value.ifEmpty { null }
        viewModelScope.launch {
            flow {
                request.pageSize = pageSize
                val list = apiService.getScoreArchiveList(request)
                emit(list)
            }.asResult().collect { result ->
                    when (result.status) {
                        HttpStatus.SUCCESS -> {
                            val data = result.data?.dataList ?: emptyList()
                            lastId = result.data?.lastId ?: -1
                            _archiveListDataState.update {
                                it.copy(
                                    list = if (isRefresh) data else it.list + data,
                                    isLoading = false,
                                    isRefreshing = false,
                                    canLoadMore = lastId?.let {id ->
                                        id > 0
                                    } ?: kotlin.run {
                                        false
                                    },
                                    msg = null
                                )
                            }
                        }
                        HttpStatus.LOADING -> {
                            _archiveListDataState.update {
                                if (isRefresh) it.copy(isRefreshing = true) else it.copy(
                                    isLoading = true
                                )
                            }
                        }
                        HttpStatus.ERROR -> {
                            _archiveListDataState.update {
                                it.copy(
                                    isLoading = false,
                                    isRefreshing = false,
                                    canLoadMore = true,
                                    msg = result.errorMsg
                                )
                            }
                        }
                        else -> {}
                    }
                }
        }
    }

    fun getDepartmentAssignRemain() {
        viewModelScope.launch {
            flow {
                val request = DepartmentRemainRequest()
                request.year = departYear.intValue.toString()
                request.departmentId = curDepartmentData.value.id
                val result = apiService.getDepartmentAssignRemain(request)
                emit(result)
            }.asResult()
                .collect { result ->
                    assignRemainStatus.value = result.data ?: DepartmentAssignListData()
                }
        }
    }

    fun getDepartmentList() {
        viewModelScope.launch {
            flow {
                val list = apiService.getDepartmentList()
                emit(list)
            }.asResult().collect { result ->
                result.data?.takeIf { it.isNotEmpty() }?.let {
                    departmentListState.value = it
                    if (curDepartmentData.value.id == 0L) {
                        curDepartmentData.value = it.first()
                    }
                    getDepartmentAssignRemain()
                    getDepartmentAssignList(isRefresh = true)
                }
            }
        }
    }

    fun getDepartmentAssignList(isRefresh: Boolean = true, request: DepartmentAssignRequest = departmentRequest) {
        if (isRefresh) {
            request.lastId = null
        } else {
            request.lastId = departmentLastId
        }
        request.year = departYear.intValue.toString()
        when(departSearchType.intValue) {
            0 -> { request.searchType = 1 }
            else -> { request.searchType = 2 }
        }
        request.searchKeyword = departSearchKeyword.value
        request.departmentId = curDepartmentData.value.id
        request.pageSize = pageSize
        viewModelScope.launch {
            flow {
                val list = apiService.getDepartmentAssignList(request)
                emit(list)
            }.asResult().collect { result ->
                when (result.status) {
                    HttpStatus.SUCCESS -> {
                        val data = result.data?.dataList ?: emptyList()
                        departmentLastId = result.data?.lastId ?: -1
                        _departmentListDataState.update {
                            it.copy(
                                list = if (isRefresh) data else it.list + data,
                                isLoading = false,
                                isRefreshing = false,
                                canLoadMore = departmentLastId?.let {id ->
                                    id > 0
                                } ?: kotlin.run {
                                    false
                                },
                                msg = null
                            )
                        }
                    }
                    HttpStatus.LOADING -> {
                        _departmentListDataState.update {
                            if (isRefresh) it.copy(isRefreshing = true) else it.copy(
                                isLoading = true
                            )
                        }
                    }
                    HttpStatus.ERROR -> {
                        _departmentListDataState.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                canLoadMore = true,
                                msg = result.errorMsg
                            )
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    fun getCannotEditMonth() {
        viewModelScope.launch {
            flow {
                val list = apiService.getCannotEditMonth(
                    DepartmentRemainRequest(
                    year = departYear.intValue.toString(),
                    departmentId = curDepartmentData.value.id)
                )
                emit(list)
            }.asResult().collect { result ->
                result.data?.takeIf { it.isNotEmpty() }?.let {
                    cannotEditMonthState.value = it
                }
            }
        }
    }

    fun updateDepartmentAssign(request: UpdateAssignRequest = UpdateAssignRequest()) {
        viewModelScope.launch {
            flow {
                val result = apiService.updateDepartmentAssign(request)
                emit(result)
            }.asResult()
                .collect { result ->
                    updateAssignState.value = result
                    if (result.status == HttpStatus.SUCCESS) {
                        getDepartmentAssignRemain()
                        getDepartmentAssignList(isRefresh = true)
                    }
                }
        }
    }

    fun getAppealManageList(isRefresh: Boolean = true, request: AppealManageRequest = appealManageRequest) {
        if (isRefresh) {
            request.lastId = null
        } else {
            request.lastId = appealManageLastId
        }
        request.status = when(currentPos) {
            0 -> "1"
            1 -> "2"
            else -> "4"
        }
        when(appealManageSearchType.intValue) {
            0 -> { request.searchType = 1 }
            1 -> { request.searchType = 5 }
            else -> { request.searchType = 3 }
        }
        request.searchKeyword = appealManageSearchKeyword.value.ifEmpty { null }
        viewModelScope.launch {
            flow {
                request.pageSize = pageSize
                val list = apiService.getAppealManageList(request)
                emit(list)
            }.asResult().collect { result ->
                when (result.status) {
                    HttpStatus.SUCCESS -> {
                        val data = result.data?.dataList ?: emptyList()
                        appealManageLastId = result.data?.lastId ?: -1
                        _appealManageListDataState.update {
                            it.copy(
                                list = if (isRefresh) data else it.list + data,
                                isLoading = false,
                                isRefreshing = false,
                                canLoadMore = appealManageLastId?.let {id ->
                                    id > 0
                                } ?: kotlin.run {
                                    false
                                },
                                msg = null
                            )
                        }
                    }
                    HttpStatus.LOADING -> {
                        _appealManageListDataState.update {
                            if (isRefresh) it.copy(isRefreshing = true) else it.copy(
                                isLoading = true
                            )
                        }
                    }
                    HttpStatus.ERROR -> {
                        _appealManageListDataState.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                canLoadMore = true,
                                msg = result.errorMsg
                            )
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    fun getAppealTabInfo() {
        val request = AppealManageRequest()
        when(appealManageSearchType.intValue) {
            0 -> { request.searchType = 1 }
            1 -> { request.searchType = 5 }
            else -> { request.searchType = 3 }
        }
        request.searchKeyword = appealManageSearchKeyword.value.ifEmpty { null }
        viewModelScope.launch {
            val dealingDeferred = async { apiService.getAppealManageList(request.copy(status = "1")) }
            val passDeferred = async { apiService.getAppealManageList(request.copy(status = "2")) }
            val rejectDeferred = async { apiService.getAppealManageList(request.copy(status = "4")) }

            // 等待所有结果返回
            val dealingResult = dealingDeferred.await()
            val passResult = passDeferred.await()
            val rejectResult = rejectDeferred.await()

            // 统一处理数据条数
            val resultList = mutableListOf<Int>()
            resultList.add(dealingResult.data?.total ?: 0)
            resultList.add(passResult.data?.total ?: 0)
            resultList.add(rejectResult.data?.total ?: 0)

            // 更新UI状态
            tabInfoState.value = resultList
        }
    }

    fun getMyAppealList(isRefresh: Boolean = true, request: AppealManageRequest = myAppealRequest) {
        if (isRefresh) {
            request.lastId = null
        } else {
            request.lastId = myAppealLastId
        }
        when(myAppealSearchType.intValue) {
            0 -> { request.searchType = 1 }
            1 -> { request.searchType = 5 }
            else -> { request.searchType = 3 }
        }
        request.searchKeyword = myAppealSearchKeyword.value.ifEmpty { null }
        viewModelScope.launch {
            flow {
                request.pageSize = pageSize
                val list = apiService.getMyAppealList(request)
                emit(list)
            }.asResult().collect { result ->
                when (result.status) {
                    HttpStatus.SUCCESS -> {
                        val data = result.data?.dataList ?: emptyList()
                        myAppealLastId = result.data?.lastId ?: -1
                        _myAppealListDataState.update {
                            it.copy(
                                list = if (isRefresh) data else it.list + data,
                                isLoading = false,
                                isRefreshing = false,
                                canLoadMore = myAppealLastId?.let {id ->
                                    id > 0
                                } ?: kotlin.run {
                                    false
                                },
                                msg = null
                            )
                        }
                    }
                    HttpStatus.LOADING -> {
                        _myAppealListDataState.update {
                            if (isRefresh) it.copy(isRefreshing = true) else it.copy(
                                isLoading = true
                            )
                        }
                    }
                    HttpStatus.ERROR -> {
                        _myAppealListDataState.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                canLoadMore = true,
                                msg = result.errorMsg
                            )
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    fun getAppealDetailInfo(id: Long) {
        viewModelScope.launch {
            flow {
                val detail = apiService.getAppealDetailInfo(AppealDetailRequest(id))
                detail.data?.auditFlows?.last()?.toNode?.takeIf {
                    it > 0L
                }?.let {
                    detail.data?.let { data ->
                        data.curNodeId = it
                    }
                    val nextNode = apiService.getNextNodeInfo(NextNodeRequest(id, it))
                    nextNode.data?.takeIf { nextNodeId ->
                        nextNodeId > 0L
                    }?.let { nextNodeId ->
                        detail.data?.let { data ->
                            data.nextNodeId = nextNodeId
                        }
                    }
                }
                emit(detail)
            }.asResult()
                .collect { result ->
                    appealDetailUiState.value = result
                }
        }
    }

    /**
     * 获取申诉详情编辑开关，operation: 2-通过，4-驳回
     */
    fun getAuditEnableInfo(operation: Int) {
        viewModelScope.launch {
            flow {
                val result = apiService.getAuditEnable()
                result.data?.apply {
                    this.operation = operation
                }
                emit(result)
            }.asResult()
                .collect { result ->
                    auditEnableState.value = result
                }
        }
    }

    /**
     * 处理申诉详情，operation: 2-通过，4-驳回
     */
    fun auditAppealDetailInfo(id: Long, operation: Int, reasons: String? = null, curNodeId: Long?, nextNodeId: Long?) {
        viewModelScope.launch {
            flow {
                val request = AuditDetailRequest()
                request.id = id
                request.operation = operation
                reasons?.let {
                    request.reasons = it
                }
                request.curNodeId = curNodeId
                request.nextNodeId = nextNodeId
                val result = apiService.auditAppealDetailInfo(request)
                emit(result)
            }.asResult()
                .collect { result ->
                    auditDetailUiState.value = result
                }
        }
    }

    fun getUnreadMessageCount() {
        viewModelScope.launch {
            flow {
                val apiService = retrofit.get().create(ReviewManagerApi::class.java)
                val response = apiService.getUnreadMessageCount()
                emit(response)
            }.asResult()
                .collect { result ->
                    if (result.status == HttpStatus.SUCCESS) {
                        _unreadMessageCount.update { result.data ?: 0 }
                    } else {
                        _unreadMessageCount.update { 0 }
                    }
                }
        }
    }

}

@Serializable
data class ArchiveListUiState(
    val list: List<ArchiveListData> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val canLoadMore: Boolean = true,
    val msg: String? = null
)

@Serializable
data class DepartmentAssignListUiState(
    val list: List<DepartmentAssignListData> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val canLoadMore: Boolean = true,
    val msg: String? = null
)

@Serializable
data class AppealListUiState(
    val list: List<AppealListData> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val canLoadMore: Boolean = true,
    val msg: String? = null
)

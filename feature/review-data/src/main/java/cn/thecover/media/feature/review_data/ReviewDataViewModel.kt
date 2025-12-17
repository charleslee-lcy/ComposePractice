package cn.thecover.media.feature.review_data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.thecover.media.core.data.DiffusionDataEntity
import cn.thecover.media.core.data.ManuscriptReviewDataEntity
import cn.thecover.media.core.data.PaginatedResult
import cn.thecover.media.feature.review_data.basic_widget.intent.ReviewDataIntent
import cn.thecover.media.feature.review_data.basic_widget.intent.ReviewUIIntent
import cn.thecover.media.feature.review_data.data.DepartmentFilterState
import cn.thecover.media.feature.review_data.data.DepartmentReviewDateFilterState
import cn.thecover.media.feature.review_data.data.ManuscriptReviewFilterState
import cn.thecover.media.feature.review_data.data.entity.DepartmentTaskDataEntity
import cn.thecover.media.feature.review_data.data.entity.DepartmentTotalDataEntity
import cn.thecover.media.feature.review_data.data.params.RepositoryResult
import cn.thecover.media.feature.review_data.repository.ReviewDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *
 * <p> Created by CharlesLee on 2025/7/28
 * 15708478830@163.com
 */
@HiltViewModel
class ReviewDataViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    val repository: ReviewDataRepository
) : ViewModel() {
    private val _unreadMessageCount = MutableStateFlow(0)
    val unreadMessageCount: StateFlow<Int> = _unreadMessageCount

    //部门考核页信息流数据
    private val _departmentReviewPageState =
        MutableStateFlow(PaginatedResult<DepartmentTotalDataEntity>())
    val departmentReviewPageState: StateFlow<PaginatedResult<DepartmentTotalDataEntity>> =
        _departmentReviewPageState

    //部门top排行页信息流数据
    private val _departmentReviewTopPageState =
        MutableStateFlow(PaginatedResult<DepartmentTotalDataEntity>())
    val departmentReviewTopPageState: StateFlow<PaginatedResult<DepartmentTotalDataEntity>> =
        _departmentReviewTopPageState

    //部门任务页信息流数据
    private val _departmentTaskPageState =
        MutableStateFlow(PaginatedResult<DepartmentTaskDataEntity>())
    val departmentTaskPageState: StateFlow<PaginatedResult<DepartmentTaskDataEntity>> =
        _departmentTaskPageState

    //稿件总排行页面数据
    private val _manuscriptReviewPageState =
        MutableStateFlow(PaginatedResult<ManuscriptReviewDataEntity>())
    val manuscriptReviewPageState: StateFlow<PaginatedResult<ManuscriptReviewDataEntity>> =
        _manuscriptReviewPageState


    //稿件top排行页面数据
    private val _manuscriptReviewTopPageState =
        MutableStateFlow(PaginatedResult<ManuscriptReviewDataEntity>())
    val manuscriptReviewTopPageState: StateFlow<PaginatedResult<ManuscriptReviewDataEntity>> =
        _manuscriptReviewTopPageState

    //稿件传播排行页面数据
    private val _manuscriptReviewDiffusionPageState =
        MutableStateFlow(PaginatedResult<DiffusionDataEntity>())
    val manuscriptReviewDiffusionPageState: StateFlow<PaginatedResult<DiffusionDataEntity>> =
        _manuscriptReviewDiffusionPageState

    private val _departmentDataFilterState = MutableStateFlow(DepartmentFilterState())
    val departmentDataFilterState = _departmentDataFilterState

    private val _manuscriptTopFilterState = MutableStateFlow(
        ManuscriptReviewFilterState(
            sortField = "总分"
        )
    )
    val manuscriptTopFilterState = _manuscriptTopFilterState

    private val _manuscriptDiffusionFilterState = MutableStateFlow(
        ManuscriptReviewFilterState(
            sortField = "公式传播分",
            searchField = "稿件标题"
        )
    )
    val manuscriptDiffusionFilterState = _manuscriptDiffusionFilterState

    private val _manuscriptReviewFilterState = MutableStateFlow(
        ManuscriptReviewFilterState(
            sortField = "全部",
            searchField = "稿件标题"
        )
    )
    val manuscriptReviewFilterState = _manuscriptReviewFilterState

    private val _departmentTopFilterState = MutableStateFlow(DepartmentReviewDateFilterState())
    val departmentTopFilterState = _departmentTopFilterState

    private val _departmentTaskFilterState = MutableStateFlow(DepartmentReviewDateFilterState())
    val departmentTaskFilterState = _departmentTaskFilterState


    fun handleReviewDataIntent(intent: ReviewDataIntent) {
        when (intent) {
            //部门数据
            is ReviewDataIntent.RefreshDepartmentReviewData -> {
                loadDepartmentData()
            }

            is ReviewDataIntent.LoadMoreDepartmentReviewData -> {
                loadDepartmentData(isLoadMore = true)
            }

            //部门排行
            is ReviewDataIntent.LoadMoreDepartmentTopRanking -> {
                loadDepartmentTopData(isLoadMore = true)
            }

            is ReviewDataIntent.RefreshDepartmentTopRanking -> {
                loadDepartmentTopData(isLoadMore = false)
            }

            //部门任务
            is ReviewDataIntent.RefreshDepartmentTaskData -> {
                loadDepartmentTaskData()
            }

            is ReviewDataIntent.LoadMoreDepartmentTaskData -> {
                loadDepartmentTaskData(isLoadMore = true)
            }

            //稿件板块 数据
            is ReviewDataIntent.RefreshManuscriptReviewData -> {
                loadManuScriptReviewData()
            }

            is ReviewDataIntent.LoadMoreManuscriptReviewData -> {
                loadManuScriptReviewData(isLoadMore = true)
            }

            //稿件传播排行 数据
            is ReviewDataIntent.RefreshManuscriptDiffusion -> {
                loadManuScriptReviewDiffusionData()
            }
            //稿件总排行 数据加载更多
            is ReviewDataIntent.LoadMoreManuscriptDiffusion -> {
                loadManuScriptReviewDiffusionData(isLoadMore = true)
            }

            //稿件top排行 数据刷新
            is ReviewDataIntent.RefreshManuscriptTopRanking -> {
                loadManuScriptReviewTopData()
            }

            //稿件top排行 数据加载更多
            is ReviewDataIntent.LoadMoreManuscriptTopRanking -> {
                loadManuScriptReviewTopData(isLoadMore = true)
            }

            //修改稿分
            is ReviewDataIntent.EditManuscriptScore -> {
                updateManuscriptScore(intent.id, intent.score)
            }

            is ReviewDataIntent.GetUnreadMessageCount -> {
                viewModelScope.launch {
                    val response = repository.getUnreadMessageCount()
                    if (response is RepositoryResult.Success) {
                        _unreadMessageCount.update { response.data }
                    } else {
                        _unreadMessageCount.update { 0 }
                    }
                }
            }
        }
    }


    fun handleUIIntent(intent: ReviewUIIntent) {
        when (intent) {
            is ReviewUIIntent.UpdateDepartmentDataFilter -> {
                _departmentDataFilterState.update { state ->
                    state.copy(
                        selectedDate = intent.time,
                        sortField = intent.state
                    )
                }
            }

            is ReviewUIIntent.UpdateManuscriptTopFilter -> {
                _manuscriptTopFilterState.update { state ->
                    state.copy(
                        selectedDate = intent.time,
                        sortField = intent.state
                    )
                }
            }

            is ReviewUIIntent.UpdateManuscriptDiffusionFilter -> {
                _manuscriptDiffusionFilterState.update { state ->
                    var newState = state
                    intent.time?.let { newState = newState.copy(selectedDate = it) }
                    intent.state?.let { newState = newState.copy(sortField = it) }
                    intent.searchType?.let { newState = newState.copy(searchField = it) }
                    intent.searchText?.let { newState = newState.copy(searchText = it) }
                    newState
                }
            }

            is ReviewUIIntent.UpdateManuscriptReviewFilter -> {
                _manuscriptReviewFilterState.update { state ->
                    var newState = state
                    intent.time?.let { newState = newState.copy(selectedDate = it) }
                    intent.state?.let { newState = newState.copy(sortField = it) }
                    intent.searchType?.let { newState = newState.copy(searchField = it) }
                    intent.searchText?.let { newState = newState.copy(searchText = it) }
                    newState
                }
            }

            is ReviewUIIntent.UpdateDepartmentTaskFilter -> {
                _departmentTaskFilterState.update { newState ->
                    newState.copy(selectedDate = intent.time)
                }
            }

            is ReviewUIIntent.UpdateDepartmentTopFilter -> {
                _departmentTopFilterState.update { newState ->
                    newState.copy(selectedDate = intent.time)
                }
            }
        }
    }

    private fun updateManuscriptScore(id: Int, newScore: Double) {

        viewModelScope.launch {
            val result = repository.modifyManuscriptScore(
                newsId = id,
                score = newScore,
                year=manuscriptReviewFilterState.value.getYearAsInt(),
                month=manuscriptReviewFilterState.value.getMonthAsInt()
            )

            if (result is RepositoryResult.Success) {
                // 刷新数据
                _manuscriptReviewPageState.update { currentState ->
                    val updatedManuscripts = currentState.dataList?.map { manuscript ->
                        if (manuscript.id == id) {
                            // 创建新的对象，更新分数
                            manuscript.copy(score = newScore, leaderScoreModified = true)
                        } else {
                            manuscript
                        }
                    }

                    currentState.copy(
                        dataList = updatedManuscripts
                    )
                }
            }
        }

    }


    private fun loadManuScriptReviewData(isLoadMore: Boolean = false) {
        _manuscriptReviewPageState.update {
            if (isLoadMore) it.copy(isLoading = true) else it.copy(
                isRefreshing = true
            )
        }
        viewModelScope.launch {
            val page = if (isLoadMore) {
                _manuscriptReviewPageState.value.currentPage + 1
            } else {
                1
            }

            val filter = manuscriptReviewFilterState.value
            val result = repository.fetchManuscriptsPage(
                year = filter.getYearAsInt(),
                month = filter.getMonthAsInt(),
                page = page,
                rankType = when (filter.sortField) {
                    "分割线以上" -> 1
                    "分割线以下（清零）" -> 2
                    else -> 0
                },
                title = if (filter.searchField.contains("标题"))
                    filter.searchText else "",
                reporter = if (filter.searchField.contains("作者"))
                    filter.searchText else "",
                id = if (filter.searchField.contains("ID"))
                    filter.searchText else ""
            )

            when (result) {
                is RepositoryResult.Success -> {
                    val manuscripts = if (isLoadMore) {
                        _manuscriptReviewPageState.value.dataList?.plus(result.data.dataList ?: emptyList())
                            ?: (result.data.dataList ?: emptyList())
                    } else {
                        result.data.dataList ?: emptyList()
                    }

                    _manuscriptReviewPageState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            dataList = manuscripts,
                            currentPage = result.data.currentPage,
                            totalPages = result.data.totalPages,
                            hasNextPage = result.data.hasNextPage
                        )
                    }
                }

                is RepositoryResult.Error -> {
                    _manuscriptReviewPageState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = result.exception.message
                        )
                    }
                }

                is RepositoryResult.Loading -> {
                    // 已在开始时处理
                }
            }

        }
    }

    private fun loadManuScriptReviewDiffusionData(isLoadMore: Boolean = false) {
        _manuscriptReviewDiffusionPageState.update {
            if (isLoadMore) it.copy(isLoading = true, error = null) else it.copy(
                isRefreshing = true,
                error = null
            )
        }
        viewModelScope.launch {
            val page = if (isLoadMore) {
                _manuscriptReviewDiffusionPageState.value.currentPage + 1
            } else {
                1
            }
            val filter = manuscriptDiffusionFilterState.value
            val result = repository.fetchManuscriptDiffusionData(
                year = filter.getYearAsInt(),
                month = filter.getMonthAsInt(),
                page = page,
                sortConditions = filter.sortField,
                title = if (filter.searchField.contains("标题"))
                    filter.searchText else "",
                reporter = if (filter.searchField.contains("作者"))
                    filter.searchText else "",
                id = if (filter.searchField.contains("ID"))
                    filter.searchText else ""
            )

            when (result) {
                is RepositoryResult.Success -> {
                    val manuscripts = if (isLoadMore) {
                        (_manuscriptReviewDiffusionPageState.value.dataList ?: emptyList()) + (result.data.dataList ?: emptyList())
                    } else {
                        result.data.dataList ?: emptyList()
                    }

                    _manuscriptReviewDiffusionPageState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            dataList = manuscripts,
                            currentPage = result.data.currentPage,
                            totalPages = result.data.totalPages,
                            hasNextPage = result.data.hasNextPage
                        )
                    }
                }

                is RepositoryResult.Error -> {
                    _manuscriptReviewDiffusionPageState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = result.exception.message
                        )
                    }
                }

                is RepositoryResult.Loading -> {
                    // 已在开始时处理
                }
            }
        }
    }

    private fun loadManuScriptReviewTopData(isLoadMore: Boolean = false) {
        _manuscriptReviewTopPageState.update {
            if (isLoadMore) it.copy(isLoading = true) else it.copy(
                isRefreshing = true
            )
        }
        viewModelScope.launch {
            val page = if (isLoadMore) {
                _manuscriptReviewTopPageState.value.currentPage + 1
            } else {
                1
            }
            
            val result = repository.fetchManuscriptsTopPage(
                year = manuscriptTopFilterState.value.getYearAsInt(),
                month = manuscriptTopFilterState.value.getMonthAsInt(),
                page = page,
                sortConditions = manuscriptTopFilterState.value.sortField 
            )
            when (result) {
                is RepositoryResult.Success -> {
                    val manuscripts = if (isLoadMore) {
                        (_manuscriptReviewTopPageState.value.dataList ?: emptyList()) + (result.data.dataList ?: emptyList())
                    } else {
                        result.data.dataList ?: emptyList()
                    }
                    _manuscriptReviewTopPageState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            dataList = manuscripts,
                            currentPage = result.data.currentPage,
                            totalPages = result.data.totalPages,
                            hasNextPage = result.data.hasNextPage
                        )
                    }
                }

                is RepositoryResult.Error -> {
                    _manuscriptReviewTopPageState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = result.exception.message
                        )
                    }
                }

                is RepositoryResult.Loading -> {
                    // 已在开始时处理
                }
            }
        }
    }

    private fun loadDepartmentTaskData(isLoadMore: Boolean = false) {
        // 开始加载
        _departmentTaskPageState.update {
            if (isLoadMore) it.copy(isLoading = true, error = null) else it.copy(
                isRefreshing = true,
                error = null
            )
        }

        viewModelScope.launch {
            val page = if (isLoadMore) {
                _departmentTaskPageState.value.currentPage + 1
            } else {
                1
            }

            val result = repository.fetchDepartmentTaskPage(
                departmentDataFilterState.value.getYearAsInt(),
                departmentDataFilterState.value.getMonthAsInt(),
                page
            )

            when (result) {
                is RepositoryResult.Success -> {
                    val departmentTaskData =
                        if (isLoadMore) (_departmentTaskPageState.value.dataList ?: emptyList()) + (result.data.dataList ?: emptyList()) else (result.data.dataList ?: emptyList())

                    _departmentTaskPageState.update {
                        it.copy(
                            isRefreshing = false,
                            isLoading = false,
                            dataList = departmentTaskData,
                            currentPage = result.data.currentPage,
                            totalPages = result.data.totalPages,
                            hasNextPage = result.data.hasNextPage
                        )
                    }
                }

                is RepositoryResult.Error -> {
                    _departmentTaskPageState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = result.exception.message
                        )
                    }
                }

                is RepositoryResult.Loading -> {
                    // 已在开始时处理
                }
            }
        }

    }


    //获取部门排行总数据
    private fun loadDepartmentData(isLoadMore: Boolean = false) {
        // 开始加载
        _departmentReviewPageState.update {
            if (isLoadMore) it.copy(isLoading = true) else it.copy(
                isRefreshing = true
            )
        }
        viewModelScope.launch {
            val page = if (isLoadMore) {
                _departmentReviewPageState.value.currentPage + 1
            } else {
                1
            }
            
            val result = repository.fetchDepartmentReviewPage(
                departmentDataFilterState.value.sortField,
                departmentDataFilterState.value.getYearAsInt(),
                departmentDataFilterState.value.getMonthAsInt(),
                page
            )
            if (result is RepositoryResult.Success) {
                val departmentData =
                    if (isLoadMore) (_departmentReviewPageState.value.dataList ?: emptyList()) + (result.data.dataList ?: emptyList()) else (result.data.dataList ?: emptyList())

                // 加载完成
                _departmentReviewPageState.update {
                    it.copy(
                        dataList = departmentData,
                        isLoading = false,
                        isRefreshing = false,
                        error = null,
                        currentPage = result.data.currentPage
                    )
                }
            } else if (result is RepositoryResult.Error) {
                _departmentReviewPageState.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        error = result.exception.message
                    )
                }
            }


        }
    }


    private fun loadDepartmentTopData(isLoadMore: Boolean = false) {
        // 开始加载
        _departmentReviewTopPageState.update {
            if (isLoadMore) it.copy(isLoading = true, error = null) else it.copy(
                isRefreshing = true,
                error = null
            )
        }
        viewModelScope.launch {
            val page = if (isLoadMore) {
                _departmentReviewTopPageState.value.currentPage + 1
            } else {
                1
            }
            
            val result = repository.fetchDepartmentTopPage(
                departmentTopFilterState.value.getYearAsInt(),
                departmentTopFilterState.value.getMonthAsInt(),
                page
            )
            if (result is RepositoryResult.Success) {
                val departmentData =
                    if (isLoadMore) (_departmentReviewTopPageState.value.dataList ?: emptyList()) + (result.data.dataList ?: emptyList()) else (result.data.dataList ?: emptyList())

                // 加载完成
                _departmentReviewTopPageState.update {
                    it.copy(
                        dataList = departmentData,
                        isLoading = false,
                        isRefreshing = false,
                        error = null,
                        currentPage = result.data.currentPage
                    )
                }
            } else if (result is RepositoryResult.Error) {
                _departmentReviewTopPageState.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        error = result.exception.message
                    )
                }
            }
        }
    }
}


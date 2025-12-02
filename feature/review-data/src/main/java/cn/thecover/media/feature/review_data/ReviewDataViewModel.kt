package cn.thecover.media.feature.review_data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.thecover.media.core.data.PaginatedResult
import cn.thecover.media.feature.review_data.basic_widget.intent.ReviewDataIntent
import cn.thecover.media.feature.review_data.basic_widget.intent.ReviewUIIntent
import cn.thecover.media.feature.review_data.data.DepartmentFilterState
import cn.thecover.media.feature.review_data.data.DepartmentReviewDateFilterState
import cn.thecover.media.feature.review_data.data.ManuscriptReviewFilterState
import cn.thecover.media.feature.review_data.data.entity.DepartmentTaskDataEntity
import cn.thecover.media.feature.review_data.data.entity.DepartmentTotalDataEntity
import cn.thecover.media.feature.review_data.data.entity.DiffusionDataEntity
import cn.thecover.media.feature.review_data.data.entity.ManuscriptReviewDataEntity

import cn.thecover.media.feature.review_data.data.params.RepositoryResult
import cn.thecover.media.feature.review_data.repository.ReviewDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
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
        MutableStateFlow(PaginatedResult<ManuscriptReviewDataEntity>())
    val manuscriptReviewDiffusionPageState: StateFlow<PaginatedResult<ManuscriptReviewDataEntity>> =
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

    private fun updateManuscriptScore(id: Int, newScore: Int) {
        _manuscriptReviewPageState.update { currentState ->
            val updatedManuscripts = currentState.dataList.map { manuscript ->
                if (manuscript.id == id) {
                    // 创建新的对象，更新分数
                    manuscript.copy(score = newScore, isEdited = true)
                } else {
                    manuscript
                }
            }

            currentState.copy(
                dataList = updatedManuscripts
            )
        }
    }

    val manuscriptTestData = listOf(
        ManuscriptReviewDataEntity(
            id = 1,
            title = "《三体》",
            author = "刘慈欣",
            editor = "王伟",
            score = 4,
            basicScore = 3,
            qualityScore = 4,
            diffusionScore = 5,
            diffusionDataEntity = DiffusionDataEntity(
                28888, 288888, 222, 222, 222, 222, 222, 222, 222
            )
        ),
        ManuscriptReviewDataEntity(
            id = 11,
            title = "流浪地球",
            author = "刘慈欣",
            editor = "王伟",
            score = 4,
            basicScore = 3,
            qualityScore = 4,
            diffusionScore = 5,
            diffusionDataEntity = DiffusionDataEntity(
                2888, 288888, 222, 222, 222, 222, 222, 222, 222
            )
        ),
        ManuscriptReviewDataEntity(
            id = 2,
            title = "2025年12月份的云南省让“看一种云南生活”富饶世界云南生活富饶世界",
            author = "张明明",
            editor = "李华",
            score = 22,
            basicScore = 3,
            qualityScore = 4,
            diffusionScore = 5,
            diffusionDataEntity = DiffusionDataEntity(
                28888, 288888, 222, 222, 222, 222, 222, 222, 222
            )
        ),
        ManuscriptReviewDataEntity(
            id = 3,
            title = "“看一种云南生活”富饶世界云南生活富饶世界",
            author = "张明明",
            editor = "李华",
            score = 22,
            basicScore = 3,
            qualityScore = 4,
            diffusionScore = 5,
            diffusionDataEntity = DiffusionDataEntity(
                28888, 288888, 222, 222, 222, 222, 222, 222, 222
            )
        )
    )

    val taskTestData = listOf(
        DepartmentTaskDataEntity("${Clock.System.now()})", 150, 150, 1f, "扣系数0.1"),
        DepartmentTaskDataEntity("社会新闻部", 23, 30, 23.toFloat() / 30, "扣系数0.1"),
        DepartmentTaskDataEntity("财经新闻部", 66, 500, 243.toFloat() / 500, "扣系数0.1"),
        DepartmentTaskDataEntity("国际新闻部", 22, 33, 3 / 100.toFloat(), "扣系数0.1"),
    )

    val departTestData = listOf(
        DepartmentTotalDataEntity(
            departmentRanking = 1,
            departmentName = "部门1",
            totalScore = 100,
            totalPersons = 10,
            averageScore = 10,
            totalPayment = 1000
        ),
        DepartmentTotalDataEntity(
            departmentRanking = 2,
            departmentName = "部门2",
            totalScore = 100,
            totalPersons = 10,
            averageScore = 10,
            totalPayment = 1000
        ),
        DepartmentTotalDataEntity(
            1,
            departmentName = "社会新闻部",
        )
    )

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

            val result = repository.fetchManuscriptsPage(page)

            when (result) {
                is RepositoryResult.Success -> {
                    val manuscripts = if (isLoadMore) {
                        _manuscriptReviewPageState.value.dataList + result.data.dataList
                    } else {
                        result.data.dataList
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
            if (isLoadMore) it.copy(isLoading = true) else it.copy(
                isRefreshing = true
            )
        }
        viewModelScope.launch {
            delay(1000)
            // val result = repository.fetchManuscriptReviewData()

            val result = manuscriptTestData

            val manuscripts =
                if (isLoadMore) (_manuscriptReviewDiffusionPageState.value.dataList + result) else result
            _manuscriptReviewDiffusionPageState.update {
                it.copy(
                    isLoading = false,
                    isRefreshing = false,
                    dataList = manuscripts
                )
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
            delay(1000)
            // val result = repository.fetchManuscriptReviewData()

            val result = manuscriptTestData

            val manuscripts =
                if (isLoadMore) (_manuscriptReviewTopPageState.value.dataList + result) else result
            _manuscriptReviewTopPageState.update {
                it.copy(
                    isLoading = false,
                    isRefreshing = false,
                    dataList = manuscripts
                )
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

            val result = repository.fetchDepartmentTaskPage(
                departmentDataFilterState.value.getYearAsInt(),
                departmentDataFilterState.value.getMonthAsInt(),
                departmentTaskPageState.value.currentPage
            )

            when (result) {
                is RepositoryResult.Success -> {
                    val departmentTaskData =
                        if (isLoadMore) _departmentTaskPageState.value.dataList + result.data.dataList else result.data.dataList

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
            val result = repository.fetchDepartmentReviewPage(
                departmentDataFilterState.value.sortField,
                departmentDataFilterState.value.getYearAsInt(),
                departmentDataFilterState.value.getMonthAsInt(),
                departmentReviewPageState.value.currentPage
            )
            if (result is RepositoryResult.Success) {
                val departmentData =
                    if (isLoadMore) _departmentReviewPageState.value.dataList + result.data.dataList else result.data.dataList

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
            val result = repository.fetchDepartmentTopPage(
                departmentTopFilterState.value.getYearAsInt(),
                departmentTopFilterState.value.getMonthAsInt(),
                departmentReviewTopPageState.value.currentPage
            )
            if (result is RepositoryResult.Success) {
                val departmentData =
                    if (isLoadMore) _departmentReviewTopPageState.value.dataList + result.data.dataList else result.data.dataList

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


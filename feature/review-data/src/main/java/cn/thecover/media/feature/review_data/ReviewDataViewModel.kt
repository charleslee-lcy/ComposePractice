package cn.thecover.media.feature.review_data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.thecover.media.feature.review_data.basic_widget.intent.ReviewDataIntent
import cn.thecover.media.feature.review_data.data.DepartmentReviewState
import cn.thecover.media.feature.review_data.data.DepartmentReviewTaskState
import cn.thecover.media.feature.review_data.data.ManuscriptReviewState
import cn.thecover.media.feature.review_data.data.entity.DepartmentTaskDataEntity
import cn.thecover.media.feature.review_data.data.entity.DepartmentTotalDataEntity
import cn.thecover.media.feature.review_data.data.entity.DiffusionDataEntity
import cn.thecover.media.feature.review_data.data.entity.ManuscriptReviewDataEntity
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
) : ViewModel() {


    //部门考核页信息流数据
    private val _departmentReviewDataState = MutableStateFlow(DepartmentReviewState())
    val departmentReviewDataState: StateFlow<DepartmentReviewState> = _departmentReviewDataState

    //部门top排行页信息流数据
    private val _departmentReviewTopState = MutableStateFlow(DepartmentReviewState())
    val departmentReviewTopState: StateFlow<DepartmentReviewState> = _departmentReviewTopState

    //部门任务页信息流数据
    private val _departmentTaskDataState = MutableStateFlow(DepartmentReviewTaskState())
    val departmentTaskDataState: StateFlow<DepartmentReviewTaskState> = _departmentTaskDataState

    //稿件总排行页面数据
    private val _manuscriptReviewData =
        MutableStateFlow(ManuscriptReviewState())
    val manuscriptReviewState: StateFlow<ManuscriptReviewState> =
        _manuscriptReviewData


    //稿件top排行页面数据
    private val _manuscriptReviewTopData =
        MutableStateFlow(ManuscriptReviewState())
    val manuscriptTopRankingState: StateFlow<ManuscriptReviewState> =
        _manuscriptReviewTopData

    //稿件传播排行页面数据
    private val _manuscriptReviewDiffusionData =
        MutableStateFlow(ManuscriptReviewState())
    val manuscriptDiffusionState: StateFlow<ManuscriptReviewState> =
        _manuscriptReviewDiffusionData


    fun handleReviewDataIntent(intent: ReviewDataIntent) {
        when (intent) {
            is ReviewDataIntent.RefreshDepartmentReviewData -> {
                loadDepartmentData()
            }

            is ReviewDataIntent.LoadMoreDepartmentTaskData -> {
                loadDepartmentTaskData(isLoadMore = true)
            }

            is ReviewDataIntent.LoadMoreDepartmentReviewData -> {
                loadDepartmentData(isLoadMore = true)
            }

            ReviewDataIntent.LoadMoreDepartmentTopRanking -> {
                loadDepartmentData(isLoadMore = true)
            }
            ReviewDataIntent.RefreshDepartmentTopRanking ->{
                loadManuScriptReviewData(isLoadMore = true)
            }

            is ReviewDataIntent.RefreshDepartmentTaskData -> {
                loadDepartmentTaskData()
            }

            is ReviewDataIntent.RefreshManuscriptReviewData -> {
                loadManuScriptReviewData()
            }
            is ReviewDataIntent.LoadMoreManuscriptReviewData -> {
                loadManuScriptReviewData(isLoadMore = true)
            }

            ReviewDataIntent.LoadMoreManuscriptDiffusion ->{
                loadManuScriptReviewData(isLoadMore = true)
            }
            ReviewDataIntent.LoadMoreManuscriptReview -> {
                loadManuScriptReviewData(isLoadMore = true)
            }

            ReviewDataIntent.RefreshManuscriptDiffusion ->{
                loadManuScriptReviewData()
            }
            ReviewDataIntent.RefreshManuscriptTopranking -> {
                loadManuScriptReviewData()
            }
        }
    }

    private fun loadManuScriptReviewData(isLoadMore: Boolean = false) {
        viewModelScope.launch {
            _manuscriptReviewData.value = ManuscriptReviewState(isLoading = true)
            // val result = repository.fetchManuscriptReviewData()

            val result = listOf(
                ManuscriptReviewDataEntity(
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

            val manuscripts =
                if (isLoadMore) _manuscriptReviewData.value.manuscripts + result else result
            _manuscriptReviewData.update {
                it.copy(
                    isLoading = false,
                    manuscripts = manuscripts
                )
            }
        }
    }

    private fun loadDepartmentTaskData(isLoadMore: Boolean = false) {
        // 开始加载
        _departmentTaskDataState.update {
            if (isLoadMore) it.copy(isLoading = true) else it.copy(
                isRefreshing = true
            )
        }

        viewModelScope.launch {
            delay(500L)

            val result = listOf(
                DepartmentTaskDataEntity("${Clock.System.now()})", 150, 150, 1f, "扣系数0.1"),
                DepartmentTaskDataEntity("社会新闻部", 23, 30, 23.toFloat() / 30, "扣系数0.1"),
                DepartmentTaskDataEntity("财经新闻部", 66, 500, 243.toFloat() / 500, "扣系数0.1"),
                DepartmentTaskDataEntity("国际新闻部", 22, 33, 3 / 100.toFloat(), "扣系数0.1"),
            )

            val departmentTaskData =
                if (isLoadMore) _departmentTaskDataState.value.tasks + result else result

            _departmentTaskDataState.update {
                it.copy(isRefreshing = false, isLoading = false, tasks = departmentTaskData)
            }
        }

    }

    private fun loadDepartmentData( isLoadMore: Boolean = false) {
        // 开始加载
        _departmentReviewDataState.update {
            if (isLoadMore) it.copy(isLoading = true) else it.copy(
                isRefreshing = true
            )
        }
        viewModelScope.launch {
            delay(500L)

            val result = listOf(
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
            )

            val departmentData =
                if (isLoadMore) _departmentReviewDataState.value.departments + result else result

            // 加载完成
            _departmentReviewDataState.update {
                it.copy(
                    departments = departmentData,
                    isLoading = false,
                    isRefreshing = false
                )
            }
        }
    }

}


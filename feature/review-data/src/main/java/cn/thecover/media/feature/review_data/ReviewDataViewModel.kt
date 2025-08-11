package cn.thecover.media.feature.review_data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.thecover.media.feature.review_data.basic_widget.intent.ReviewDataIntent
import cn.thecover.media.feature.review_data.data.DepartmentReviewState
import cn.thecover.media.feature.review_data.data.DepartmentTaskDataEntity
import cn.thecover.media.feature.review_data.data.DepartmentTaskState
import cn.thecover.media.feature.review_data.data.DepartmentTotalDataEntity
import cn.thecover.media.feature.review_data.data.DiffusionDataEntity
import cn.thecover.media.feature.review_data.data.ManuscriptReviewDataEntity
import cn.thecover.media.feature.review_data.data.ManuscriptReviewState
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


    //部门考核数据
    private val _departmentReviewDataState = MutableStateFlow(DepartmentReviewState())
    val departmentReviewDataState: StateFlow<DepartmentReviewState> =
        _departmentReviewDataState

    //部门任务数据
    private val _departmentTaskDataState = MutableStateFlow(DepartmentTaskState())
    val departmentTaskDataState: StateFlow<DepartmentTaskState> = _departmentTaskDataState


    private val _manuscriptReviewData =
        MutableStateFlow(ManuscriptReviewState())
    val manuscriptReviewData: StateFlow<ManuscriptReviewState> =
        _manuscriptReviewData


    fun handleReviewDataIntent(intent: ReviewDataIntent) {
        when (intent) {
            is ReviewDataIntent.FetchDepartmentReviewData -> {
                loadDepartmentData(intent.time)
            }

            is ReviewDataIntent.FetchDepartmentTaskData -> {
                loadDepartmentTaskData()
            }

            is ReviewDataIntent.FetchManuscriptReviewData -> {
                loadManuScriptReviewData()
            }

            is ReviewDataIntent.LoadMoreDepartmentTaskData -> {
                loadMoreDepartmentTaskData()
            }
        }
    }

    private fun loadManuScriptReviewData() {
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
            _manuscriptReviewData.update {
                it.copy(
                    isLoading = false,
                    manuscripts = result
                )
            }
        }
    }

    private fun loadDepartmentTaskData() {
        // 开始加载
        _departmentTaskDataState.update { it.copy(isRefreshing = true) }

        viewModelScope.launch {
            delay(2000L)
            val departmentTaskData = listOf(
                DepartmentTaskDataEntity("${Clock.System.now()})", 150, 150, 1f, "扣系数0.1"),
                DepartmentTaskDataEntity("社会新闻部", 23, 30, 23.toFloat() / 30, "扣系数0.1"),
                DepartmentTaskDataEntity("教育事业部", 243, 500, 243.toFloat() / 500, "扣系数0.1"),
                DepartmentTaskDataEntity("科创部", 3, 100, 3 / 100.toFloat(), "扣系数0.1"),

                DepartmentTaskDataEntity("实用生活部", 88, 150, 1f, "扣系数0.1"),
                DepartmentTaskDataEntity("科技创新部", 99, 300, 23.toFloat() / 30, "扣系数0.1"),
                DepartmentTaskDataEntity("财经新闻部", 66, 500, 243.toFloat() / 500, "扣系数0.1"),
                DepartmentTaskDataEntity("国际新闻部", 22, 33, 3 / 100.toFloat(), "扣系数0.1"),)

            _departmentTaskDataState.update {
                it.copy(isRefreshing = false, tasks = departmentTaskData)
            }
        }

    }

    private fun loadMoreDepartmentTaskData() {
        // 开始加载
        _departmentTaskDataState.update { it.copy(isLoadingMore = true) }

        viewModelScope.launch {
            delay(1000L)

            val departmentTaskData =_departmentTaskDataState.value.tasks + listOf(
                DepartmentTaskDataEntity("时政部", 150, 150, 1f, "扣系数0.1"),
                DepartmentTaskDataEntity("社会新闻部", 23, 30, 23.toFloat() / 30, "扣系数0.1"),
                DepartmentTaskDataEntity("教育事业部", 243, 500, 243.toFloat() / 500, "扣系数0.1"),
                DepartmentTaskDataEntity("科创部", 3, 100, 3 / 100.toFloat(), "扣系数0.1"),)

            _departmentTaskDataState.update {
                it.copy(isLoadingMore = false, tasks = departmentTaskData)
            }
        }

    }

    private fun loadDepartmentData(time: String) {
        // 开始加载
        _departmentReviewDataState.update { it.copy(isLoading = true) }
        val departmentData = listOf(
            DepartmentTotalDataEntity(
                departmentRanking = 1,
                departmentName = "部门1_$time",
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
        // 加载完成
        _departmentReviewDataState.update {
            it.copy(
                departments = departmentData,
                isLoading = false
            )
        }
    }

}


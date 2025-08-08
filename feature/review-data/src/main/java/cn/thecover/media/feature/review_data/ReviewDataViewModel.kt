package cn.thecover.media.feature.review_data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.thecover.media.feature.review_data.basic_widget.intent.ReviewDataIntent
import cn.thecover.media.feature.review_data.data.DepartmentReviewState
import cn.thecover.media.feature.review_data.data.DepartmentTaskDataEntity
import cn.thecover.media.feature.review_data.data.DepartmentTaskState
import cn.thecover.media.feature.review_data.data.DepartmentTotalDataEntity
import cn.thecover.media.feature.review_data.data.ManuscriptReviewDataEntity
import cn.thecover.media.feature.review_data.data.ManuscriptReviewState
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
                loadDepartmentData()

            }

            is ReviewDataIntent.FetchDepartmentTaskData -> {

                    loadDepartmentTaskData()

            }
        }
    }

    private fun loadDepartmentTaskData() {
        // 开始加载
        _departmentTaskDataState.update { it.copy(isLoading = true) }
        val departmentTaskData = listOf(
            DepartmentTaskDataEntity("时政部", 150, 150, 1f, "扣系数0.1"),
            DepartmentTaskDataEntity("社会新闻部", 23, 30, 23.toFloat() / 30, "扣系数0.1"),
            DepartmentTaskDataEntity("教育事业部", 243, 500, 243.toFloat() / 500, "扣系数0.1"),
            DepartmentTaskDataEntity("科创部", 3, 100, 3 / 100.toFloat(), "扣系数0.1"),

            )
        _departmentTaskDataState.update {
            it.copy(isLoading = false, tasks =departmentTaskData)
        }
    }

    private fun loadDepartmentData() {
        // 开始加载
        _departmentReviewDataState.update { it.copy(isLoading = true) }
        val departmentData = listOf(
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
        // 加载完成
        _departmentReviewDataState.update {
            it.copy(
                departments = departmentData,
                isLoading = false
            )
        }
    }

}


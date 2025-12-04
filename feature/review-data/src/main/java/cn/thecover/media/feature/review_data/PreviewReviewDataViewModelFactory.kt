package cn.thecover.media.feature.review_data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cn.thecover.media.core.data.NetworkResponse
import cn.thecover.media.core.data.PaginatedResult
import cn.thecover.media.feature.review_data.data.entity.*
import cn.thecover.media.feature.review_data.data.params.*
import cn.thecover.media.feature.review_data.repository.ReviewDataRepository

// 简化的预览 ViewModel 工厂
class PreviewReviewDataViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PreviewReviewDataViewModel() as T
    }
}

// 预览专用的简化 ViewModel
class PreviewReviewDataViewModel : ViewModel() {
    // 这里可以添加预览需要的状态和方法
    // 由于是预览，可以返回空数据或模拟数据
}

// 简化的预览 ApiService，只返回空数据或模拟数据
class PreviewReviewDataApiService : ReviewDataApiService {
    override suspend fun getManuscriptReviewData(requestBody: ManuscriptReviewRequest): NetworkResponse<PaginatedResult<ManuscriptReviewDataEntity>> {
        return NetworkResponse(PaginatedResult(dataList = emptyList()))
    }

    override suspend fun getManuscriptReviewTopData(requestBody: ManuscriptTopRequest): NetworkResponse<PaginatedResult<ManuscriptReviewDataEntity>> {
        return NetworkResponse(PaginatedResult(dataList = emptyList()))
    }

    override suspend fun getManuscriptDiffusionData(requestBody: ManuscriptDiffusionRequest): NetworkResponse<PaginatedResult<DiffusionDataEntity>> {
        return NetworkResponse(PaginatedResult(dataList = emptyList()))
    }

    override suspend fun getDepartmentReviewData(requestBody: DepartmentReviewRequest): NetworkResponse<PaginatedResult<DepartmentTotalDataEntity>> {
        return NetworkResponse(PaginatedResult(dataList = emptyList()))
    }

    override suspend fun getDepartmentTopData(requestBody: DepartmentTopRequest): NetworkResponse<PaginatedResult<DepartmentTotalDataEntity>> {
        return NetworkResponse(PaginatedResult(dataList = emptyList()))
    }

    override suspend fun getDepartmentTaskData(requestBody: DepartmentTaskRequest): NetworkResponse<PaginatedResult<DepartmentTaskDataEntity>> {
        return NetworkResponse(PaginatedResult(dataList = emptyList()))
    }

    override suspend fun modifyManuscriptScore(requestBody: ModifyManuscriptScoreRequest): NetworkResponse<Nothing> {
        return NetworkResponse(null)
    }
}

package cn.thecover.media.feature.review_data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cn.thecover.media.core.data.NetworkResponse
import cn.thecover.media.core.data.PaginatedResult
import cn.thecover.media.feature.review_data.data.entity.DepartmentTaskDataEntity

import cn.thecover.media.feature.review_data.data.entity.DepartmentTotalDataEntity
import cn.thecover.media.feature.review_data.data.entity.ManuscriptReviewDataEntity
import cn.thecover.media.feature.review_data.repository.ReviewDataRepository

// 创建一个简单的 ViewModel 工厂用于 Preview
class PreviewReviewDataViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ReviewDataViewModel(
            savedStateHandle = SavedStateHandle(),
            repository = ReviewDataRepository(
                reviewApiService = FakeReviewApiService()
            ) // 使用假数据 Repository
        ) as T
    }
}



// 创建假的 ApiService
class FakeReviewApiService : ReviewDataApiService {
    override suspend fun getManuscriptReviewData(page: Int): NetworkResponse<PaginatedResult<ManuscriptReviewDataEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun getDepartmentReviewData(requestBody: Map<String, Any>): NetworkResponse<PaginatedResult<DepartmentTotalDataEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun getDepartmentTopData(requestBody: Map<String, Any>): NetworkResponse<PaginatedResult<DepartmentTotalDataEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun getDepartmentTaskData(requestBody: Map<String, Any>): NetworkResponse<PaginatedResult<DepartmentTaskDataEntity>> {
        TODO("Not yet implemented")
    }

}

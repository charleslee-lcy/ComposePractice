package cn.thecover.media.feature.review_data.repository

import cn.thecover.media.feature.review_data.ReviewDataApiService
import cn.thecover.media.feature.review_data.data.entity.DepartmentTaskDataEntity
import cn.thecover.media.feature.review_data.data.entity.DepartmentTotalDataEntity
import cn.thecover.media.feature.review_data.data.entity.ManuscriptReviewDataEntity
import cn.thecover.media.core.data.PaginatedResult
import cn.thecover.media.feature.review_data.data.params.RepositoryResult
import cn.thecover.media.feature.review_data.data.params.SortConditions
import jakarta.inject.Inject

/**
 *  Created by Wing at 14:53 on 2025/8/21
 *
 */
 class ReviewDataRepository @Inject constructor(
    private val reviewApiService: ReviewDataApiService
) {
    // 稿件相关数据
     suspend fun fetchManuscriptsPage(page: Int): RepositoryResult<PaginatedResult<ManuscriptReviewDataEntity>> {
        return try {
            val response = reviewApiService.getManuscriptReviewData(page)
            if (response.status == 0) {
                val body = response.data ?: throw Exception("Empty response")
                RepositoryResult.Success(
                    PaginatedResult(
                        dataList = body.dataList,
                        currentPage = body.currentPage,
                        totalPages = body.total,
                        hasNextPage = body.currentPage < body.total
                    )
                )
            } else {
                RepositoryResult.Error(Exception(response.message))
            }
        } catch (e: Exception) {
            RepositoryResult.Error(e)
        }
    }

    // 部门考核数据
    suspend fun fetchDepartmentReviewPage(page: Int): RepositoryResult<PaginatedResult<DepartmentTotalDataEntity>> {
        // 实现部门考核数据获取逻辑
        TODO("实现部门考核数据获取")
    }

    // 部门任务数据
    suspend fun fetchDepartmentTaskPage(page: Int): RepositoryResult<PaginatedResult<DepartmentTaskDataEntity>> {
        // 实现部门任务数据获取逻辑
        try {
            val response = reviewApiService.getDepartmentReviewData(mapOf(
                "year" to "2025",
                "month" to "08",
                "page" to "",
                "page_size" to "20",
                "sortConditions" to listOf<SortConditions>(
                    SortConditions(
                        field = "total_count",
                        direction = "desc"
                    )
                )
            ))

        }catch (e: Exception){

        }
        TODO("实现部门任务数据获取")
    }

    // 其他数据类型...
}


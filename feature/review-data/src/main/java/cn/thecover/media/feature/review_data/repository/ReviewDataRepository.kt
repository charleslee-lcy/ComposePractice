package cn.thecover.media.feature.review_data.repository

import cn.thecover.media.feature.review_data.ReviewDataApiService
import cn.thecover.media.feature.review_data.data.entity.DepartmentTaskDataEntity
import cn.thecover.media.feature.review_data.data.entity.DepartmentTotalDataEntity
import cn.thecover.media.feature.review_data.data.entity.ManuscriptReviewDataEntity
import cn.thecover.media.core.data.PaginatedResult
import cn.thecover.media.feature.review_data.data.entity.DiffusionDataEntity
import cn.thecover.media.feature.review_data.data.params.RepositoryResult
import cn.thecover.media.feature.review_data.data.params.SortConditions
import cn.thecover.media.feature.review_data.data.params.SortConditions.Companion.DEPT_DATA_AVERAGE_SCORE
import cn.thecover.media.feature.review_data.data.params.SortConditions.Companion.NEWS_DATA_FORMULA_SPREAD_SCORE
import jakarta.inject.Inject

/**
 *  Created by Wing at 14:53 on 2025/8/21
 *
 */
class ReviewDataRepository @Inject constructor(
    private val reviewApiService: ReviewDataApiService
) {
    // 稿件相关数据
    suspend fun fetchManuscriptsPage(
        year: Int,
        month: Int,
        page: Int,
        rankType: Int = 0,
        reporter: String = "",
        title: String = "",
        id: String = ""
    ): RepositoryResult<PaginatedResult<ManuscriptReviewDataEntity>> {
        return try {
            val response = reviewApiService.getManuscriptReviewData(
                mapOf(
                    "newsId" to id,
                    "newsTitle" to title,
                    "rankType" to rankType,
                    "reporterName" to reporter,

                    "year" to year,
                    "month" to month,
                    "page" to page,
                    "page_size" to "20",
                )
            )
            if (response.isSuccess()) {
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

    //稿件 TOP
    suspend fun fetchManuscriptsTopPage(
        year: Int,
        month: Int,
        page: Int,
        rankType: Int = 0,
        reporter: String = "",
        title: String = "",
        id: String = ""
    ): RepositoryResult<PaginatedResult<ManuscriptReviewDataEntity>> {
        return try {
            val response = reviewApiService.getManuscriptReviewTopData(
                mapOf(
                    "newsId" to id,
                    "newsTitle" to title,
                    "rankType" to rankType,
                    "reporterName" to reporter,

                    "year" to year,
                    "month" to month,
                    "page" to page,
                    "page_size" to "20",
                )
            )
            if (response.isSuccess()) {
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

    //稿件传播效果
    suspend fun fetchManuscriptDiffusionData(
        year: Int,
        month: Int,
        page: Int,
        sortConditions: String = "",
        reporter: String = "",
        title: String = "",
        id: String = ""
    ): RepositoryResult<PaginatedResult<DiffusionDataEntity>> {
        return try {
            val response = reviewApiService.getManuscriptDiffusionData(
                mapOf(
                    "newsId" to id,
                    "newsTitle" to title,
                    "sortConditions" to listOf(
                        SortConditions.putSortConditions(sortConditions)
                    ),
                    "reporterName" to reporter,

                    "year" to year,
                    "month" to month,
                    "page" to page,
                    "page_size" to "20",
                )
            )
            if (response.isSuccess()) {
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
    suspend fun fetchDepartmentReviewPage(
        filter: String,
        year: Int,
        month: Int,
        page: Int
    ): RepositoryResult<PaginatedResult<DepartmentTotalDataEntity>> {
        // 实现部门考核数据获取逻辑
        return try {
            val response = reviewApiService.getDepartmentReviewData(
                mapOf(
                    "year" to year,
                    "month" to month,
                    "page" to page,
                    "page_size" to "20",
                    "sortConditions" to listOf(
                        SortConditions.putSortConditions(filter)
                    )
                )
            )

            if (response.isSuccess()) {
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

    // 部门任务数据
    suspend fun fetchDepartmentTaskPage(
        year: Int,
        month: Int,
        page: Int
    ): RepositoryResult<PaginatedResult<DepartmentTaskDataEntity>> {

        return try {
            val response = reviewApiService.getDepartmentTaskData(
                mapOf(
                    "year" to year,
                    "month" to month,
                    "page" to page,
                    "page_size" to "20"
                )
            )

            if (response.isSuccess()) {
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


    suspend fun fetchDepartmentTopPage(
        year: Int,
        month: Int,
        page: Int
    ): RepositoryResult<PaginatedResult<DepartmentTotalDataEntity>> {
        // 实现部门考核数据获取逻辑
        return try {
            val response = reviewApiService.getDepartmentTopData(
                mapOf(
                    "year" to year,
                    "month" to month,
                    "page" to page,
                    "page_size" to "10",
                    "sortConditions" to DEPT_DATA_AVERAGE_SCORE
                )
            )

            if (response.isSuccess()) {
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
}


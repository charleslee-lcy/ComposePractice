package cn.thecover.media.feature.review_data.data.params

/**
 *  Created by Wing at 15:15 on 2025/8/21
 *
 */

data class PaginatedResult<T>(
    val dataList: List<T> = emptyList<T>(),
    val currentPage: Int = 0,
    val totalPages: Int = 0,
    val hasNextPage: Boolean = false,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
)
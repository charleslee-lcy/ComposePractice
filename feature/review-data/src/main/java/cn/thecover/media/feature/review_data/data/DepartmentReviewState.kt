package cn.thecover.media.feature.review_data.data

import cn.thecover.media.feature.review_data.data.entity.DepartmentTotalDataEntity

/**
 *  Created by Wing at 15:56 on 2025/8/12
 *
 */



data class DepartmentReviewState(
    val departments: List<DepartmentTotalDataEntity> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null
)
package cn.thecover.media.feature.review_data.data

import cn.thecover.media.feature.review_data.data.entity.DepartmentTaskDataEntity

/**
 *  Created by Wing at 16:02 on 2025/8/12
 *
 */

data class DepartmentReviewTaskState(
    val tasks: List<DepartmentTaskDataEntity> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false
)

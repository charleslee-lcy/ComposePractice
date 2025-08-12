package cn.thecover.media.feature.review_data.data

import cn.thecover.media.feature.review_data.data.entity.ManuscriptReviewDataEntity

/**
 *  Created by Wing at 15:59 on 2025/8/12
 *
 */

data class ManuscriptReviewState(
    val manuscripts: List<ManuscriptReviewDataEntity> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false
)




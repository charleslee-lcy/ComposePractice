package cn.thecover.media.feature.review_data.data

/**
 *  Created by Wing at 16:21 on 2025/8/12
 *
 */

data class TaskReviewPageState(
    val data: DepartmentReviewTaskState = DepartmentReviewTaskState(),
    val filter: DepartmentReviewDateFilterState = DepartmentReviewDateFilterState()
)
data class TopRankingPageState(
    val data: DepartmentReviewState = DepartmentReviewState(),
    val filter: DepartmentReviewDateFilterState = DepartmentReviewDateFilterState()
)
data class TotalReviewPageState(
    val data: DepartmentReviewState = DepartmentReviewState(),
    val filter: DepartmentReviewDateFilterState = DepartmentReviewDateFilterState()
)

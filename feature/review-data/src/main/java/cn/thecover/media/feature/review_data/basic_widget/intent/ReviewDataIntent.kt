package cn.thecover.media.feature.review_data.basic_widget.intent

/**
 *  Created by Wing at 14:44 on 2025/8/8
 *  数据考核列表数据意图管理
 */

sealed class ReviewDataIntent {

    data object RefreshDepartmentReviewData : ReviewDataIntent()
    data object LoadMoreDepartmentReviewData : ReviewDataIntent()

    data object RefreshDepartmentTaskData : ReviewDataIntent()
    data object LoadMoreDepartmentTaskData : ReviewDataIntent()

    data object RefreshDepartmentTopRanking: ReviewDataIntent()
    data object LoadMoreDepartmentTopRanking: ReviewDataIntent()

    data object RefreshManuscriptReviewData : ReviewDataIntent()
    data object LoadMoreManuscriptReviewData : ReviewDataIntent()

    data object RefreshManuscriptTopRanking: ReviewDataIntent()
    data object LoadMoreManuscriptTopRanking : ReviewDataIntent()

    data object RefreshManuscriptDiffusion : ReviewDataIntent()
    data object LoadMoreManuscriptDiffusion : ReviewDataIntent()

    data class EditManuscriptScore(val id: Int, val score: Double) : ReviewDataIntent()
    data object GetUnreadMessageCount : ReviewDataIntent()
}
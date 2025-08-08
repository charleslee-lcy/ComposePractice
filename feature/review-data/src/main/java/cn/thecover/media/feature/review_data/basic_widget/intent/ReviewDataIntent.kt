package cn.thecover.media.feature.review_data.basic_widget.intent

/**
 *  Created by Wing at 14:44 on 2025/8/8
 *  数据考核意图管理
 */

sealed class ReviewDataIntent {

    data class FetchDepartmentReviewData(val time: String) : ReviewDataIntent()
    data class FetchDepartmentTaskData(val time: String) : ReviewDataIntent()
    data class FetchManuscriptReviewData(val time: String) : ReviewDataIntent()
}
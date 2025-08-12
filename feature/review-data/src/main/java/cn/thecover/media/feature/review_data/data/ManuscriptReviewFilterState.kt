package cn.thecover.media.feature.review_data.data

/**
 *  Created by Wing at 16:04 on 2025/8/12
 *
 */

data class ManuscriptReviewFilterState(
    val selectedDate: String = "",
    val sortField: String = "一级媒体转载数",
    val searchField: String = "稿件标题",
    val searchText: String = ""
)

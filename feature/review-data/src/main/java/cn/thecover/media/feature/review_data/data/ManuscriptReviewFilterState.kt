package cn.thecover.media.feature.review_data.data

import java.time.LocalDate

/**
 *  Created by Wing at 16:04 on 2025/8/12
 *
 */

data class ManuscriptReviewFilterState(
    override val selectedDate: String = "${LocalDate.now().year}年${LocalDate.now().monthValue}月",
    val sortField: String = "",
    val searchField: String = "",
    val searchText: String = ""
): FilterState()

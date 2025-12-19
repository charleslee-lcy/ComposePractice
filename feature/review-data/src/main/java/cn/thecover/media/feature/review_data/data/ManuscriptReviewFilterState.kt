package cn.thecover.media.feature.review_data.data

import java.time.LocalDate

/**
 *  Created by Wing at 16:04 on 2025/8/12
 *
 */

data class ManuscriptReviewFilterState(
    override val selectedDate: String = "${if (LocalDate.now().monthValue == 1) LocalDate.now().year - 1 else LocalDate.now().year}年${if (LocalDate.now().monthValue == 1) 12 else LocalDate.now().monthValue - 1}月",
    val sortField: String = "",
    val searchField: String = "",
    val searchText: String = "",
    val time: Long = System.currentTimeMillis()
): FilterState()

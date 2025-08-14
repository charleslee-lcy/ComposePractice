package cn.thecover.media.feature.review_data.data

import java.time.LocalDate


/**
 *  Created by Wing at 16:03 on 2025/8/12
 *
 */

data class DepartmentReviewDateFilterState(
    val selectedDate: String = "${LocalDate.now().year}年${LocalDate.now().monthValue}月",
)

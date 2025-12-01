package cn.thecover.media.feature.review_data.data.params

/**
 *  Created by Wing at 17:16 on 2025/12/1
 *
 */

data class SortConditions(
    val field: String,
    val direction: String
){
    companion object {
        const val ASC = "ASC"
        const val DESC = "DESC"

        const val DEPT_DATA_PERSON_COUNT = "DEPT_DATA_PERSON_COUNT"//-部门总人数
        const val DEPT_DATA_TOTAL_MONEY = "DEPT_DATA_TOTAL_MONEY"//-部门总稿费
        const val DEPT_DATA_TOTAL_SCORE = "DEPT_DATA_TOTAL_SCORE"//-部门总分
        const val DEPT_DATA_AVERAGE_SCORE = "DEPT_DATA_AVERAGE_SCORE"//-部门平均分
    }
}

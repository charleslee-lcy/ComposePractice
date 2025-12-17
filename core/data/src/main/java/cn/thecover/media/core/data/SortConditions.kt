package cn.thecover.media.core.data

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

        const val NEWS_DATA_READ_COUNT = "NEWS_DATA_READ_COUNT"//-阅读数
        const val NEWS_DATA_SHARE_COUNT = "NEWS_DATA_SHARE_COUNT"//-分享数
        const val NEWS_DATA_LIKE_COUNT = "NEWS_DATA_LIKE_COUNT"//-点赞数
        const val NEWS_DATA_COMMENT_COUNT = "NEWS_DATA_COMMENT_COUNT"//-评论数
        const val NEWS_DATA_CORE_MEDIA_REPRINT_COUNT = "NEWS_DATA_CORE_MEDIA_REPRINT_COUNT"//-核心媒体转载数
        const val NEWS_DATA_LEVEL1_MEDIA_REPRINT_COUNT = "NEWS_DATA_LEVEL1_MEDIA_REPRINT_COUNT"//-一级媒体转载数
        const val NEWS_DATA_LEVEL2_MEDIA_REPRINT_COUNT = "NEWS_DATA_LEVEL2_MEDIA_REPRINT_COUNT"//-二级媒体转载数
        const val NEWS_DATA_FORMULA_SPREAD_SCORE = "NEWS_DATA_FORMULA_SPREAD_SCORE"//-公式传播分
        const val NEWS_DATA_FINAL_SPREAD_SCORE = "NEWS_DATA_FINAL_SPREAD_SCORE"//-最终传播分
        const val NEWS_DATA_BASIC_SCORE = "NEWS_DATA_BASIC_SCORE"//-稿件榜单基础分
        const val NEWS_DATA_QUALITY_SCORE = "NEWS_DATA_QUALITY_SCORE"//-稿件榜单质量分
        const val NEWS_DATA_SPREAD_SCORE = "NEWS_DATA_SPREAD_SCORE"//-稿件榜单传播分
        const val NEWS_DATA_FINAL_SCORE = "NEWS_DATA_FINAL_SCORE"//-稿件榜单最终分

        fun putSortConditions(conditions: String, direction: String = DESC): SortConditions {
            return when(conditions){
                "部门总稿费" -> SortConditions(DEPT_DATA_TOTAL_MONEY,direction)
                "部门总分" -> SortConditions(DEPT_DATA_TOTAL_SCORE,direction)
                "部门人员平均分" -> SortConditions(DEPT_DATA_AVERAGE_SCORE,direction)
                "部门总人数" -> SortConditions(DEPT_DATA_PERSON_COUNT,direction)
                "阅读数" -> SortConditions(NEWS_DATA_READ_COUNT,direction)
                "分享数" -> SortConditions(NEWS_DATA_SHARE_COUNT,direction)
                "点赞数" -> SortConditions(NEWS_DATA_LIKE_COUNT,direction)
                "评论数" -> SortConditions(NEWS_DATA_COMMENT_COUNT,direction)
                "核心媒体转载数" -> SortConditions(NEWS_DATA_CORE_MEDIA_REPRINT_COUNT,direction)
                "一级媒体转载数" -> SortConditions(NEWS_DATA_LEVEL1_MEDIA_REPRINT_COUNT,direction)
                "二级媒体转载数" -> SortConditions(NEWS_DATA_LEVEL2_MEDIA_REPRINT_COUNT,direction)
                "公式传播分" -> SortConditions(NEWS_DATA_FORMULA_SPREAD_SCORE,direction)
                "最终传播分" -> SortConditions(NEWS_DATA_FINAL_SPREAD_SCORE,direction)
                "基础分" -> SortConditions(NEWS_DATA_BASIC_SCORE,direction)
                "质量分" -> SortConditions(NEWS_DATA_QUALITY_SCORE,direction)
                "传播分" -> SortConditions(NEWS_DATA_SPREAD_SCORE,direction)
                "总分" -> SortConditions(NEWS_DATA_FINAL_SCORE,direction)
                else -> SortConditions(conditions, direction)
            }
        }
    }
}

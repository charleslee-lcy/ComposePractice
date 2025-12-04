package cn.thecover.media.feature.review_data.data.params

/**
 * 稿件数据请求参数
 */
data class ManuscriptReviewRequest(
    val newsId: String = "",
    val newsTitle: String = "",
    val rankType: Int = 0,
    val reporterName: String = "",
    val year: Int,
    val month: Int,
    val page: Int,
    val page_size: String = "20"
)

/**
 * 稿件TOP数据请求参数
 */
data class ManuscriptTopRequest(
    val newsId: String = "",
    val newsTitle: String = "",
    val sortConditions: List<SortConditions> = emptyList(),
    val reporterName: String = "",
    val year: Int,
    val month: Int,
    val page: Int,
    val page_size: String = "20"
)

/**
 * 稿件传播效果数据请求参数
 */
data class ManuscriptDiffusionRequest(
    val newsId: String = "",
    val newsTitle: String = "",
    val sortConditions: List<SortConditions> = emptyList(),
    val reporterName: String = "",
    val year: Int,
    val month: Int,
    val page: Int,
    val page_size: String = "20"
)

/**
 * 部门考核数据请求参数
 */
data class DepartmentReviewRequest(
    val year: Int,
    val month: Int,
    val page: Int,
    val page_size: String = "20",
    val sortConditions: List<SortConditions> = emptyList()
)

/**
 * 部门任务数据请求参数
 */
data class DepartmentTaskRequest(
    val year: Int,
    val month: Int,
    val page: Int,
    val page_size: String = "20"
)

/**
 * 部门TOP数据请求参数
 */
data class DepartmentTopRequest(
    val year: Int,
    val month: Int,
    val page: Int,
    val page_size: String = "10",
    val sortConditions: List<SortConditions> = emptyList()
)

/**
 * 修改稿分请求参数
 */
data class ModifyManuscriptScoreRequest(
    val newsId: Int,
    val modifyScore: Double,
    val year: Int,
    val month: Int,
)
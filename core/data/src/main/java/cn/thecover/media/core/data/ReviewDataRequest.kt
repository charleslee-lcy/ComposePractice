package cn.thecover.media.core.data

/**
 * 稿件数据请求参数
 */
data class ManuscriptReviewRequest(
    val ynNewsId: String = "",
    val newsTitle: String = "",
    val rankType: Int = 0,
    val reporterName: String = "",
    val year: Int,
    val month: Int,
    val pageSize: String = "20",
    val lastId: Long = -1
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
    val pageSize: String = "10",
    val lastId: Long = -1
)

/**
 * 稿件传播效果数据请求参数
 */
data class ManuscriptDiffusionRequest(
    val ynNewsId: String = "",
    val newsTitle: String = "",
    val sortConditions: List<SortConditions> = emptyList(),
    val reporterName: String = "",
    val year: Int,
    val month: Int,
    val pageSize: String = "20",
    val lastId: Long = -1,
)

/**
 * 部门考核数据请求参数
 */
data class DepartmentReviewRequest(
    val year: Int,
    val month: Int,
    val pageSize: String = "20",
    val sortConditions: List<SortConditions> = emptyList(),
    val lastId: Long = -1,
)

/**
 * 部门任务数据请求参数
 */
data class DepartmentTaskRequest(
    val year: Int,
    val month: Int,
    val pageSize: String = "20",
    val lastId: Long = -1,
)

/**
 * 部门TOP数据请求参数
 */
data class DepartmentTopRequest(
    val year: Int,
    val month: Int,
    val pageSize: String = "10",
    val sortConditions: List<SortConditions> = emptyList(),
    val lastId: Long = -1,
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
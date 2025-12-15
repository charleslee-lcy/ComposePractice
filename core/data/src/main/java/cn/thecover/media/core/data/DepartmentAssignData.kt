package cn.thecover.media.core.data

import kotlinx.serialization.Serializable


/**
 *
 * <p> Created by CharlesLee on 2025/12/9
 * 15708478830@163.com
 */

@Serializable
data class DepartmentAssignRequest(
    var lastId: Long? = null,
    var pageSize: Int = 10,
    var year: String = "",
    var departmentId: Long = 0L,
    var searchType: Int = 1, //搜索类型 1-部门人员 2-人员ID
    var searchKeyword: String = "", //搜索关键词
)

@Serializable
data class DepartmentRemainRequest(
    var year: String = "",
    var departmentId: Long = 0L
)

data class UpdateAssignRequest(
    var userId: Long = 0L,
    var year: String = "",
    var departmentId: Long = 0L,
    var janBudget: String = "",
    var febBudget: String = "",
    var marBudget: String = "",
    var aprBudget: String = "",
    var mayBudget: String = "",
    var junBudget: String = "",
    var julBudget: String = "",
    var augBudget: String = "",
    var sepBudget: String = "",
    var octBudget: String = "",
    var novBudget: String = "",
    var decBudget: String = "",
)

@Serializable
data class DepartmentAssignListData(
    val id: String = "",
    val departmentId: Long = 0L,
    val departmentName: String = "",
    val userDepartmentId: Long = 0L,
    val userDepartmentName: String = "",
    val userId: Long = 0L,
    val userName: String = "",
    val janBudget: String = "",
    val febBudget: String = "",
    val marBudget: String = "",
    val aprBudget: String = "",
    val mayBudget: String = "",
    val junBudget: String = "",
    val julBudget: String = "",
    val augBudget: String = "",
    val sepBudget: String = "",
    val octBudget: String = "",
    val novBudget: String = "",
    val decBudget: String = "",
    val yearBudget: String = "",
    val yearTotalBudget: String = "",
    val handleTime: String = "",
    val status: Int = 0
)

@Serializable
data class DepartmentListData(
    val children: List<DepartmentListData>? = null,
    val id: Long = 0L,
    val index: Int = 0,
    val name: String = "",
    val parentId: Long = 0L,
    val type: Int = 0
)
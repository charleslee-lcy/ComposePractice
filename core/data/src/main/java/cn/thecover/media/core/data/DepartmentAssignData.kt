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
    var janBudget: String = "0",
    var febBudget: String = "0",
    var marBudget: String = "0",
    var aprBudget: String = "0",
    var mayBudget: String = "0",
    var junBudget: String = "0",
    var julBudget: String = "0",
    var augBudget: String = "0",
    var sepBudget: String = "0",
    var octBudget: String = "0",
    var novBudget: String = "0",
    var decBudget: String = "0",
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
    var janBudget: String = "0",
    var febBudget: String = "0",
    var marBudget: String = "0",
    var aprBudget: String = "0",
    var mayBudget: String = "0",
    var junBudget: String = "0",
    var julBudget: String = "0",
    var augBudget: String = "0",
    var sepBudget: String = "0",
    var octBudget: String = "0",
    var novBudget: String = "0",
    var decBudget: String = "0",
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
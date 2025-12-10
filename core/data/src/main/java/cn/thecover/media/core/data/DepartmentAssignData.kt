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
    var departmentId: Long = 4,
    var searchType: Int = 1, //搜索类型 1-部门人员 2-人员ID
    var searchKeyword: String = "", //搜索关键词
)

@Serializable
data class DepartmentRemainRequest(
    var year: String = "",
    var departmentId: Long = 4
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
    val janBudget: Int = 0,
    val febBudget: Int = 0,
    val marBudget: Int = 0,
    val aprBudget: Int = 0,
    val mayBudget: Int = 0,
    val junBudget: Int = 0,
    val julBudget: Int = 0,
    val augBudget: Int = 0,
    val sepBudget: Int = 0,
    val octBudget: Int = 0,
    val novBudget: Int = 0,
    val decBudget: Int = 0,
    val yearBudget: Int = 0,
    val yearTotalBudget: Int = 0,
    val handleTime: String = "",
    val status: Int = 0
)
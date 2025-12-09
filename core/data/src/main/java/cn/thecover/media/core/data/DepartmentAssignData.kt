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
data class DepartmentAssignListData(
    val id: String,
    val departmentId: Int,
    val departmentName: String,
    val userDepartmentId: Int,
    val userDepartmentName: String,
    val userId: Int,
    val userName: String,
    val janBudget: Int,
    val febBudget: Int,
    val marBudget: Int,
    val aprBudget: Int,
    val mayBudget: Int,
    val junBudget: Int,
    val julBudget: Int,
    val augBudget: Int,
    val sepBudget: Int,
    val octBudget: Int,
    val novBudget: Int,
    val decBudget: Int,
    val yearTotalBudget: Int,
    val handleTime: String,
    val status: Int
)
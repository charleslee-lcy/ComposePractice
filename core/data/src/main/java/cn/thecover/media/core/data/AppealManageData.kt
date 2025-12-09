package cn.thecover.media.core.data

import kotlinx.serialization.Serializable


/**
 *
 * <p> Created by CharlesLee on 2025/12/9
 * 15708478830@163.com
 */

@Serializable
data class AppealManageRequest(
    var lastId: Long? = null,
    var pageSize: Int = 10,
    var status: String? = null,
    var type: String = "",
    var searchType: Int = 1, //搜索类型 1-稿件标题 2-姓名 3-申诉内容
    var searchKeyword: String? = null, //搜索关键词
)

@Serializable
data class AppealDetailRequest(
    var id: Long? = 0
)

@Serializable
data class AppealListData(
    val auditFlows: List<AuditFlow> = emptyList(),
    val content: String = "",
    val createTime: String = "",
    val creator: Long = 0L,
    val creatorName: String = "",
    val id: Long = 0L,
    val materials: List<Material> = emptyList(),
    val newsId: Long = 0L,
    val newsTitle: String = "",
    val appealTitle: String = "",
    val passTime: String = "",
    val recallTime: String = "",
    val rejectTime: String = "",
    val reply: String = "",
    val replyTime: String = "",
    val status: Int = 0,
    val submitTime: String = "",
    val type: Int = 0,
    val typeName: String = "",
    val updateTime: String = "",
    val updater: Long = 0L,
    val users: List<User> = emptyList()
)

@Serializable
data class AuditFlow(
    val appealId: Long = 0L,
    val createTime: String = "",
    val id: Long = 0L,
    val node: Long = 0L,
    val nodeName: String = "",
    val operation: Long = 0L,
    val operationName: String = "",
    val `operator`: Long = 0L,
    val operatorName: String = "",
    val remark: String = "",
    val state: Long = 0L,
    val toNode: Long = 0L,
    val toState: Long = 0L
)

@Serializable
data class Material(
    val key: String = "",
    val fileName: String = ""
)

@Serializable
data class User(
    val userId: Long = 0L,
    val userName: String = ""
)

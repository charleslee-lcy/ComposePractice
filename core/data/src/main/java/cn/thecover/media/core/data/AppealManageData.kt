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
    var searchType: Int = 1, //搜索类型 1-稿件标题 2-姓名 3-申诉理由
    var searchKeyword: String? = null, //搜索关键词
)

@Serializable
data class AppealDetailRequest(
    var id: Long? = 0L
)

@Serializable
data class AppealNewsRequest(
    var newsId: Long? = 0L
)

@Serializable
data class AuditDetailRequest(
    var id: Long = 0L,
    var curNodeId: Long? = null,
    var nextNodeId: Long? = null,
    var operation: Int = 0,
    var reasons: String? = null
)

@Serializable
data class AppealNewsData(
    val audioUrl: String? = null,
    val broadcastScore: String? = null,
    val categoryId: Int,
    val content: String,
    val createTime: Long,
    val disable: Int,
    val id: Int,
    val images: String? = null,
    val img169: String,
    val img43: String,
    val listImg: String,
    val listKind: Int,
    val newsCategory: String,
    val newsContent: String? = null,
    val newsData: String? = null,
    val newsKind: Int,
    val newsSource: Int,
    val publishTime: Long,
    val qualityScore: String? = null,
    val status: Int,
    val title: String,
    val updateTime: Long,
    val videoUrl: String? = null,
    val wapUrl: String = "",
    val writeTimeType: Int,
    val ynNewsId: Int
)

@Serializable
data class AppealListData(
    val auditFlows: List<AuditFlow> = emptyList(),
    val content: String = "",
    val createTime: String = "",
    val creator: Long = 0L,
    val creatorName: String = "",
    val id: Long = 0L,
    val material: String = "",
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
    var curNodeId: Long = 0L,
    var nextNodeId: Long = 0L,
    val users: List<User> = emptyList()
)

@Serializable
data class AuditFlow(
    val appealId: Long = 0L,
    val createTime: String = "",
    val id: Long = 0L,
    val node: Long = 0L,
    val nodeName: String = "",
    val operation: Int = 0,
    val operationName: String = "",
    val `operator`: Long = 0L,
    val operatorName: String = "",
    val remark: String? = null,
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

@Serializable
data class NextNodeRequest(
    val id: Long = 0L,
    val curNodeId: Long = 0L,
)

@Serializable
data class AppealSwitchInfo(
    val appealAuditFlowStatus: Int,
    val `data`: String,
    val id: Long,
    val inEffect: Boolean = false,
    val key: String,
    val keyDesc: String,
    val remark: String,
    val showType: Int,
    val type: Int,
    val value: Int = 0,
    var operation: Int = 0
)

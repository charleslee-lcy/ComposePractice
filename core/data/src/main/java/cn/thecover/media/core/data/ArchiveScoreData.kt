package cn.thecover.media.core.data

import kotlinx.serialization.Serializable


/**
 *
 * <p> Created by CharlesLee on 2025/12/8
 * 15708478830@163.com
 */
@Serializable
data class ScoreArchiveListRequest(
    var lastId: Long? = null,
    var pageSize: Int = 10,
    var startPublishDate: String = "2025-11-08",
    var endPublishDate: String = "2025-12-08",
    var searchType: Int = 3, //搜索类型 1-记者 2-稿件ID 3-稿件名称
    var searchKeyword: String? = null, //搜索关键词
    var newsScoreStatus: String? = null, //稿件打分状态 0-未打分 1-已打分
    var userScoreStatus: String? = null //本人打分状态 0-未打分 1-已打分
)

@Serializable
data class UpdateScoreRequest(
    val newsId: Long? = null,
    val scoreGroupId: Int = 0,
    val scoreLevel: Int = 0
)

@Serializable
data class ScoreRuleData(
    val id: Long = 0L,
    val dayCount: String = "",
    val monthCount: String = "",
    val scoreLevel: String = "",
    val scoreLevelName: String? = null,
    val order: String?= null
)

@Serializable
data class ScoreLevelData(
    val id: Int = 0,
    val levelCode: String = "",
    val levelNum: Int = 0,
    val qualityScore: Double,
    val qualityType: Int = 0
)

@Serializable
data class UserScoreGroup(
    val id: Int = 0,
    val scoreName: String = "",
    val type: Int = 0  //1-值班副总编辑 2-值班编委 3-专家
)

@Serializable
data class ArchiveListData(
    val broadcastScore: String = "",
    val categoryId: Long = 0L,
    val categoryName: String = "",
    val content: String = "",
    val disable: Int = 0,
    val id: Long = 0L,
    val img169: String = "",
    val img43: String = "",
    val audioUrl: String? = null,
    val videoUrl: String? = null,
    val listImg: String = "",
    val listKind: Int = 0,
    val newsCategoryName: String = "",
    val newsKind: Int = 0,
    val newsKindName: String = "",
    val newsScoreStatus: String = "",
    val newsSource: Int = 0,
    val newsSourceName: String = "",
    val publishTime: Long = 0L,
    val qualityScore: String = "",
    val reporters: List<Reporter> = listOf(),
    val scoreLevels: List<ScoreLevel> = listOf(),
    val scoreUserCount: Int = 0,
    val status: Int = 0,
    val title: String = "",
    val userScoreStatus: String = "",
    val wapUrl: String = "",
    val writeTimeType: Int = 0,
    val ynNewsId: Long = 0L
): java.io.Serializable

@Serializable
data class Reporter(
    val id: Long = 0L,
    val newsId: Long = 0L,
    val reporterId: Long = 0L,
    val reporterName: String = "",
    val type: Int = 0
): java.io.Serializable

@Serializable
data class ScoreLevel(
    val id: Long = 0L,
    val newsId: Long = 0L,
    val score: String? = null,
    val scoreGroupId: Int = 0,
    val scoreGroupName: String? = null,
    val scoreLevel: String = "",
    val scoreLevelName: String? = null,
    val scoreTime: String = "",
    val self: Boolean = false,
    val userId: Long = 0L,
    val userName: String? = null
): java.io.Serializable


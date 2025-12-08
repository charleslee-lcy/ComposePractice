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
    val searchType: Int = 3, //搜索类型 1-记者 2-稿件ID 3-稿件名称
    var searchKeyword: String? = null, //搜索关键词
    val newsScoreStatus: String = "", //稿件打分状态 0-未打分 1-已打分
    val userScoreStatus: String = "" //本人打分状态 0-未打分 1-已打分
)

@Serializable
data class ArchiveListData(
    val broadcastScore: String = "",
    val categoryId: Long = 0,
    val categoryName: String = "",
    val content: String = "",
    val disable: Int = 0,
    val id: Long = 0,
    val img169: String = "",
    val img43: String = "",
    val listImg: String = "",
    val listKind: Int = 0,
    val newsCategoryName: String = "",
    val newsKind: Int = 0,
    val newsKindName: String = "",
    val newsScoreStatus: String = "",
    val newsSource: Int = 0,
    val newsSourceName: String = "",
    val publishTime: Long = 0,
    val qualityScore: String = "",
    val reporters: List<Reporter> = listOf(),
    val scoreLevels: List<ScoreLevel> = listOf(),
    val scoreUserCount: Int = 0,
    val status: Int = 0,
    val title: String = "",
    val userScoreStatus: String = "",
    val wapUrl: String = "",
    val writeTimeType: Int = 0,
    val ynNewsId: Long = 0
): java.io.Serializable

@Serializable
data class Reporter(
    val id: Long = 0,
    val newsId: Long = 0,
    val reporterId: Long = 0,
    val reporterName: String = "",
    val type: Int = 0
): java.io.Serializable

@Serializable
data class ScoreLevel(
    val id: Long = 0,
    val newsId: Long = 0,
    val score: String = "",
    val scoreGroupId: Long = 0,
    val scoreGroupName: String = "",
    val scoreLevel: String = "",
    val scoreLevelName: String = "",
    val scoreTime: String = "",
    val self: Boolean = false,
    val userId: Long = 0,
    val userName: String = ""
): java.io.Serializable


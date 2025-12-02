package cn.thecover.media.feature.review_data.data.entity

import com.google.gson.annotations.SerializedName

/**
 *  Created by Wing at 15:32 on 2025/8/6
 *  稿件传播效果数据实体
 */

data class DiffusionDataEntity(
    @SerializedName("newsId")
    val id: Int = 0,
    @SerializedName("rank")
    val rank : Int = 0,
    @SerializedName("ynNewsId")
    val ynNewsId : Int = 0,
    @SerializedName("newsTitle")
    val title: String = "",
    @SerializedName("reporterList")
    val reporter: List<ReporterEntity> = emptyList(),
    val formulaSpreadScore: Int = 0,//公式传播分
    val spreadScore: Int = 0,//最终传播分
    val coreMediaReprintCount: Int = 0,//核心媒体转载
    val level1MediaReprintCount: Int = 0,//一级媒体转载数
    val level2MediaReprintCount: Int = 0,//二级媒体转载
    val readCount: Int = 0,        //阅读数
    val shareCount: Int = 0,//分享数
    val likeCount: Int = 0,//点赞数
    val commentCount: Int = 0//评论数
)
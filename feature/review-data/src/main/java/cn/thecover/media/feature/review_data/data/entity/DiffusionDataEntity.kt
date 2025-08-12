package cn.thecover.media.feature.review_data.data.entity

/**
 *  Created by Wing at 15:32 on 2025/8/6
 *  稿件传播效果数据实体
 */

data class DiffusionDataEntity(
    val basicDiffusionScore: Int = 0,//公式传播分
    val ultimateDiffusionScore: Int = 0,//最终传播分
    val coreMediaReprint: Int = 0,//核心媒体转载
    val level1MediaReprint: Int = 0,//一级媒体转载数
    val level2MediaReprint: Int = 0,//二级媒体转载
    val readNumber: Int = 0,        //阅读数
    val shareNumber: Int = 0,//分享数
    val thumbNumber: Int = 0,//点赞数
    val commentNumber: Int = 0//评论数
)
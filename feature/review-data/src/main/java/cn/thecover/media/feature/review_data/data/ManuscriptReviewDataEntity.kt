package cn.thecover.media.feature.review_data.data

/**
 *  Created by Wing at 09:47 on 2025/8/6
 *  稿件考核评分数据实体类
 */

data class ManuscriptReviewDataEntity(
    val title: String = "",
    val author: String = "",
    val editor: String = "",
    val score: Int = 0,
    val basicScore: Int = 0,
    val qualityScore: Int = 0,
    val diffusionScore: Int = 0,
    val diffusionDataEntity: DiffusionDataEntity = DiffusionDataEntity(

    )
)

package cn.thecover.media.core.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 *  Created by Wing at 09:47 on 2025/8/6
 *  稿件考核评分数据实体类
 */

data class ManuscriptReviewDataEntity(
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
    @SerializedName("editorList")
    val editor: List<ReporterEntity> = emptyList(),
    @SerializedName("cutNews")
    val isCutNews: Boolean = false,
    val leaderScoreModified: Boolean = false,
    @SerializedName("totalScore")
    val score: Double = 0.0,//稿件总分
    val addSubScore: Double = 0.0,//加减分
    @SerializedName("basicScore")
    val basicScore: Double = 0.0,//基础分
    @SerializedName("qualityScore")
    val qualityScore: Double = 0.0,//质量分
    @SerializedName("spreadScore")
    val diffusionScore: Double = 0.0,//传播分

    val diffusionDataEntity: DiffusionDataEntity = DiffusionDataEntity(

    )
): Serializable



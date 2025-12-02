package cn.thecover.media.feature.review_data.data.entity

import com.google.gson.annotations.SerializedName

/**
 *  Created by Wing at 15:58 on 2025/12/2
 *
 */

data class ReporterEntity(
    @SerializedName("reporterId")
    val id: Int = 0,
    @SerializedName("reporterName")
    val name: String = "",
)

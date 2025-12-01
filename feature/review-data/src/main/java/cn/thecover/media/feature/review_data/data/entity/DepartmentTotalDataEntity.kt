package cn.thecover.media.feature.review_data.data.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 *  Created by Wing at 15:55 on 2025/8/4
 *  部门数据实体类
 */

data class DepartmentTotalDataEntity(
    @SerializedName("rank")
    val departmentRanking: Int=0,
    @SerializedName("departmentId")
    val departmentId: Int=0,
    @SerializedName("departmentName")
    val departmentName: String,
    @SerializedName("totalScore")
    val totalScore: Int = 0,
    @SerializedName("totalPersonCount")
    val totalPersons: Int = 0,
    @SerializedName("averageScore")
    val averageScore: Int = 0,
    @SerializedName("totalMoney")
    val totalPayment: Int = 0
): Serializable


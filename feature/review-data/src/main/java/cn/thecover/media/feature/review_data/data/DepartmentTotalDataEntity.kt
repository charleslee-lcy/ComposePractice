package cn.thecover.media.feature.review_data.data

/**
 *  Created by Wing at 15:55 on 2025/8/4
 *  部门数据实体类
 */

data class DepartmentTotalDataEntity(
    val departmentRanking: Int,
    val departmentName: String,
    val totalScore: Int = 0,
    val totalPersons: Int = 0,
    val averageScore: Int = 0,
    val totalPayment: Int = 0
)

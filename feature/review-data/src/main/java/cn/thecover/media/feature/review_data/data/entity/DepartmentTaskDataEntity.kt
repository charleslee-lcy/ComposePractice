package cn.thecover.media.feature.review_data.data.entity

/**
 *  Created by Wing at 10:46 on 2025/8/5
 *  部门任务实体类
 */

data class DepartmentTaskDataEntity(
    val departmentId: Int,
    val departmentName: String?,
    val finishedPersonNum: Int,
    val taskGoalNum: Int,
    val finishPercent: Double,
    val subCoefficient: Double,
    val taskDesc: String?,
)

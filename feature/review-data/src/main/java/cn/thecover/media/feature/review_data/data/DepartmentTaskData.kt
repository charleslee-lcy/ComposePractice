package cn.thecover.media.feature.review_data.data

/**
 *  Created by Wing at 10:46 on 2025/8/5
 *  部门任务实体类
 */

data class DepartmentTaskDataEntity(
    val departmentName: String?,
    val taskFinishedPersons: Int,
    val taskTotalPersons: Int,
    val taskProgress: Float,
    val taskDesc: String?
)

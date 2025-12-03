package cn.thecover.media.core.data

import kotlinx.serialization.Serializable


/**
 *
 * <p> Created by CharlesLee on 2025/12/3
 * 15708478830@163.com
 */
@Serializable
data class HomeInfo(
    val userId: Long,
    val userName: String,
    val jobType: Int,  //职务类型 1-记者，2-校检，3-领导
    val finalScore: Long,  //绩效最终得分
    val money: Long,   //稿费编辑费
    val showMoney: Boolean,  //是否展示稿费
    val quotaBasicScore: Double,  //定额基数分
    val assessmentResult: Boolean, //考核是否合格
    val participateAssessmentCount: Long,  //部门参加考核的人数
    val passCount: Long,   //部门合格人数
    val passRate: Long,    //合格率
    val deptAverageScore: Long,  //部门平均分
    val finalCoefficient: Long,  //最终系数
    val quotaCoefficient: Long,  //定额系数
    val innerTaskGoalNum: Long,  //内参任务目标任务数
    val innerTaskFinishedNum: Long, //内参任务实际完成数
)
package cn.thecover.media.core.data

import kotlinx.serialization.Serializable


/**
 *
 * <p> Created by CharlesLee on 2025/12/3
 * 15708478830@163.com
 */
@Serializable
data class HomeInfo(
    val userId: Long = 0L,
    val userName: String = "",
    val jobType: Int = 1,  //职务类型 1-记者，2-校检，3-领导
    val finalScore: String = "--",  //绩效最终得分
    val money: String = "--",   //稿费编辑费
    val showMoney: Boolean = true,  //是否展示稿费
    val quotaBasicScore: String = "--",  //定额基数分
    val quotaFinishScore: String = "--",  //实际完成
    val assessmentResult: Boolean? = null, //考核是否合格
    val participateAssessmentCount: String = "--",  //部门参加考核的人数
    val passCount: String = "--",   //部门合格人数
    val passRate: String = "--",    //合格率
    val deptAverageScore: String = "--",  //部门平均分
    val finalCoefficient: String = "--",  //最终系数
    val quotaCoefficient: String = "--",  //定额系数
    val innerTaskGoalNum: String = "",  //内参任务目标任务数
    val innerTaskFinishedNum: String = "", //内参任务实际完成数
)
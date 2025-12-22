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
    val finalScore: String = "0",  //绩效最终得分
    val layoutScore: Double = 0.0,  //布局得分
    val newsScore: Double = 0.0,  //布局得分排名

    val money: String = "0",   //稿费编辑费
    val showMoney: Boolean = true,  //是否展示稿费
    val quotaBasicScore: String = "0",  //定额基数分
    val quotaFinishScore: String = "0",  //实际完成
    val assessmentResult: Boolean? = null, //考核是否合格
    val assess: Boolean = false,  //是否考核中
    val intraAssess: Boolean = false,  //是否内部考核中
    val participateAssessmentCount: String = "0",  //部门参加考核的人数
    val passCount: String = "0",   //部门合格人数
    val passRate: String = "0",    //合格率
    val deptAverageScore: String = "0",  //部门平均分
    val finalCoefficient: String = "0",  //最终系数
    val quotaCoefficient: String = "0",  //定额系数
    val innerTaskGoalNum: String = "",  //内参任务目标任务数
    val innerTaskFinishedNum: String = "", //内参任务实际完成数
    val status: Int = 0,  //接口状态，0-正常，1-功能未开启
    val statusInfo: String = ""
)
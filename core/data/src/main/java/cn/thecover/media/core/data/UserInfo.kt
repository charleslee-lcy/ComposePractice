package cn.thecover.media.core.data

import kotlinx.serialization.Serializable


/**
 *
 * <p> Created by CharlesLee on 2025/12/2
 * 15708478830@163.com
 */
@Serializable
data class UserInfo(
    val userId: Long = 0L,
    val nickname: String = "",
    val avatar: String = "",
    val hasSubmitAppealAuth: Boolean = false, //提交申述权限
    val hasAuditAppealAuth: Boolean = false, //审核申述权限
    val hasModifyNewsScoreAuth: Boolean = false //修改稿件分数权限
)

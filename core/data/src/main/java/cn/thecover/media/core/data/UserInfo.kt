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
    val hasModifyNewsScoreAuth: Boolean = false, //修改稿件分数权限
    val userAuthCodeList: List<String>? = null //用户权限码
) {
    /**
     * 是否有稿件打分操作权限
     */
    fun hasNewsScoreOperationAuth(): Boolean {
        return userAuthCodeList?.contains("PERFORMANCE_NEWS_SCORE_OPERATION") == true
    }

    /**
     * 是否有稿件打分查看权限
     */
    fun hasNewsScoreListAuth(): Boolean {
        return userAuthCodeList?.contains("PERFORMANCE_NEWS_SCORE_LIST") == true
    }
}

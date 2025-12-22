package cn.thecover.media.feature.basis.mine

/**
 *  Created by Wing at 10:34 on 2025/7/29
 *
 */

sealed class MineIntent {

    data class GetUserInfo(val username: String) : MineIntent()

    data object ClearCache : MineIntent()

    data object GetHelpCenterUrl : MineIntent()

    data class ModifyPassword(
        val oldPassword: String,
        val password: String,
        val passwordVerify: String,
    ) : MineIntent()

    data class ModifyPasswordWithoutLogin(
        val username: String, // 用户名
        val oldPassword: String, // 临时密码（用户刚刚尝试登录时使用的密码）
        val password: String,
        val passwordVerify: String,
    ) : MineIntent()
}
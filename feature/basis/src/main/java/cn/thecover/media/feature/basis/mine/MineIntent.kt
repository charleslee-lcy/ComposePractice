package cn.thecover.media.feature.basis.mine

import cn.thecover.media.feature.basis.mine.data.requestParams.ModifyPasswordRequest

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
}
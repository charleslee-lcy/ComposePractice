package cn.thecover.media.feature.basis.mine

/**
 *  Created by Wing at 10:34 on 2025/7/29
 *
 */

sealed class MineIntent {

    data class GetUserInfo(val username: String) : MineIntent()

    data object ClearCache : MineIntent()

    data object GetHelpCenterUrl : MineIntent()
}
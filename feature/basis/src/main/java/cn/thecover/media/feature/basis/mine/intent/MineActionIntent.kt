package cn.thecover.media.feature.basis.mine.intent

import kotlinx.serialization.Serializable

/**
 *  Created by Wing at 15:21 on 2025/7/29
 *
 */

sealed class MineNavigationIntent {
    @Serializable
    data object ModifyPassword : MineNavigationIntent()
    @Serializable
    data class ModifyPasswordWithoutLogin(
        val isFromLogin: Boolean = true,
        val username: String = "",
        val tempPassword: String = ""
    ) : MineNavigationIntent()

    @Serializable
    data object HelpCenter : MineNavigationIntent()

    //非导航意图
    data object ClearCache : MineNavigationIntent()
}
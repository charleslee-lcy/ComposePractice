package cn.thecover.media.feature.basis.mine.data.requestParams

/**
 *  Created by Wing at 09:20 on 2025/12/5
 *
 */

data class ModifyPasswordRequest(
    val oldPassword: String,
    val password: String,
    val passwordVerify: String,
    val client: String="android"
)

package cn.thecover.media.feature.basis.mine.data.requestParams

/**
 *  Created by Wing at 10:00 on 2025/12/22
 *  未登录状态下修改密码的请求参数
 */

data class ModifyPasswordWithoutLoginRequest(
    val username: String, // 用户名
    val oldPassword: String, // 临时密码（用户刚刚尝试登录时使用的密码）
    val password: String,
    val passwordVerify: String,
)
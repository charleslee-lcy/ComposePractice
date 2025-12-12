package cn.thecover.media.core.data


/**
 *
 * <p> Created by CharlesLee on 2025/12/1
 * 15708478830@163.com
 */
@kotlinx.serialization.Serializable
data class LoginResponse(val token: String)

@kotlinx.serialization.Serializable
data class LoginRequest(
    val username: String,
    val password: String,
    val client: String = "android",
) : NetworkRequest()

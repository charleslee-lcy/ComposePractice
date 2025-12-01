package cn.thecover.media.core.network.recorder

import kotlinx.serialization.Serializable

/**
 *
 * @author TT
 * @since 2021-12-10
 */
@Serializable
class HttpRecordData(
    var method: String? = null,
    var host: String? = null,
    var url: String? = null,
    var headers: String? = null,
    var body: String? = null,
    var response: String? = null,
    var code: Int = 200,
    var requestTimeStamp: Long = 0,
    var responseTimeStamp: Long = 0
)
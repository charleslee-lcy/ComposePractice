package cn.thecover.media.core.network.recorder

/**
 *
 * @author TT
 * @since 2021-12-10
 */
interface IHttpRecorder {
    fun push(recordData: HttpRecordData)

    fun list() : List<HttpRecordData>
}
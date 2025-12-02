package cn.thecover.media.core.network.recorder

/**
 *
 * @author TT
 * @since 2021-12-10
 */
object HttpRecorder : IHttpRecorder {
    private var recorder: IHttpRecorder = HttpMemoryRecorder()

    override fun push(recordData: HttpRecordData) {
        recorder.push(recordData)
    }

    override fun list(): List<HttpRecordData> {
        return recorder.list()
    }
}
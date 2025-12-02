package cn.thecover.media.core.network.recorder

import java.util.*

/**
 *
 * @author TT
 * @since 2021-12-10
 */
class HttpMemoryRecorder : IHttpRecorder {
    private val recorderDeque: Deque<HttpRecordData> by lazy {
        LinkedList()
    }

    override fun push(recordData: HttpRecordData) {
        recorderDeque.push(recordData)

        if (recorderDeque.size > MAX_COUNT) {
            recorderDeque.removeFirst()
        }
    }

    override fun list(): List<HttpRecordData> {
        return recorderDeque.toList()
    }

    companion object {
        private const val MAX_COUNT = 50
    }
}
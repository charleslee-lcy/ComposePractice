package cn.thecover.media.core.network.upload

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*

/**
 * Created by scott on 2023/2/10
 * Description:
 **/
class FileRequestBody<T>(
    private val requestBody: RequestBody,
    private val callback: UploadCallBack<T>
) : RequestBody() {
    private var bufferedSink: BufferedSink? = null

    override fun contentLength(): Long {
        return requestBody.contentLength()
    }

    override fun contentType(): MediaType? {
        return requestBody.contentType()
    }

    override fun writeTo(sink: BufferedSink) {
        if (bufferedSink == null) {
            bufferedSink = createSink(sink).buffer()
        }
        bufferedSink?.let {
            //写入
            requestBody.writeTo(it)
            //必须调用flush，否则最后一部分数据可能不会被写入
            it.flush()
        }
    }

    private fun createSink(sink: Sink): Sink {
        return object: ForwardingSink(sink) {
            //当前写入字节数
            var bytesWritten = 0L
            //总字节长度，避免多次调用contentLength()方法
            var contentLength = 0L

            override fun write(source: Buffer, byteCount: Long) {
                super.write(source, byteCount)
                if (contentLength == 0L) {
                    //获得contentLength的值，后续不再调用
                    contentLength = contentLength()
                }
                //增加当前写入的字节数
                bytesWritten += byteCount
                //回调
                callback.onProgress(contentLength, bytesWritten)
            }
        }
    }
}
package cn.thecover.media.core.network.recorder

import cn.thecover.media.core.network.upload.FileRequestBody
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset
import kotlin.math.min

// 打印请求body的最大值（表单除外)
private const val REQUEST_BODY_MAX_LENGTH = (128 + 64).toLong()

class HttpRecorderInterceptor(private val baseUrl: String) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val recordData = HttpRecordData().apply {
            requestTimeStamp = System.currentTimeMillis()
        }

        val response = chain.proceed(request)

        with(recordData) {
            responseTimeStamp = System.currentTimeMillis()
            method = request.method
            host = baseUrl
            // url 为去除baseUrl和后面参数的中间部分
            val urlString = request.url.toString()
            val startIndex = if (urlString.startsWith(baseUrl)) {
                baseUrl.length
            } else {
                0
            }
            val endIndex = if (urlString.contains("?")) {
                urlString.indexOf("?")
            } else {
                urlString.length
            }
            url = if (startIndex < endIndex) {
                urlString.substring(startIndex, endIndex)
            } else {
                urlString
            }
            var headerString = request.headers.toString()
            if (headerString.endsWith("\n")) {
                headerString = headerString.substring(0, headerString.length - 1)
            }
            headers = headerString
            // 请求参数
            val bodyStringBuffer = StringBuffer()
            val queryNameSet = request.url.queryParameterNames
            if (queryNameSet.isNotEmpty()) {
                // url中有请求参数
                for (query in queryNameSet) {
                    val values = request.url.queryParameterValues(query)
                    for (value in values) {
                        bodyStringBuffer.append(query).append(": ").append(value ?: "").append("\n")
                    }
                }
            } else {
                // 查看body中是否有请求参
                request.body?.let {
                    when(it) {
                        is MultipartBody,
                        is FileRequestBody<*> -> {}
                        is FormBody -> {
                            for (index in 0 until it.size) {
                                bodyStringBuffer.append("${it.name(index)}: ${it.value(index)}")
                                    .append("\n")
                            }
                        }
                        else -> {
                            val buffer = Buffer()
                            it.writeTo(buffer)
                            var charset: Charset? = Charset.forName("UTF-8")
                            if (it.contentType() != null) {
                                val contentType = it.contentType()
                                if (contentType != null) {
                                    charset = contentType.charset(charset)
                                    if (charset == null) {
                                        charset = Charset.forName("UTF-8")
                                    }
                                }
                            }

                            charset?.let { char ->
                                bodyStringBuffer.append(
                                    buffer.readString(
                                        min(REQUEST_BODY_MAX_LENGTH, buffer.size),
                                        char
                                    )
                                )
                            }
                        }
                    }
                }
            }

            if (bodyStringBuffer.endsWith("\n")) {
                bodyStringBuffer.delete(bodyStringBuffer.length - 1, bodyStringBuffer.length)
            }
            body = bodyStringBuffer.toString()

            this.response = response.body?.string()
            code = response.code
        }

        HttpRecorder.push(recordData)

        return response.newBuilder()
            .body((recordData.response ?: "").toResponseBody("UTF-8".toMediaTypeOrNull()))
            .build()
    }
}
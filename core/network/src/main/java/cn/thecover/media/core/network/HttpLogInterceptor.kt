package cn.thecover.media.core.network

import android.text.TextUtils
import cn.thecover.media.core.network.upload.FileRequestBody
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import java.io.IOException
import java.net.URLDecoder
import java.nio.charset.Charset
import kotlin.math.min

// 打印请求body的最大值（表单除外)
private const val REQUEST_BODY_MAX_LENGTH = (128 + 64).toLong()

class HttpLogInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val response = chain.proceed(request)

        // 打印请求到的信息
        val sb = StringBuilder()
        sb.append("********** Request **********\n")
            .append(request.method).append(" - ")
            // 打印的url中文显示不出来
            .append(URLDecoder.decode(request.url.toString())).append("\n")
            .append("--- headers ---").append("\n").append(request.headers)

        request.body?.let { requestBody ->
            when(requestBody) {
                is MultipartBody,
                is FileRequestBody<*> -> {}
                is FormBody -> {
                    sb.append("--- body ---").append("\n")
                    for (index in 0 until requestBody.size) {
                        sb.append("${requestBody.name(index)} : ${requestBody.value(index)}")
                            .append("\n")
                    }
                }
                else -> {
                    val buffer = Buffer()
                    requestBody.writeTo(buffer)
                    var charset: Charset? = Charset.forName("UTF-8")
                    if (requestBody.contentType() != null) {
                        val contentType = requestBody.contentType()
                        if (contentType != null) {
                            charset = contentType.charset(charset)
                            if (charset == null) {
                                charset = Charset.forName("UTF-8")
                            }
                        }
                    }

                    val body = buffer.readString(min(REQUEST_BODY_MAX_LENGTH, buffer.size), charset!!)

                    if (!TextUtils.isEmpty(body)) {
                        sb.append("--- body ---").append("\n").append(body).append("\n")
                    }
                }
            }
        }

        sb.append("---------------\n")
        sb.append("********** Response **********\n")
        var content = ""
        if (response.body != null) {
            content = response.body!!.string()
            if (!TextUtils.isEmpty(content)) {
                sb.append(
                    if (content.decodeUnicode().length <= 500)
                        JsonUtil.jsonBeautify(content.decodeUnicode())
                    else content.decodeUnicode()
                ).append("\n")
            }
        }

        if (response.code == 200) {
            LogUtil.print(sb.toString(), LogUtil.DEBUG)
        } else {
            LogUtil.print(sb.toString(), LogUtil.ERROR)
        }

        return response.newBuilder().body(content.toResponseBody("UTF-8".toMediaTypeOrNull()))
            .build()
    }
}
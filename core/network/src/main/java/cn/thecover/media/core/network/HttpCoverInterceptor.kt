package cn.thecover.media.core.network

import KEY_DATA
import KEY_DATA_WITHOUT_TRANSFORM
import cn.thecover.media.core.network.upload.FileRequestBody
import com.google.gson.GsonBuilder
import okhttp3.*
import okio.Buffer

/**
 *
 * @author TT
 * @since 2021-10-26
 */
class HttpCoverInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val origin = chain.request()

        val builder = origin.newBuilder()

        // 处理header
        builder.decorateHeader()

        // 处理body（或url拼接）
        builder.decorateBody(origin)

        return chain.proceed(builder.build())
    }

    private fun Request.Builder.decorateHeader() {
        for ((key, value) in HttpManager.config.extraHeaderMap()) {
            removeHeader(key)
            if (value.isNotEmpty()) {
                addHeader(key, value)
            }
        }
    }

    private fun Request.Builder.decorateBody(origin: Request) {
        // 处理body 如果定义的请求使用了表单，则继续使用表单，否则就用url拼接方式
        when (origin.body) {
            null -> convertByUrl(origin.url)
            is FormBody -> convertByFormBody(origin.method, origin.body as FormBody)
            is MultipartBody -> convertByUrl(origin.url)
            is FileRequestBody<*> -> return
            else -> convertByRequestBody(origin.method, origin.body)
        }
    }

    private fun Request.Builder.convertByUrl(httpUrl: HttpUrl) {
        // 默认成请求参数在连接后面（GET）
        // 获取连接schema  host 等，抛弃参数
        val sb = StringBuffer().apply {
            val url = httpUrl.toString()
            val index = url.indexOf("?")
            if (index == -1) {
                append(url)
            } else {
                append(url.substring(0, index))
            }
            append("?")
        }

        // 加上额外的必传参数
        for ((key, value) in HttpManager.config.extraParamMap()) {
            sb.append(key).append("=").append(value).append("&")
        }

        // 处理原始参数，做成data参数，放入url中
        val queryNameSet = httpUrl.queryParameterNames
        if (queryNameSet.isNotEmpty()) {
            val map = mutableMapOf<String, String>()
            for (query in queryNameSet) {
                val values = httpUrl.queryParameterValues(query)
                val value = if (values.isEmpty()) null else values[values.size - 1]
                if (!value.isNullOrEmpty()) {
                    if (KEY_DATA_WITHOUT_TRANSFORM == query) {
                        sb.append("$KEY_DATA=").append(value)
                        break
                    }

                    map[query] = value
                }
            }
            if (map.isNotEmpty()) {
                sb.append("$KEY_DATA=")
                    .append(GsonBuilder().disableHtmlEscaping().create().toJson(map))
            }
        }

        if (sb.endsWith("&")) {
            sb.delete(sb.length - 1, sb.length)
        }

        url(sb.toString())
    }

    private fun Request.Builder.convertByFormBody(method: String, formBody: FormBody) {
        // 加上额外的必传参数
        val formBodyBuilder = FormBody.Builder().apply {
            for ((key, value) in HttpManager.config.extraParamMap()) {
                if (value.isNotEmpty()) {
                    add(key, value)
                }
            }
        }

        // 处理原始参数，做成data参数，放入formBody中
        val map = mutableMapOf<String, String>()
        for (index in 0 until formBody.size) {
//                Timber.i("key - ${oldFormBody.name(index)} || value - ${oldFormBody.value(index)}")
            if (KEY_DATA_WITHOUT_TRANSFORM == formBody.name(index)) {
                formBodyBuilder.add(KEY_DATA, formBody.value(index))
                method(method, formBodyBuilder.build())
                return
            }
            map[formBody.name(index)] = formBody.value(index)
//                formBodyBuilder.addEncoded(oldFormBody.encodedName(index), oldFormBody.encodedValue(index))
        }

        if (map.isNotEmpty()) {
            formBodyBuilder.add(
                KEY_DATA,
                GsonBuilder().disableHtmlEscaping().create().toJson(map)
            )
        }

        method(method, formBodyBuilder.build())
    }

    private fun Request.Builder.convertByRequestBody(method: String, body: RequestBody?) {
        // 加上额外的必传参数
        val formBodyBuilder = FormBody.Builder().apply {
            for ((key, value) in HttpManager.config.extraParamMap()) {
                if (value.isNotEmpty()) {
                    add(key, value)
                }
            }
        }

        val json = body?.run {
            val buffer = Buffer()
            writeTo(buffer)
            buffer.readUtf8()
        } ?: "{}"

        formBodyBuilder.add(KEY_DATA, json)

        method(method, formBodyBuilder.build())
    }
}
package cn.thecover.media.core.network

import android.content.Context
import cn.thecover.media.core.common.Constants
import cn.thecover.media.core.widget.datastore.getToken
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.Buffer
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject


/**
 *
 * <p> Created by CharlesLee on 2025/12/1
 * 15708478830@163.com
 */
class AuthInterceptor @Inject constructor(
    @ApplicationContext private val context: Context
) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestUrl = request.url
        val requestBody = request.body
        if (!request.url.toString().startsWith(URLConstant.YB_BASE_URL))
            return chain.proceed(request)

        val newRequest = request.newBuilder()
        val newHttpUrl = requestUrl.newBuilder()
            .addQueryParameter("vno", Constants.APP_VERSION)
            .addQueryParameter("client", "android")
            .build()

        val token = getToken(context)
        if (token.isNotEmpty()) {
            newRequest.removeHeader("Authorization-API")
                .addHeader("Authorization-API", "bearer $token")
        }

        // 只处理POST请求且body为JSON类型
//        if (request.method == "POST" && requestBody is RequestBody) {
//            val newBody = modifyRequestBody(requestBody)
//            newRequest.method(request.method, newBody)
//        }

        newRequest.url(newHttpUrl)
        return chain.proceed(newRequest.build())
    }

    private fun modifyRequestBody(originalBody: RequestBody): RequestBody {
        // 读取原始body内容
        val contentType = originalBody.contentType()

        // 只处理JSON类型的请求体
        if (contentType?.toString()?.contains("application/json") == true) {
            try {
                // 读取原始body内容
                val buffer = Buffer()
                originalBody.writeTo(buffer)
                val bodyString = buffer.readUtf8()

                // 解析JSON并添加字段
                val jsonObject = JSONObject(bodyString)
                if (!jsonObject.has("vno")) {
                    jsonObject.put("vno", Constants.APP_VERSION)
                }

                // 创建新的请求体
                val newBody = jsonObject.toString().toRequestBody(contentType)
                return newBody
            } catch (e: Exception) {
                // 如果解析失败，返回原始请求
                e.printStackTrace()
            }
        }
        // 返回新的RequestBody
        return originalBody
    }
}
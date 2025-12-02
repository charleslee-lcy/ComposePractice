package cn.thecover.media.core.network

import android.content.Context
import cn.thecover.media.core.widget.datastore.getToken
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
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
        if (!request.url.toString().startsWith(URLConstant.YB_BASE_URL))
            return chain.proceed(request)

        val token = getToken(context)
        if (token.isEmpty()) return chain.proceed(request)

        val newRequest = request.newBuilder()
            .removeHeader("Authorization-API")
            .addHeader("Authorization-API", "bearer $token")
            .build()
        return chain.proceed(newRequest)
    }
}
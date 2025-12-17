package cn.thecover.media.core.network

import IHttpConfig
import android.annotation.SuppressLint
import cn.thecover.media.core.network.recorder.HttpRecorderInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.Proxy
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.*
import kotlin.reflect.KClass

/**
 *
 * @author TT
 * @since 2021-10-26
 */
@Suppress("unused")
object HttpManager {
    private const val TIMEOUT = 30L

    const val HTTP_CODE_PERMISSION = 401
    const val STATUS_SUCCESS = 0
    const val STATUS_SUCCESS_200 = 200

    val config: IHttpConfig by lazy {
        val iterator = ServiceLoader.load(IHttpConfig::class.java).iterator()
        if (iterator.hasNext()) {
            iterator.next()
        } else {
            throw Exception("You must need implement IHttpConfig with @AutoService(IHttpConfig::class)")
        }
    }

    fun <T : Any> obtainApi(cls: KClass<T>) : T {
        return retrofit.create(cls.java)
    }

    fun <T : Any> obtainApi(cls: Class<T>): T {
        return retrofit.create(cls)
    }

    // retrofit
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(config.baseUrl)
            .client(mOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    fun retrofit(): Retrofit {
        return retrofit
    }

    // OkHttp 客户端
    private val mOkHttpClient: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .sslSocketFactory(getSSLSocketFactory(), getTrustManager())
            .hostnameVerifier(getHostnameVerifier())
        // 未进行头部和body变化前的日志
        if (config.enablePreLog()) {
            builder.addInterceptor(HttpLogInterceptor())
        }
        // 添加头部和body变化
        builder.addInterceptor(config.coverInterceptor)
        // 头部和body变化后的日志
        if (config.enableRealLog()) {
            builder.addInterceptor(HttpLogInterceptor())
        }
        // 请求记录
        if (config.enableRecord()) {
            builder.addInterceptor(HttpRecorderInterceptor(config.baseUrl))
        }
        // 是否允许抓包
        if (!config.enableProxy()) {
            builder.proxy(Proxy.NO_PROXY)
        }
        builder.build()
    }

    /**
     * 获取这个SSLSocketFactory
     */
    fun getSSLSocketFactory(): SSLSocketFactory {
        return try {
            val sslContext: SSLContext = SSLContext.getInstance("SSL")
            sslContext.init(null, arrayOf<TrustManager>(getTrustManager()), SecureRandom())
            sslContext.socketFactory
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    /**
     * 获取TrustManager
     */
    @SuppressLint("CustomX509TrustManager")
    fun getTrustManager(): X509TrustManager {
        return object : X509TrustManager {
            @SuppressLint("TrustAllX509TrustManager")
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            @SuppressLint("TrustAllX509TrustManager")
            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return emptyArray()
            }
        }
    }

    /**
     * 获取HostnameVerifier
     */
    fun getHostnameVerifier(): HostnameVerifier {
        return HostnameVerifier { _, _ -> true }
    }
}
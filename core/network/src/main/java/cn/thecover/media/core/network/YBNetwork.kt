package cn.thecover.media.core.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 *
 * <p> Created by CharlesLee on 2025/7/23
 * 15708478830@163.com
 */
object YBNetwork {
    private const val YB_BASE_URL = "https://www.wanandroid.com/"

    private val ybOkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .apply {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    },
            )
            .connectTimeout(30 * 1000, TimeUnit.MILLISECONDS)
            .readTimeout(30 * 1000, TimeUnit.MILLISECONDS)
            .writeTimeout(30 * 1000, TimeUnit.MILLISECONDS)
            .build()
    }

    val ybRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(YB_BASE_URL)
            .client(ybOkHttpClient)
//            .addConverterFactory(
//                json.asConverterFactory("application/json".toMediaType()),
//            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
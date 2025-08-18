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
private val previewOkHttpClient = OkHttpClient.Builder()
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

val previewRetrofit = Retrofit.Builder()
    .baseUrl(URLConstant.YB_BASE_URL)
    .callFactory { previewOkHttpClient.newCall(it) }
    .addConverterFactory(GsonConverterFactory.create())
    .build()

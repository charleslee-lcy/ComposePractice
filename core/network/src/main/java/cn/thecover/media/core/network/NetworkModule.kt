package cn.thecover.media.core.network

import android.content.Context
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.util.DebugLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


/**
 *
 * <p> Created by CharlesLee on 2025/7/25
 * 15708478830@163.com
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun okHttpCallFactory(): Call.Factory = OkHttpClient.Builder()
        .addInterceptor(HttpLogInterceptor())
        .connectTimeout(30 * 1000, TimeUnit.MILLISECONDS)
        .readTimeout(30 * 1000, TimeUnit.MILLISECONDS)
        .writeTimeout(30 * 1000, TimeUnit.MILLISECONDS)
        .build()

    @Provides
    @Singleton
    fun ybRetrofit(
        networkJson: Json,
        okHttpCallFactory: dagger.Lazy<Call.Factory>,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(URLConstant.YB_BASE_URL)
        .callFactory { okHttpCallFactory.get().newCall(it) }
//        .addConverterFactory(
//            networkJson.asConverterFactory("application/json".toMediaType()),
//        )
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /**
     * Since we're displaying SVGs in the app, Coil needs an ImageLoader which supports this
     * format. During Coil's initialization it will call `applicationContext.newImageLoader()` to
     * obtain an ImageLoader.
     *
     * @see <a href="https://github.com/coil-kt/coil/blob/main/coil-singleton/src/main/java/coil/Coil.kt">Coil</a>
     */
    @Provides
    @Singleton
    fun imageLoader(
        // We specifically request dagger.Lazy here, so that it's not instantiated from Dagger.
        okHttpCallFactory: dagger.Lazy<Call.Factory>,
        @ApplicationContext application: Context,
    ): ImageLoader = ImageLoader.Builder(application)
        .callFactory { okHttpCallFactory.get() }
        .components { add(SvgDecoder.Factory()) }
        // Assume most content images are versioned urls
        // but some problematic images are fetching each time
        .respectCacheHeaders(false)
        .apply {
            if (BuildConfig.DEBUG) {
                logger(DebugLogger())
            }
        }
        .build()

}
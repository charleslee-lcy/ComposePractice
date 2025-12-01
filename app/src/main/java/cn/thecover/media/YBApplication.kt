package cn.thecover.media

import android.app.Application
import cn.thecover.media.core.network.BuildConfig
import cn.thecover.media.core.network.LogUtil
import coil.ImageLoader
import coil.ImageLoaderFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 *
 * <p> Created by CharlesLee on 2025/7/25
 * 15708478830@163.com
 */
@HiltAndroidApp
class YBApplication : Application(), ImageLoaderFactory {
    @Inject
    lateinit var imageLoader: dagger.Lazy<ImageLoader>

    override fun onCreate() {
        super.onCreate()

        LogUtil.init(BuildConfig.DEBUG)
    }

    override fun newImageLoader(): ImageLoader = imageLoader.get()
}
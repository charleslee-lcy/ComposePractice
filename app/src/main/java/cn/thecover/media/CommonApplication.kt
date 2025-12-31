package cn.thecover.media

import android.app.Application
import cn.thecover.media.core.common.Constants
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
class CommonApplication : Application(), ImageLoaderFactory {
    @Inject
    lateinit var imageLoader: dagger.Lazy<ImageLoader>

    override fun onCreate() {
        super.onCreate()

        // 初始化应用版本号
        Constants.APP_VERSION = BuildConfig.VERSION_NAME
        
        LogUtil.init(BuildConfig.DEBUG)
    }

    override fun newImageLoader(): ImageLoader = imageLoader.get()
}
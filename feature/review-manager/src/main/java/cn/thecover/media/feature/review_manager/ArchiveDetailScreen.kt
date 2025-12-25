package cn.thecover.media.feature.review_manager

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.navigation.NavController
import cn.thecover.media.core.widget.component.PreviewImages
import cn.thecover.media.core.widget.component.TOAST_TYPE_WARNING
import cn.thecover.media.core.widget.component.YBTitleBar
import cn.thecover.media.core.widget.event.clickableWithoutRipple
import cn.thecover.media.core.widget.event.showToast
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.core.widget.ui.PhonePreview
import com.mohamedrejeb.calf.ui.web.rememberWebViewState

@Composable
internal fun ArchiveDetailRoute(
    modifier: Modifier = Modifier,
    url: String,
    navController: NavController
) {
    ArchiveDetailScreen(modifier, url, navController)
}

/**
 *
 * <p> Created by CharlesLee on 2025/8/8
 * 15708478830@163.com
 */
@Composable
fun ArchiveDetailScreen(
    modifier: Modifier = Modifier,
    url: String,
    navController: NavController
) {

    val showImages = remember { mutableStateOf(false) }
    val imagesData = remember {
        mutableStateListOf(
            "https://gips2.baidu.com/it/u=2655957002,493323304&fm=3074&app=3074&f=PNG?w=1440&h=2560",
            "https://q0.itc.cn/q_70/images03/20241216/c64a9e4d9e8c4f33ad0a7cb6ac050120.jpeg",
            "https://gips2.baidu.com/it/u=195724436,3554684702&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960",
            "https://gips2.baidu.com/it/u=207216414,2485641185&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=720",
            "https://gips2.baidu.com/it/u=828570294,3060139577&fm=3028&app=3028&f=JPEG&fmt=auto?w=1024&h=1024",
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        WebViewContent(showImages, url, navController)
//        PreviewImages(imagesData, showImages)
    }
}

@Composable
private fun WebViewContent(
    showImages: MutableState<Boolean>,
    url: String,
    navController: NavController
) {
    val scrollState = rememberScrollState()
    val webViewState = rememberWebViewState(
        url = url
    )
    var webView: WebView? by remember { mutableStateOf(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    var webTitle by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .statusBarsPadding()
        )
        YBTitleBar(title = "稿件详情", leftOnClick = {
            navController.popBackStack()
        })
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    // 在WebView配置中添加以下设置
                    settings.apply {
                        javaScriptEnabled = true
                        useWideViewPort = true
                        loadWithOverviewMode = true
                        domStorageEnabled = true
                        builtInZoomControls = false
                        displayZoomControls = false
                        setSupportZoom(false)
                        // 添加以下配置
                        layoutAlgorithm = android.webkit.WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
                        loadWithOverviewMode = true
                        useWideViewPort = true
                        databaseEnabled = true
                        setGeolocationEnabled(true)
                        // 设置默认缩放
                        defaultZoom = android.webkit.WebSettings.ZoomDensity.FAR
                    }

                    // 设置WebView客户端
                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(
                            view: WebView?,
                            url: String?,
                            favicon: Bitmap?
                        ) {
                            super.onPageStarted(view, url, favicon)
                            isLoading = true
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            isLoading = false
                        }

                        override fun onReceivedError(
                            view: WebView?,
                            request: WebResourceRequest?,
                            error: WebResourceError?
                        ) {
                            super.onReceivedError(view, request, error)
                            isLoading = false
                        }

                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): Boolean {
                            val url = request?.url.toString()
                            if (url.startsWith("yndaily://")) {
                                try {
                                    val uri = url.toUri()
                                    val intent = Intent(Intent.ACTION_VIEW, uri)
                                    val context = view?.context

                                    context?.apply {
                                        if (intent.resolveActivity(packageManager) != null) {
                                            startActivity(intent)
                                        } else {
                                            showToast(msg = "请先安装云新闻应用", action = TOAST_TYPE_WARNING)
                                        }
                                    }
                                } catch (e: Exception) {
                                    // 处理其他异常情况
                                    e.printStackTrace()
                                }
                                view?.takeIf {
                                    it.canGoBack()
                                }?.let {
                                    it.goBack()
                                }
                                return true
                            }
                            return super.shouldOverrideUrlLoading(view, request)
                        }
                    }

                    webChromeClient = object : WebChromeClient() {
                        override fun onReceivedTitle(view: WebView?, title: String?) {
                            super.onReceivedTitle(view, title)
                            title?.let { webTitle = it }
                        }
                    }

                    loadUrl(url)
                    webView = this
                }
            },
            update = { view ->
                if (view.url != url) {
                    view.loadUrl(url)
                }
            }
        )
    }

    BackHandler(true) {
        webView?.let {
            if (it.canGoBack()) {
                it.goBack()
            } else {
                navController.popBackStack()
            }
        }
    }
}

@PhonePreview
@Composable
private fun ArchiveScoreScreenPreview() {
    YBTheme {
        ArchiveDetailScreen(
            url = "https://www.baidu.com",
            navController = NavController(LocalContext.current)
        )
    }
}
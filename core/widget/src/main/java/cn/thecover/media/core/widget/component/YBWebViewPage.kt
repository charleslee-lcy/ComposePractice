package cn.thecover.media.core.widget.component


import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import cn.thecover.media.core.widget.component.popup.YBLoadingDialog
import cn.thecover.media.core.widget.state.rememberTipsDialogState
import cn.thecover.media.core.widget.theme.MainTextColor


/**
 *  Created by Wing at 16:15 on 2025/8/18
 *
 */

/**
 * 用于显示H5页面的基础控件
 *
 * @param modifier Modifier
 * @param url 要加载的网页URL
 * @param onBackRequested 返回按键回调
 * @param onLoadingStateChanged 加载状态变化回调
 * @param onReceivedTitle 接收到网页标题回调
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun YBWebViewPage(
    modifier: Modifier = Modifier,
    url: String,
    defaultTitle: String = "",
    onBackRequested: (() -> Unit)? = null,
    onLoadingStateChanged: ((Boolean) -> Unit)? = null,
    onReceivedTitle: ((String) -> Unit)? = null
) {
    val loadingState = rememberTipsDialogState()
    var isLoading by remember { mutableStateOf(true) }
    var webView: WebView? by remember { mutableStateOf(null) }
    var webTitle by remember { mutableStateOf(defaultTitle) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .statusBarsPadding()
        )
        YBTitleBar (
            center = {
                Text(
                    text = webTitle,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 32.dp).basicMarquee(),
                    color = MainTextColor
                )
            }, leftOnClick = {
                if (isLoading) {
                    onBackRequested?.invoke()
                } else {
                    webView?.let { web ->
                        if (web.canGoBack()) {
                            web.goBack()
                        } else {
                            onBackRequested?.invoke()
                        }
                    }
                }

            }, backgroundColor = MaterialTheme.colorScheme.background
        )
        HorizontalDivider(
            modifier = Modifier
                .height(0.5.dp)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.outline.copy(0.25f)
        )
        Box(modifier = modifier.fillMaxSize()) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )

                        // WebView基础配置
                        settings.apply {
                            javaScriptEnabled = true
                            domStorageEnabled = true
                            useWideViewPort = true
                            loadWithOverviewMode = true
                            builtInZoomControls = false
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
                                loadingState.show("加载中...")
                                onLoadingStateChanged?.invoke(true)
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                isLoading = false
                                loadingState.hide()
                                onLoadingStateChanged?.invoke(false)
                            }

                            override fun onReceivedError(
                                view: WebView?,
                                request: WebResourceRequest?,
                                error: WebResourceError?
                            ) {
                                super.onReceivedError(view, request, error)
                                isLoading = false
                                loadingState.hide()
                                onLoadingStateChanged?.invoke(false)
                            }
                        }

                        webChromeClient = object : WebChromeClient() {
                            override fun onReceivedTitle(view: WebView?, title: String?) {
                                super.onReceivedTitle(view, title)
                                title?.let { webTitle = it }
                                onReceivedTitle?.invoke(title ?: "")
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

            YBLoadingDialog(
                enableDismiss = false,
                onDismissRequest = {

                },
                loadingState = loadingState
            )
        }
    }


}

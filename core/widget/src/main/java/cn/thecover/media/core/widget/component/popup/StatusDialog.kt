package cn.thecover.media.core.widget.component.popup

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import cn.thecover.media.core.widget.R
import cn.thecover.media.core.widget.state.IconDialogState
import cn.thecover.media.core.widget.state.TipsDialogState
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

/**
 *  Created by Wing at 10:54 on 2025/7/30
 *  状态弹窗如成功提示失败提示等
 */

@Composable
fun YBAutoDismissDialog(
    tipsState: IconDialogState,
    onDismissRequest: () -> Unit = {},
) {
    if (tipsState.isVisible) {
        IconStatusDialog(
            onDismissRequest = {
                tipsState.hide()
                onDismissRequest()
            },
            title = tipsState.message,
            icon = tipsState.iconResource,
            autoDismissMillis = 2000L,
        )
    }
}

/**
 * 图标状态弹窗
 *
 * @param onDismissRequest 弹窗关闭的回调
 * @param title 弹窗标题
 * @param icon 图标
 */
@Composable
fun IconStatusDialog(
    onDismissRequest: () -> Unit,
    title: String = "",
    @DrawableRes icon: Int,
    autoDismissMillis: Long = 2000L
) {
    FullScreenStatusDialogContent(
        onDismissRequest = onDismissRequest, message = title, icon = {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
        },
        autoDismissMillis = autoDismissMillis
    )
}

/**
 * 使用 LoadingState 的加载弹窗
 *
 * @param loadingState 加载状态管理器
 * @param onDismissRequest 取消弹窗的回调
 * @param enableDismiss 是否允许点击蒙层或返回键关闭弹窗，默认为 false
 */
@Composable
fun YBLoadingDialog(
    loadingState: TipsDialogState,
    onDismissRequest: () -> Unit = {},
    enableDismiss: Boolean = false
) {
    FullScreenLoading(
        isVisible = loadingState.isVisible,
        onDismissRequest = {
            loadingState.hide()
            onDismissRequest()
        },
        message = loadingState.message,
        enableDismiss = enableDismiss
    )
}

/**
 * 全屏居中显示的Loading弹窗（无蒙层效果）
 *
 * @param isVisible 是否显示loading
 * @param message 加载提示文本
 * @param enableDismiss 是否允许点击外部关闭，默认为false
 * @param onDismissRequest 关闭时的回调
 */
@Composable
fun FullScreenLoading(
    isVisible: Boolean,
    message: String = "加载中",
    enableDismiss: Boolean = false,
    onDismissRequest: () -> Unit = {}
) {
    if (isVisible) {
        FullScreenLoadingContent(
            message = message,
            enableDismiss = enableDismiss,
            onDismissRequest = onDismissRequest
        )
    }
}


/**
 * 全屏居中显示的Loading弹窗（无蒙层效果）
 *
 * @param message 加载提示文本
 * @param enableDismiss 是否允许点击外部关闭，默认为false
 * @param onDismissRequest 弹窗关闭的回调
 */
@SuppressLint("ConfigurationScreenWidthHeight", "UnusedBoxWithConstraintsScope")
@Composable
private fun FullScreenLoadingContent(
    message: String,
    enableDismiss: Boolean,
    onDismissRequest: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "rotationTransition")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3500
            }
        ), label = "rotation"
    )
    FullScreenStatusDialogContent(
        message = message,
        icon = {
            // 加载指示器
            Icon(
                painter = painterResource(id = R.drawable.ic_loading),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .rotate(rotation),
                tint = MaterialTheme.colorScheme.primary
            )
        },
        enableDismiss = enableDismiss,
        onDismissRequest = onDismissRequest,
        autoDismissMillis = 20000L
    )
}

/**
 * 仅限状态弹窗内容
 * @param message 弹窗内容
 * @param icon 弹窗图标
 * @param enableDismiss 是否允许点击外部关闭，默认为false
 * @param onDismissRequest 弹窗关闭的回调
 */
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun FullScreenStatusDialogContent(
    message: String,
    icon: @Composable (() -> Unit)? = null,
    enableDismiss: Boolean = false,
    onDismissRequest: (() -> Unit)? = null,
    autoDismissMillis: Long = 2000L
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    // 获取屏幕尺寸（像素）
    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }

    val boxSize = 110.dp
    val boxSizePx = with(density) { boxSize.toPx() }

    if (autoDismissMillis > 0) {
        LaunchedEffect(Unit) {
            delay(autoDismissMillis)
            onDismissRequest?.invoke()
        }
    }
    Popup(
        properties = PopupProperties(
            dismissOnBackPress = enableDismiss,
            dismissOnClickOutside = enableDismiss
        )
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (enableDismiss) {
                        Modifier.clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            onDismissRequest?.invoke()
                        }
                    } else {
                        Modifier
                    }
                )
        ) {
            // 计算居中位置
            val centerX = (screenWidthPx - boxSizePx) / 2
            val centerY = (screenHeightPx - boxSizePx) / 2

            Card(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = centerX.roundToInt(),
                            y = centerY.roundToInt()
                        )
                    }
                    .size(110.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                )

            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    icon?.invoke()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}



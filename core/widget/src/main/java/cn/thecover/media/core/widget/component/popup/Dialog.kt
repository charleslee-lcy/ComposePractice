package cn.thecover.media.core.widget.component.popup

import android.os.Build
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.WindowCompat
import cn.thecover.media.core.widget.component.YBButton
import cn.thecover.media.core.widget.icon.YBIcons
import cn.thecover.media.core.widget.theme.TertiaryTextColor
import cn.thecover.media.core.widget.theme.YBShapes
import cn.thecover.media.core.widget.ui.PhonePreview


/**
 *  Created by Wing at 10:02 on 2025/7/31
 *  普通弹窗封装
 */


/**
 * 弹窗封装
 * @param dialogState 弹窗状态
 * @param onDismissRequest 弹窗关闭回调
 * @param title 弹窗标题
 * @param confirmText 确认按钮文案(不需要置为null)
 * @param cancelText 取消按钮文案
 * @param onConfirm 确认按钮点击回调
 * @param onCancel 取消按钮点击回调
 * @param content 弹窗内容
 *
 */
@Composable
fun YBDialog(
    dialogState: MutableState<Boolean>,
    onDismissRequest: () -> Unit,
    title: String? = null,
    confirmText: String? = "确认",
    cancelText: String? = "取消",
    onConfirm: (() -> Unit)? = null,
    onCancel: (() -> Unit)? = null,
    widthRate: Float = 0.8f,
    content: @Composable () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    // 获取屏幕尺寸
    val screenWidth = with(density) { configuration.screenWidthDp.dp }

    if(dialogState.value){
        Dialog(
            onDismissRequest = onDismissRequest,
            content = {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                    ),
                    shape = YBShapes.medium,
                    modifier = Modifier
                        .width(screenWidth * widthRate)
                        .padding()

                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Spacer(modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.weight(1f))
                            if (title != null) {
                                Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(imageVector = YBIcons.Close, "关闭弹窗", modifier = Modifier.size(24.dp).clickable{
                               dialogState.value=false
                            })
                        }
                        Spacer(modifier = Modifier.size(20.dp))
                        content()
                        Spacer(modifier = Modifier.size(20.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            if(cancelText!=null){
                                YBButton(
                                    onClick = {
                                        onCancel?.invoke()
                                        dialogState.value = false
                                    },
                                    modifier = Modifier.weight(1f),
                                    textColor = TertiaryTextColor,
                                    backgroundColor = MaterialTheme.colorScheme.surface,
                                    shape = MaterialTheme.shapes.extraSmall,
                                    borderColor = TertiaryTextColor,
                                ) {
                                    Text(text = cancelText)
                                }
                            }
                            if(confirmText!=null){
                                YBButton(
                                    onClick = {
                                        onConfirm?.invoke()
                                        dialogState.value = false
                                    },
                                    shape = MaterialTheme.shapes.extraSmall,
                                    modifier = Modifier.weight(1f),
                                ) {
                                    Text(text = confirmText)
                                }
                            }


                        }
                    }
                }
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun YBFullDialog(
    dialogState: MutableState<Boolean>,
    onDismissRequest: () -> Unit,
    backgroundColor: Color = Color.White,
    content: @Composable () -> Unit,
) {
    if(dialogState.value){
        // 取物理屏幕尺寸
        Dialog(
            onDismissRequest = onDismissRequest,
            content = {
                val view = LocalView.current
                val window = (view.parent as DialogWindowProvider).window
                SideEffect {
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                    window.setBackgroundDrawable(android.graphics.Color.TRANSPARENT.toDrawable())

                    // 允许画到状态栏/导航栏区域
                    window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

                    val controller = WindowCompat.getInsetsController(window, view)
                    controller.isAppearanceLightStatusBars = false
                }

                Box(
                    modifier = Modifier.fillMaxSize().background(backgroundColor)
                ) {
                    content()
                }
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false, // 关键：去掉默认宽高限制
                decorFitsSystemWindows = false, // 允许内容延伸到状态栏/导航栏
                dismissOnBackPress = true,
                dismissOnClickOutside = false
            )
        )
    }
}

@Composable
fun YBAlertDialog(
    dialogState: MutableState<Boolean>,
    onDismissRequest: () -> Unit,
    title: String? = null,
    content: @Composable () -> Unit,
    confirmButtonText: String = "确认",
    onConfirm: () -> Unit,
    dismissButtonText: String? = "取消",
    onDismiss: (() -> Unit)? = null,
    isConfirmDestructive: Boolean = false,
    enableDismiss: Boolean = true,
) {
    YBDialog(dialogState, onDismissRequest, title, confirmButtonText, dismissButtonText, onConfirm, onDismiss){
        Column {
            content()
        }
    }
}

@PhonePreview
@Composable
fun YBAlertDialogPreview() {
    YBDialog(
        dialogState = remember { mutableStateOf(true) },
        onDismissRequest = {},
        title = "提示",
        confirmText = "确认",
        cancelText = "取消"
    ) {

    }
}


 
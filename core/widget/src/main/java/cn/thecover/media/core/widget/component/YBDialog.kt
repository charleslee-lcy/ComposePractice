package cn.thecover.media.core.widget.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy


/**
 *  Created by Wing at 09:31 on 2025/7/30
 *  自定义弹窗对话框封装
 */


/**
 * 自定义弹窗对话框封装（不带蒙层版）
 * 严格意义上讲，这不是一根标准的基于dialog的弹窗，旨在屏蔽某些场景下不需要弹窗的背景蒙层，由于官方并未提供弹窗蒙层显示设置，
 * 故单独使用box替代背景，card作为弹窗主体。
 *
 * @param onDismissRequest 取消弹窗的回调
 * @param title 弹窗标题
 * @param content 弹窗内容
 * @param confirmButtonText 确认按钮文本
 * @param onConfirm 确认按钮点击回调
 * @param dismissButtonText 取消按钮文本（可为空，为空时不显示取消按钮）
 * @param onDismiss 取消按钮点击回调
 * @param isConfirmDestructive 确认按钮是否为危险操作样式（红色）
 * @param showScrim 是否显示背景蒙层，默认为 false
 */
@Composable
fun YBDialogNoScrim(
    onDismissRequest: () -> Unit,
    title: String? = null,
    content: @Composable () -> Unit,
    confirmButtonText: String = "确认",
    onConfirm: () -> Unit,
    dismissButtonText: String? = "取消",
    onDismiss: (() -> Unit)? = null,
    isConfirmDestructive: Boolean = false,
    showScrim: Boolean = true
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() } // 去除水波纹
            ) {
                onDismissRequest()
            },
        contentAlignment = Alignment.Center
    ) {
        if (showScrim) {
            // 如果需要显示背景蒙层，则添加半透明遮罩
            Spacer(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.32f))
            )
        }
        // 弹窗内容
        YBDialogContent(
            onDismissRequest = onDismissRequest,
            title = title,
            content = content,
            confirmButtonText = confirmButtonText,
            onConfirm = onConfirm,
            dismissButtonText = dismissButtonText,
            onDismiss = onDismiss,
            isConfirmDestructive = isConfirmDestructive
        )

    }
}

/**
 * 自定义带蒙层的弹窗
 *
 * @param onDismissRequest 取消弹窗的回调
 * @param title 弹窗标题
 * @param content 弹窗内容
 * @param confirmButtonText 确认按钮文本
 * @param onConfirm 确认按钮点击回调
 * @param dismissButtonText 取消按钮文本（可为空，为空时不显示取消按钮）
 * @param onDismiss 取消按钮点击回调
 * @param isConfirmDestructive 确认按钮是否为危险操作样式（红色）
 * @param enableDismiss 是否允许点击蒙层或返回键关闭弹窗，默认为 true
 */
@Composable
fun YBDialog(
    onDismissRequest: () -> Unit,
    title: String? = null,
    content: @Composable () -> Unit,
    confirmButtonText: String = "确认",
    onConfirm: () -> Unit,
    dismissButtonText: String? = "取消",
    onDismiss: (() -> Unit)? = null,
    isConfirmDestructive: Boolean = false,
    enableDismiss: Boolean = true
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = enableDismiss,
            dismissOnClickOutside = enableDismiss,
            securePolicy = SecureFlagPolicy.Inherit
        )
    ) {
        // 弹窗内容
        YBDialogContent(
            onDismissRequest = onDismissRequest,
            title = title,
            content = content,
            confirmButtonText = confirmButtonText,
            onConfirm = onConfirm,
            dismissButtonText = dismissButtonText,
            onDismiss = onDismiss,
            isConfirmDestructive = isConfirmDestructive
        )
    }
}

@Composable
private fun YBDialogContent(
    onDismissRequest: () -> Unit,
    title: String? = null,
    content: @Composable () -> Unit,
    confirmButtonText: String = "确认",
    onConfirm: () -> Unit,
    dismissButtonText: String? = "取消",
    onDismiss: (() -> Unit)? = null,
    isConfirmDestructive: Boolean = false,
) {
    Card(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(0.8f)
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .wrapContentHeight()
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            // 标题
            title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // 内容
            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                ProvideTextStyle(
                    value = MaterialTheme.typography.bodyMedium
                ) {
                    content()
                }
            }

            // 按钮区域
            Row(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .align(Alignment.End),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 取消按钮
                dismissButtonText?.let {
                    TextButton(
                        onClick = {
                            onDismiss?.invoke()
                            onDismissRequest()
                        },
                        interactionSource = remember { MutableInteractionSource() } // 去除水波纹
                    ) {
                        Text(it)
                    }
                }

                // 确认按钮
                Button(
                    onClick = {
                        onConfirm()
                        onDismissRequest()
                    },
                    colors = if (isConfirmDestructive) {
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        )
                    } else {
                        ButtonDefaults.buttonColors()
                    },
                    interactionSource = remember { MutableInteractionSource() } // 去除水波纹
                ) {
                    Text(confirmButtonText)
                }
            }
        }
    }
}


/**
 * 简单的确认弹窗
 */
@Composable
fun ConfirmDialog(
    openDialog: Boolean,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    if (openDialog) {
        YBDialogNoScrim(
            onDismissRequest = onDismissRequest,
            title = "确认操作",
            content = {
                Text("您确定要执行此操作吗？")
            },
            onConfirm = onConfirm,
            isConfirmDestructive = true
        )
    }
}

/**
 * 带输入框的弹窗
 */
@Composable
fun InputDialog(
    openDialog: Boolean,
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit,
    initialText: String = ""
) {
    var inputText by remember { mutableStateOf(initialText) }

    if (openDialog) {
        YBDialogNoScrim(
            onDismissRequest = {
                inputText = ""
                onDismissRequest()
            },
            title = "请输入",
            content = {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    label = { Text("输入内容") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButtonText = "保存",
            onConfirm = { onConfirm(inputText) },
            dismissButtonText = "取消"
        )
    }
}

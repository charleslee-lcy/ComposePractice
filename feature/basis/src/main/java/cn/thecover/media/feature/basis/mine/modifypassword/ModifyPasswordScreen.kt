package cn.thecover.media.feature.basis.mine.modifypassword

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import cn.thecover.media.core.widget.component.YBButton
import cn.thecover.media.core.widget.component.YBTitleBar
import cn.thecover.media.core.widget.component.YBToast
import cn.thecover.media.core.widget.icon.YBIcons
import cn.thecover.media.core.widget.theme.SecondaryTextColor
import cn.thecover.media.core.widget.theme.TertiaryTextColor
import cn.thecover.media.feature.basis.home.navigation.LoginRoute
import cn.thecover.media.feature.basis.mine.MineIntent
import cn.thecover.media.feature.basis.mine.MineViewModel

/**
 *  Created by Wing at 11:21 on 2025/7/29
 *  修改密码页面
 */

@Composable
internal fun ModifyPasswordRoute(
    modifier: Modifier = Modifier,
    viewModel: MineViewModel = hiltViewModel(),
    navController: NavController,
    isFromLogin: Boolean = false,
    username: String = "",
    tempPassword: String = ""
) {
    val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val oneTimeUiState by viewModel.oneTimeUiState.collectAsStateWithLifecycle()

    // 监听状态变化
    LaunchedEffect(oneTimeUiState) {
        val toastMessage = oneTimeUiState.toastMessage
        val shouldNavigateToLogin = oneTimeUiState.shouldNavigateToLogin

        if (toastMessage?.isNotEmpty() == true) {
            snackBarHostState.showSnackbar(
                message = toastMessage,
            )
            if (shouldNavigateToLogin) {
                // 导航到登录页面，并清除所有返回栈
                navController.navigate(LoginRoute) {
                    popUpTo(0) { inclusive = true }
                }
                // 如果是从登录页面跳转过来的，需要清除密码字段，但保留用户名
                if (isFromLogin) {
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        "clearPassword",
                        true
                    )
                    navController.currentBackStackEntry?.savedStateHandle?.set("username", username)
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ModifyPasswordScreen(
            modifier,
            navController,
            snackBarHostState,
            isFromLogin,
            username,
            tempPassword
        ) { old, new, confirm ->
            if (isFromLogin) {
                // 未登录状态的修改密码
                viewModel.handleIntent(
                    MineIntent.ModifyPasswordWithoutLogin(
                        username,
                        old,
                        new,
                        confirm
                    )
                )
            } else {
                // 已登录状态的修改密码
                viewModel.handleIntent(MineIntent.ModifyPassword(old, new, confirm))
            }
        }

        YBToast(snackBarHostState = snackBarHostState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyPasswordScreen(
    modifier: Modifier,
    navController: NavController,
    snackBarHostState: SnackbarHostState,
    isFromLogin: Boolean,
    username: String,
    tempPassword: String,
    commit: (String, String, String) -> Unit
) {
    val oldPass = remember { mutableStateOf(if (isFromLogin) tempPassword else "") }
    val newPass = remember { mutableStateOf("") }
    val confirmPass = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
        )
        YBTitleBar(
            title = "修改密码", leftOnClick = {
                if (isFromLogin) {
                    // 如果是从登录页面跳转过来的，需要保留用户名
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        "clearPassword",
                        true
                    )
                    navController.currentBackStackEntry?.savedStateHandle?.set("username", username)
                    // 直接导航到登录页面
                    navController.navigate(LoginRoute) {
                        popUpTo(0) { inclusive = true }
                    }
                } else {
                    // 如果是从已登录状态进入的，正常返回
                    navController.popBackStack()
                }
            }, backgroundColor = MaterialTheme.colorScheme.background
        )
        HorizontalDivider(
            modifier = Modifier
                .height(0.5.dp)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.outline.copy(0.25f)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {

            ModifyPasswordInput(oldPass, "原密码", "请输入原密码")
            ModifyPasswordInput(newPass, "新密码", "请输入新密码")
            ModifyPasswordInput(confirmPass, "确认密码", "请再次输入新密码")
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "*密码长度不小于 8 位，必须包含数字、字母、特殊符号\n*修改后，电脑端也需要用新密码登录",
                color = TertiaryTextColor,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(40.dp))
            YBButton(
                modifier = Modifier.fillMaxWidth(),
                text = { Text("提交") },
                onClick = {
                    // 收起软键盘
                    keyboardController?.hide()
                    commit(oldPass.value, newPass.value, confirmPass.value)
                },
                borderColor = Color.Transparent,
                enabled = oldPass.value.length >= 8 && newPass.value.length >= 8 && confirmPass.value.length >= 8 &&
                        oldPass.value.length <= 20 && newPass.value.length <= 20 && confirmPass.value.length <= 20,
                shape = RoundedCornerShape(2.dp)
            )
        }
    }

}

@Composable
fun ModifyPasswordInput(
    textState: MutableState<String>,
    label: String,
    hint: String?,
    isPassword: Boolean = true
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        BasicTextField(
            value = textState.value,
            onValueChange = { textState.value = it },
            modifier = Modifier.weight(1f),
            visualTransformation = if (isPassword && !passwordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .padding(vertical = 12.dp)
                                .width(80.dp)
                        ) {
                            Text(label, fontSize = 15.sp, color = SecondaryTextColor)
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            if (textState.value.isEmpty()) {
                                Text(
                                    hint ?: "",
                                    fontSize = 15.sp,
                                    color = TertiaryTextColor,
                                    style = LocalTextStyle.current
                                )
                            }
                            innerTextField()
                        }
                    }
                }
            }
        )

        if (isPassword) {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    painterResource(if (passwordVisible) YBIcons.Custom.PasswordIsShow else YBIcons.Custom.PasswordIsHide),
                    tint = TertiaryTextColor,
                    contentDescription = if (passwordVisible) "隐藏密码" else "查看密码"
                )
            }
        }
    }
    HorizontalDivider(
        modifier = Modifier
            .height(0.5.dp)
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.outline.copy(0.25f)
    )
}


 
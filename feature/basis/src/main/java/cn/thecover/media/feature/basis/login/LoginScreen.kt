package cn.thecover.media.feature.basis.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.navOptions
import cn.thecover.media.core.common.util.DESUtil.simpleEncrypt
import cn.thecover.media.core.network.BaseUiState
import cn.thecover.media.core.network.HttpStatus
import cn.thecover.media.core.network.previewRetrofit
import cn.thecover.media.core.widget.component.CommonInput
import cn.thecover.media.core.widget.component.TOAST_TYPE_ERROR
import cn.thecover.media.core.widget.component.TOAST_TYPE_WARNING
import cn.thecover.media.core.widget.component.CommonButton
import cn.thecover.media.core.widget.component.CommonImage
import cn.thecover.media.core.widget.component.popup.CommonLoadingDialog
import cn.thecover.media.core.widget.datastore.Keys
import cn.thecover.media.core.widget.datastore.saveData
import cn.thecover.media.core.widget.event.showToast
import cn.thecover.media.core.widget.state.rememberTipsDialogState
import cn.thecover.media.core.widget.theme.EditHintTextColor
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.OutlineColor
import cn.thecover.media.core.widget.theme.PageBackgroundColor
import cn.thecover.media.core.widget.theme.CommonTheme
import cn.thecover.media.core.widget.ui.ComponentVisibility
import cn.thecover.media.core.widget.ui.Visibility
import cn.thecover.media.feature.basis.R
import cn.thecover.media.feature.basis.home.HomeViewModel
import cn.thecover.media.feature.basis.home.navigation.navigateToHome
import cn.thecover.media.feature.basis.mine.navigation.navigateToModifyPasswordWithoutLogin


/**
 *
 * <p> Created by CharlesLee on 2025/7/28
 * 15708478830@163.com
 */
@Composable
fun LoginRoute(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    CommonTheme {
        LoginScreen(modifier, navController)
    }
}


@Composable
internal fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var nameText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }
    val loadingState = rememberTipsDialogState()
    var isLoginEnable by remember { mutableStateOf(false) }

    val loginState = viewModel.loginUiState.collectAsStateWithLifecycle().value

    // 监听是否需要清除密码字段和恢复用户名
    val navControllerEntry = navController.currentBackStackEntry
    LaunchedEffect(navControllerEntry) {
        // 检查是否是从修改密码页面返回的，如果是，则只清除密码字段，恢复用户名
        navControllerEntry?.savedStateHandle?.get<Boolean>("clearPassword")?.let { shouldClear ->
            if (shouldClear) {
                passwordText = ""
                // 恢复用户名
                navControllerEntry?.savedStateHandle?.get<String>("username")
                    ?.let { savedUsername ->
                        nameText = savedUsername
                        // 从ViewModel中保存用户名
                        viewModel.setTempUsername(savedUsername)
                    }
                navControllerEntry.savedStateHandle.remove<Boolean>("clearPassword")
                navControllerEntry.savedStateHandle.remove<String>("username")
            }
        }
    }

    // 从ViewModel恢复用户名
    LaunchedEffect(Unit) {
        viewModel.getTempUsername()?.let { savedUsername ->
            if (savedUsername.isNotEmpty()) {
                nameText = savedUsername
                // 清除ViewModel中的临时用户名
                viewModel.clearTempUsername()
            }
        }
    }

    LaunchedEffect(loginState) {
        when (loginState.status) {
            HttpStatus.LOADING -> {
                loadingState.show()
            }
            HttpStatus.SUCCESS -> {
                loadingState.hide()
                loginState.data?.token?.let {
                    saveData(context, Keys.USER_TOKEN, it)
                    // 重置HomeViewModel状态，确保用户切换后能重新获取数据
                    viewModel.resetState()
                    navController.navigateToHome(
                        navOptions = navOptions {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    )
                } ?: kotlin.run {
                    showToast(loginState.errorMsg.ifEmpty { "登录失败" }, TOAST_TYPE_ERROR)
                }

                viewModel.loginUiState.value = BaseUiState()
            }
            HttpStatus.ERROR -> {
                loadingState.hide()
                showToast(loginState.errorMsg.ifEmpty { "登录失败" }, TOAST_TYPE_ERROR)

                // 接口提示用户需要修改密码（包括初登验证、长时间未修改密码检测、后台操作重置了密码等等），需跳转到修改密码页面
                if (loginState.errorCode == 414) {
                    // 导航到修改密码页面，传递用户名和临时密码
                    navController.navigateToModifyPasswordWithoutLogin(
                        username = nameText,
                        tempPassword = "",
                        navOptions = navOptions {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                        }
                    )
                }

                viewModel.loginUiState.value = BaseUiState()
            }
            else -> {}
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PageBackgroundColor)
    ) {
        CommonImage(
            modifier = Modifier.fillMaxSize(),
            placeholder = painterResource(R.drawable.img_login_page_bg)
        )
        Column(
            modifier = modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 60.dp)
        ) {
            Spacer(Modifier.size(80.dp))
            CommonImage(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                placeholder = painterResource(R.mipmap.img_login_logo)
            )
            Spacer(Modifier.size(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                ComponentVisibility(visibility = Visibility.Gone) {
                    Text(
                        text = "用户名：",
                        modifier = Modifier.padding(start = 20.dp),
                    )
                }
                CommonInput(
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(
                        fontSize = 15.sp, color = MainTextColor
                    ),
                    text = nameText,
                    hint = "请输入您的用户名",
                    hintTextStyle = TextStyle(
                        fontSize = 15.sp,
                        color = EditHintTextColor
                    ),
                    onValueChange = {
                        nameText = it
                        isLoginEnable = nameText.isNotEmpty() && passwordText.isNotEmpty()
                    })
            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = OutlineColor, thickness = 0.8.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                ComponentVisibility(visibility = Visibility.Gone) {
                    Text(
                        text = "密    码：",
                        modifier = Modifier.padding(start = 20.dp)
                    )
                }
                CommonInput(
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(
                        fontSize = 15.sp, color = MainTextColor
                    ),
                    text = passwordText,
                    hint = "请输入密码",
                    hintTextStyle = TextStyle(
                        fontSize = 15.sp,
                        color = EditHintTextColor
                    ),
                    isPassword = true,
                    showVisibleIcon = true,
                    onValueChange = {
                        passwordText = it
                        isLoginEnable = nameText.isNotEmpty() && passwordText.isNotEmpty()
                    })
            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = OutlineColor, thickness = 0.8.dp)
            CommonButton(
                text = { Text("登录", fontSize = 16.sp, fontWeight = FontWeight.SemiBold) },
                modifier = modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .padding(top = 40.dp, bottom = 10.dp)
                    .height(44.dp)
                    .alpha(if (isLoginEnable) 1f else 0.5f),
                shape = RoundedCornerShape(2.dp),
                onClick = {
                    if (!isLoginEnable) return@CommonButton

                    if (nameText.isEmpty()) {
                        showToast("用户名不能为空", TOAST_TYPE_WARNING)
                        return@CommonButton
                    }
                    if (passwordText.isEmpty()) {
                        showToast("密码不能为空", TOAST_TYPE_WARNING)
                        return@CommonButton
                    }
                    focusManager.clearFocus()

                    viewModel.login(simpleEncrypt(nameText), simpleEncrypt(passwordText))
                }
            )

//            Text(
//                text = "找回密码",
//                fontSize = 15.sp,
//                color = MainColor,
//                modifier = Modifier
//                    .align(Alignment.CenterHorizontally)
//                    .clickableWithoutRipple {
//                        navController.navigateToModifyPassword()
//                    }
//                    .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)
//            )
        }
    }

    CommonLoadingDialog(loadingState, enableDismiss = true, onDismissRequest = { loadingState.hide() })
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    CommonTheme {
        LoginScreen(
            navController = NavController(LocalContext.current),
            viewModel = HomeViewModel(
                SavedStateHandle(),
                retrofit = { previewRetrofit }
            ))
    }
}
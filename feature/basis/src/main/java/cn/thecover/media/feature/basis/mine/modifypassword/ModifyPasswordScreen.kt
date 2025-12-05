package cn.thecover.media.feature.basis.mine.modifypassword

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cn.thecover.media.core.widget.component.YBButton
import cn.thecover.media.core.widget.component.YBTitleBar
import cn.thecover.media.core.widget.component.YBTopAppBar
import cn.thecover.media.core.widget.icon.YBIcons
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.SecondaryTextColor
import cn.thecover.media.core.widget.theme.TertiaryTextColor
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
    navController: NavController
) {
    val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(viewModel.oneTimeUiState) {
        if(viewModel.oneTimeUiState.value.successMessage == "修改密码成功") {
            snackBarHostState.showSnackbar("修改密码成功")
            navController.popBackStack()
        }else if(viewModel.oneTimeUiState.value.toastMessage?.isNotEmpty() == true){
            snackBarHostState.showSnackbar(viewModel.oneTimeUiState.value.toastMessage?:"")
        }
    }

    ModifyPasswordScreen(modifier, navController){ old, new, confirm ->
        viewModel.handleIntent(MineIntent.ModifyPassword(old, new, confirm))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyPasswordScreen(modifier: Modifier, navController: NavController,commit: (String, String, String) -> Unit){
    val oldPass = remember { mutableStateOf("") }
    val newPass = remember { mutableStateOf("") }
    val confirmPass = remember { mutableStateOf("") }
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
            title = "修改密码", leftOnClick = {
                navController.popBackStack()
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
                text = "*密码不小于8位，必须包含数字、字母、特殊符号\n*修改后，电脑端也需要用新密码登录",
                color = TertiaryTextColor,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(40.dp))
            YBButton(
                modifier = Modifier.fillMaxWidth(),
                text = { Text("提交") },
                onClick = {
                    commit(oldPass.value, newPass.value, confirmPass.value)
                },
                shape = RoundedCornerShape(2.dp)
            )
        }
    }

}

@Composable
fun ModifyPasswordInput(textState: MutableState<String>, label: String, hint: String?) {
    var passwordVisible by remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        BasicTextField(
            value = textState.value,
            onValueChange = { textState.value = it },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
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
                        AnimatedVisibility(textState.value.isNotEmpty()) {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    painterResource(if (passwordVisible) YBIcons.Custom.PasswordHide else YBIcons.Custom.PasswordWatch),
                                    tint = TertiaryTextColor,
                                    contentDescription = if (passwordVisible) "隐藏密码" else "查看密码"
                                )
                            }
                        }
                    }
                }
            }
        )
    }
    HorizontalDivider(
        modifier = Modifier
            .height(0.5.dp)
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.outline.copy(0.25f)
    )
}


 
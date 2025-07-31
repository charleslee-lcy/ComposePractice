package cn.thecover.media.feature.basis.mine

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import cn.thecover.media.core.widget.component.YBAutoDismissDialog
import cn.thecover.media.core.widget.component.YBDialog
import cn.thecover.media.core.widget.component.YBLoadingDialog
import cn.thecover.media.core.widget.state.rememberIconTipsDialogState
import cn.thecover.media.core.widget.state.rememberTipsDialogState
import androidx.navigation.navOptions
import cn.thecover.media.core.widget.component.YBButton
import cn.thecover.media.core.widget.component.picker.YBDatePicker
import cn.thecover.media.core.widget.component.picker.YBTimePicker
import cn.thecover.media.core.widget.component.popup.YBPopup
import cn.thecover.media.feature.basis.R
import cn.thecover.media.feature.basis.mine.MineViewModel.Companion.CACHE_CLEAR_STATE_FAILED
import cn.thecover.media.feature.basis.mine.MineViewModel.Companion.CACHE_CLEAR_STATE_FINISHED
import cn.thecover.media.feature.basis.mine.MineViewModel.Companion.CACHE_CLEAR_STATE_STARTED
import cn.thecover.media.feature.basis.home.navigation.LoginRoute
import cn.thecover.media.feature.basis.home.navigation.navigateToLogin
import cn.thecover.media.feature.basis.mine.intent.MineNavigationIntent
import cn.thecover.media.feature.basis.mine.navigation.navigateToModifyPassword
import coil.compose.AsyncImage
import com.google.samples.apps.nowinandroid.core.designsystem.theme.YBTheme


/**
 *
 * <p> Created by CharlesLee on 2025/7/28
 * 15708478830@163.com
 */
@Composable
internal fun MineRoute(
    modifier: Modifier = Modifier,
    viewModel: MineViewModel = hiltViewModel(),
    navController: NavController,
) {
    MineScreen(modifier, navController = navController)
}

@Composable
internal fun MineScreen(
    modifier: Modifier = Modifier,
    viewModel: MineViewModel = hiltViewModel(),
    navController: NavController,
) {
    Box(
        contentAlignment = Alignment.Center, modifier = modifier.fillMaxSize()
    ) {
        var showLogoutDialog by remember { mutableStateOf(false) }
        val userAvatarState by viewModel.userAvatarState.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 24.dp, end = 24.dp, top = 56.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UserAvatar(userAvatarState.avatarUrl, userAvatarState.username)
            Spacer(modifier = Modifier.height(56.dp))
            MineFunctionList(navController,viewModel)
            YBButton(
                onClick = {
                    showLogoutDialog = true

                },
                modifier = Modifier
                    .padding(top = 24.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
            ) {
                Text("退出登录")
            }
        }
        if (showLogoutDialog) {
            YBDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = "退出登录",
                content = {
                    Text("您确定要退出登录吗？")
                },
                confirmButtonText = "退出",
                onConfirm = {
                    navController.navigateToLogin(navOptions {
                        // 清除所有之前的页面
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    })
                },
                isConfirmDestructive = true
            )
        }

    }
}

@Preview(showSystemUi = true)
@Composable
private fun MineScreenPreview() {
    YBTheme {
        // 手动创建ViewModel实例，用于预览
        val previewViewModel = MineViewModel(SavedStateHandle())

        MineScreen(
            viewModel = previewViewModel, navController = NavController(LocalContext.current)
        )
    }
}


enum class MineFunctionType(
    val title: String, var desc: String, val navigateAction: (MineNavigationIntent)? = null
) {
    Version("版本", "v1.0.0"), Cache("缓存", "上次清理 "), ModifyPassword(
        "修改密码",
        ""
    ),
    HelpCenter("帮助中心", "")
}

/**
 * 用户头像
 */
@Composable
private fun UserAvatar(avatarUrl: String?, userName: String?) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        AsyncImage(
            model = avatarUrl,
            contentDescription = "用户头像",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color.LightGray),

            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.ic_mine_avatar),

            // 加载失败/异常占位图
            error = painterResource(R.drawable.ic_mine_avatar),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(userName ?: "", modifier = Modifier.weight(1f), fontSize = 20.sp)
    }
}


/**
 * 我的功能列表
 */
@Composable
private fun MineFunctionList(
    navController: NavController,
    viewModel: MineViewModel = hiltViewModel()
) {
    val loadingState = rememberTipsDialogState()

    val statusState = rememberIconTipsDialogState()
    val showClearCacheState = viewModel.cacheClearState.collectAsState()
    val dialogState=remember { mutableStateOf(false) }
    var showpop by remember { mutableStateOf(false) }
    var timePickerShow by remember { mutableStateOf(false) }
    if (showClearCacheState.value == CACHE_CLEAR_STATE_STARTED) {
        loadingState.show("清理中")
    } else {
        loadingState.hide()
        if (showClearCacheState.value == CACHE_CLEAR_STATE_FINISHED) {
            statusState.show("清理完成", cn.thecover.media.core.widget.R.drawable.icon_checked)
        } else if (showClearCacheState.value == CACHE_CLEAR_STATE_FAILED) {
            statusState.show("清理失败")
        }
    }
    val datePickerState = remember { mutableStateOf(false) }
    LazyColumn(modifier = Modifier.padding(start = 16.dp)) {
        items(MineFunctionType.entries) { func ->
            MineFunctionItem(
                func.title, func.desc, clickAction =
                    when (func) {
                        MineFunctionType.ModifyPassword -> {
                            {
                                navController.navigateToModifyPassword()
                            }
                        }

                        MineFunctionType.Cache -> {
                            {
                                viewModel.handleIntent(MineIntent.ClearCache)
                            }
                        }

                        MineFunctionType.HelpCenter -> {
                            {
                                timePickerShow=true
                                //todo 跳转至帮助中心
                            }
                        }

                        else -> null

                    })
        }
    }


    YBLoadingDialog(loadingState, enableDismiss = true, onDismissRequest = { loadingState.hide() })

    YBAutoDismissDialog(statusState)



    YBTimePicker(timePickerShow, onCancel = { timePickerShow = false }, onChange = {})
    YBDatePicker(datePickerState.value, onCancel = { datePickerState.value = false }, onChange = {})
    YBDialog(dialogState=dialogState, onDismissRequest = { dialogState.value=false },title = "帮助中心", cancelText = "取消", confirmText = "确定") {
        Box(modifier = Modifier.wrapContentSize().background(color = Color.Blue)){
            Text("bangzhuzhongxin")
        }
    }
    YBPopup(showpop, title = "提示", content = {}, draggable = true,onClose = { showpop = false })
}


/**
 * 我的功能项
 */

@Composable
private fun MineFunctionItem(title: String, desc: String, clickAction: (() -> Unit)? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = clickAction != null) { clickAction?.invoke() }
            .padding(vertical = 16.dp),

        verticalAlignment = Alignment.CenterVertically) {
        Text(title, modifier = Modifier.weight(1f), fontSize = 16.sp)

        Text(desc)

        if (clickAction != null) {
            Text(">", fontSize = 16.sp)
        }
    }

}
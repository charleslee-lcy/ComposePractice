package cn.thecover.media.feature.basis.mine

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.navigation.navOptions
import cn.thecover.media.core.widget.component.popup.YBAutoDismissDialog
import cn.thecover.media.core.widget.component.popup.YBDialog
import cn.thecover.media.core.widget.component.popup.YBAlertDialog
import cn.thecover.media.core.widget.component.popup.YBLoadingDialog
import cn.thecover.media.core.widget.component.picker.YBDatePicker
import cn.thecover.media.core.widget.component.picker.YBTimePicker
import cn.thecover.media.core.widget.component.popup.YBPopup
import cn.thecover.media.core.widget.datastore.Keys
import cn.thecover.media.core.widget.datastore.clearData
import cn.thecover.media.core.widget.icon.YBIcons
import cn.thecover.media.core.widget.state.rememberIconTipsDialogState
import cn.thecover.media.core.widget.state.rememberTipsDialogState
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.TertiaryTextColor
import cn.thecover.media.feature.basis.home.navigation.navigateToLogin
import cn.thecover.media.feature.basis.mine.MineViewModel.Companion.CACHE_CLEAR_STATE_FAILED
import cn.thecover.media.feature.basis.mine.MineViewModel.Companion.CACHE_CLEAR_STATE_FINISHED
import cn.thecover.media.feature.basis.mine.MineViewModel.Companion.CACHE_CLEAR_STATE_STARTED
import cn.thecover.media.feature.basis.mine.intent.MineNavigationIntent
import cn.thecover.media.feature.basis.mine.navigation.navigateToModifyPassword
import coil.compose.AsyncImage
import cn.thecover.media.core.widget.theme.YBTheme
import kotlinx.coroutines.launch


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
        modifier = modifier.fillMaxSize()
    ) {
        val showLogoutDialog = remember { mutableStateOf(false) }
        val userAvatarState by viewModel.userAvatarState.collectAsState()
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        AsyncImage(
            model = YBIcons.Background.Mine,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()

        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 24.dp, end = 24.dp, top = 76.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UserAvatar(userAvatarState.avatarUrl, userAvatarState.username)
            Spacer(modifier = Modifier.height(40.dp))
            MineFunctionList(navController, viewModel)
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 16.dp)
            ) {
                MineFunctionItem(
                    YBIcons.Custom.MineLogout,
                    "退出登录",
                    "",
                    clickAction = { showLogoutDialog.value = true },
                    showRightArrow = false
                )
            }

        }

        YBDialog(
            dialogState = showLogoutDialog,
            onDismissRequest = { showLogoutDialog.value = false },
            title = "退出登录",
            content = {
                Text("您确定要退出登录吗？")
            },
            confirmText = "退出",
            onConfirm = {
                scope.launch {
                    clearData(context, Keys.USER_INFO)
                    navController.navigateToLogin(navOptions {
                        // 清除所有之前的页面
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    })
                }
            },
            cancelText = "取消",
            onCancel = {
                showLogoutDialog.value = false
            }
        )


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
    @DrawableRes val icon: Int,
    val title: String,
    var desc: String,
    val navigateAction: (MineNavigationIntent)? = null
) {
    Version(
        YBIcons.Custom.MineVersion,
        "版本",
        "v1.0.0"
    ),
    Cache(icon = YBIcons.Custom.MineClearCache, "缓存", "上次清理 "), ModifyPassword(
        icon = YBIcons.Custom.MineModifyPassword, "修改密码",
        ""
    ),
    HelpCenter(icon = YBIcons.Custom.MineHelpCenter, "帮助中心", "")
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
                .size(60.dp)
                .clip(CircleShape)
                .background(Color.LightGray),

            contentScale = ContentScale.Crop,
            placeholder = painterResource(YBIcons.Custom.DefaultAvatar),

            // 加载失败/异常占位图
            error = painterResource(YBIcons.Custom.DefaultAvatar),
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
    val dialogState = remember { mutableStateOf(false) }
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
    LazyColumn(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp)
            )
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        items(MineFunctionType.entries) { func ->
            MineFunctionItem(
                icon = func.icon,
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
                                timePickerShow = true
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
    YBDialog(
        dialogState = dialogState,
        onDismissRequest = { dialogState.value = false },
        title = "帮助中心",
        cancelText = "取消",
        confirmText = "确定"
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .background(color = Color.Blue)
        ) {
            Text("bangzhuzhongxin")
        }
    }
    YBPopup(showpop, title = "提示", content = {}, draggable = true, onClose = { showpop = false })
}


/**
 * 我的功能项
 */

@Composable
private fun MineFunctionItem(
    @DrawableRes icon: Int,
    title: String,
    desc: String,
    clickAction: (() -> Unit)? = null,
    showRightArrow: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = clickAction != null) { clickAction?.invoke() }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painterResource(icon),
            contentDescription = title,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, modifier = Modifier.weight(1f), fontSize = 15.sp, color = MainTextColor)

        Text(desc, fontSize = 14.sp, color = TertiaryTextColor)

        if (clickAction != null && showRightArrow) {
            Icon(
                painterResource(YBIcons.Custom.RightArrow),
                "",
                modifier = Modifier.size(16.dp),
                tint = TertiaryTextColor
            )
        }
    }

}
/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.thecover.media.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration.Short
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult.ActionPerformed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.navOptions
import cn.thecover.media.core.data.LogoutEvent
import cn.thecover.media.core.data.ToastEvent
import cn.thecover.media.core.widget.component.TOAST_TYPE_WARNING
import cn.thecover.media.core.widget.component.YBBackground
import cn.thecover.media.core.widget.component.YBGradientBackground
import cn.thecover.media.core.widget.component.YBImage
import cn.thecover.media.core.widget.component.YBNavigationBar
import cn.thecover.media.core.widget.component.YBNavigationBarItem
import cn.thecover.media.core.widget.component.YBToast
import cn.thecover.media.core.widget.datastore.Keys
import cn.thecover.media.core.widget.datastore.clearData
import cn.thecover.media.core.widget.event.FlowBus
import cn.thecover.media.core.widget.event.showToast
import cn.thecover.media.core.widget.theme.GradientColors
import cn.thecover.media.core.widget.theme.MsgColor
import cn.thecover.media.core.widget.theme.OutlineColor
import cn.thecover.media.feature.basis.home.navigation.navigateToLogin
import cn.thecover.media.navigation.YBNavHost
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.reflect.KClass

@Composable
fun YBApp(
    appState: YBAppState,
    modifier: Modifier = Modifier,
) {
    val snackBarHostState = remember { SnackbarHostState() }

    YBBackground(modifier = modifier) {
        YBGradientBackground(
            gradientColors = GradientColors(),
        ) {
            val isOffline by appState.isOffline.collectAsStateWithLifecycle()

            // If user is not connected to the internet show a snack bar to inform them.
//            val notConnectedMessage = stringResource(R.string.not_connected)
//            LaunchedEffect(isOffline) {
//                if (isOffline) {
//                    snackBarHostState.showSnackbar(
//                        message = notConnectedMessage,
//                        duration = Indefinite,
//                    )
//                }
//            }

            YBApp(
                appState = appState,
                snackBarHostState = snackBarHostState
            )
        }
    }

    YBToast(snackBarHostState = snackBarHostState)
}

@Composable
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class,
)
internal fun YBApp(
    appState: YBAppState,
    snackBarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val currentDestination = appState.currentDestination

    // 判断当前是否在顶级目的地页面或考核数据相关页面
    val currentRoute = currentDestination?.route ?: ""
    val isReviewDataPage = currentRoute.contains("review_data") ||
            currentRoute.contains("DepartmentReviewRoute") ||
            currentRoute.contains("DepartmentTaskReviewRoute") ||
            currentRoute.contains("DepartmentTopRankingRoute") ||
            currentRoute.contains("ManuscriptReviewRoute") ||
            currentRoute.contains("ManuscriptTopRoute") ||
            currentRoute.contains("ManuscriptDiffusionRoute")

    // 只有在顶级页面或考核数据相关页面才显示底部导航栏
    Column {
        MainContent(
            appState, snackBarHostState, modifier = modifier
                .fillMaxWidth()
                .weight(1f)
        )
        if (appState.isTopLevelDestination == true || isReviewDataPage) {
            HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 0.25.dp, color = OutlineColor)
            YBNavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .height(60.dp)
            ) {
                appState.topLevelDestinations.forEachIndexed { index, destination ->
                    val hasUnreadMsg = false
                    val showMsgCount = index == 2
                    val msgModifier = if (hasUnreadMsg) {
                        if (showMsgCount) {
                            Modifier.showMsg(18, MessageType.Number)
                        } else {
                            Modifier.showMsg(18, MessageType.Dot)
                        }
                    } else {
                        Modifier
                    }
                    val selected = currentDestination.isRouteInHierarchy(destination.route)
                    YBNavigationBarItem(
                        selected = selected,
                        onClick = { appState.navigateToTopLevelDestination(destination) },
                        icon = {
                            if (destination.selectedIcon is String) {
                                // 网络图片
                                YBImage(
                                    imageUrl = destination.selectedIcon,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            if (destination.selectedIcon is Int) {
                                // 静态资源
                                YBImage(
                                    placeholder = painterResource(destination.selectedIcon),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        },
                        selectedIcon = {
                            if (destination.unselectedIcon is String) {
                                // 网络图片
                                YBImage(
                                    imageUrl = destination.unselectedIcon,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            if (destination.unselectedIcon is Int) {
                                // 静态资源
                                YBImage(
                                    placeholder = painterResource(destination.unselectedIcon),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        },
                        label = {
                            Text(
                                text = stringResource(destination.iconTextId),
                                fontSize = 12.sp
                            )
                        },
                        modifier = Modifier.then(if (hasUnreadMsg) msgModifier else Modifier),
                    )
                }
            }
        }
    }
}

@Composable
private fun MainContent(
    appState: YBAppState,
    snackBarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val logoutProcessing = remember { mutableStateOf(false) } // 防重复标志

    LaunchedEffect(Unit) {
        FlowBus.observeEvent<ToastEvent>("toast") {
            scope.launch {
                snackBarHostState.showSnackbar(
                    message = it.data.message,
                    actionLabel = it.data.action,
                    duration = Short
                )
            }
        }

        FlowBus.observeEvent<LogoutEvent>("logout") {
            // 防重复检查
            if (!logoutProcessing.value) {
                logoutProcessing.value = true

                scope.launch {
                    try {
                        // 清除token和用户信息缓存
                        clearData(context, Keys.USER_TOKEN)
                        clearData(context, Keys.USER_INFO)

                        withContext(Dispatchers.Main) {
                            appState.navController.navigateToLogin(navOptions {
                                // 清除所有之前的页面
                                popUpTo(appState.navController.graph.id) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            })
                        }
                    } finally {
                        // 重置标志位，允许下次处理
                        delay(1000) // 确保操作完成后再允许
                        logoutProcessing.value = false
                    }
                }
            }
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = {
            SnackbarHost(
                snackBarHostState,
                modifier = Modifier.windowInsetsPadding(
                    WindowInsets.safeDrawing.exclude(
                        WindowInsets.ime,
                    ),
                ),
                snackbar = {
//                    Snackbar(it, contentColor = Red40, containerColor = Orange90)
                }
            )
        },
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal,
                    ),
                ),
        ) {
            Box(
                // Workaround for https://issuetracker.google.com/338478720
                modifier = Modifier.consumeWindowInsets(
                    WindowInsets(0, 0, 0, 0)
                ),
            ) {
                YBNavHost(
                    appState = appState,
                    onShowSnackBar = { message, action ->
                        snackBarHostState.showSnackbar(
                            message = message,
                            actionLabel = action,
                            duration = Short,
                        ) == ActionPerformed
                    },
                )
            }
        }
    }
}

private enum class MessageType {
    Dot,
    Number
}

private fun Modifier.showMsg(
    msgCount: Int = 0,
    type: MessageType = MessageType.Number
): Modifier =
    composed {
        val textMeasurer = rememberTextMeasurer()
        val textLayoutResult = remember("$msgCount") {
            textMeasurer.measure(
                text = "$msgCount",
                style = TextStyle(
                    fontSize = 10.sp,
                    color = Color.White
                )
            )
        }
        drawWithContent {
            drawContent()

            if (type == MessageType.Number) {
                val offset = center + Offset(
                    64.dp.toPx() * .08f,
                    32.dp.toPx() * -.45f - 10.dp.toPx(),
                )
                val horPadding = 3.5f.dp.toPx()
                // 1. 先画圆角背景
                drawRoundRect(
                    color = MsgColor,
                    topLeft = offset,
                    size = Size(
                        textLayoutResult.size.width.toFloat() + horPadding * 2,
                        textLayoutResult.size.height.toFloat()
                    ),
                    cornerRadius = CornerRadius(50.dp.toPx(), 50.dp.toPx())
                )
                // 2. 再画文字（居中）
                drawText(
                    textLayoutResult = textLayoutResult,
                    topLeft = offset.copy(
                        x = offset.x + horPadding,
                    )
                )
            } else {
                // 画小圆点
                drawCircle(
                    MsgColor,
                    radius = 4.dp.toPx(),
                    // This is based on the dimensions of the NavigationBar's "indicator pill";
                    // however, its parameters are private, so we must depend on them implicitly
                    // (NavigationBarTokens.ActiveIndicatorWidth = 64.dp)
                    center = center + Offset(
                        64.dp.toPx() * .2f,
                        32.dp.toPx() * -.45f - 6.dp.toPx(),
                    ),
                )
            }
        }
    }

fun NavDestination?.isRouteInHierarchy(route: KClass<*>) =
    this?.hierarchy?.any {
        it.hasRoute(route)
    } == true

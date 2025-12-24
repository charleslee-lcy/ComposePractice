package cn.thecover.media.feature.review_data

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cn.thecover.media.core.widget.R
import cn.thecover.media.core.widget.component.YBBadge
import cn.thecover.media.core.widget.component.YBImage
import cn.thecover.media.core.widget.component.popup.YBDropdownMenu
import cn.thecover.media.core.widget.event.clickableWithoutRipple
import cn.thecover.media.core.widget.icon.YBIcons
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.feature.review_data.basic_widget.intent.ReviewDataIntent
import cn.thecover.media.feature.review_data.navigation.ManuscriptReviewRoute
import cn.thecover.media.feature.review_data.navigation.ReviewDataNavigationType
import cn.thecover.media.feature.review_data.navigation.reviewDataPage


/**
 *
 * <p> Created by CharlesLee on 2025/7/28
 * 15708478830@163.com
 */
@Composable
internal fun ReviewDataRoute(
    modifier: Modifier = Modifier,
    viewModel: ReviewDataViewModel = hiltViewModel(),
    routeToMsgScreen: () -> Unit = {},
) {
    ReviewDataScreen(modifier = modifier, routeToMsgScreen = routeToMsgScreen,viewModel)
}

@Composable
internal fun ReviewDataScreen(
    modifier: Modifier = Modifier,
    routeToMsgScreen: () -> Unit = {},
    viewModel: ReviewDataViewModel = hiltViewModel(),
) {
    val reviewNavController = rememberNavController()
    val actualStartDestination =  ManuscriptReviewRoute

    val unreadMessageCount = viewModel.unreadMessageCount.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    // 监听页面可见性变化
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    // 页面可见时获取未读消息数量
                    viewModel.handleReviewDataIntent(ReviewDataIntent.GetUnreadMessageCount)
                }

                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    Column {
        TopBar(reviewNavController, unreadMessageCount.value, routeToMsgScreen)
        NavHost(
            navController = reviewNavController,
            startDestination = actualStartDestination,
            modifier = modifier.padding(top = 12.dp)
        ) {
            reviewDataPage(viewModel = viewModel)
        }
    }
}

@Composable
private fun TopBar(
    navController: NavController,
    unreadMessageCount: Int,
    routeToMsgScreen: () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .statusBarsPadding()

    ) {
        val showReviewDataMenu = remember {
            mutableStateOf(false)
        }
        val animRotate = remember { Animatable(0f) }
        // 当菜单状态改变时触发动画
        LaunchedEffect(showReviewDataMenu.value) {
            animRotate.animateTo(
                targetValue = if (showReviewDataMenu.value) 180f else 0f,
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            )
        }
        Row(
            modifier = Modifier
                .clickableWithoutRipple {
                    // 点击下拉菜单时先隐藏软键盘
                    focusManager.clearFocus()
                    showReviewDataMenu.value = !showReviewDataMenu.value
                }
                .padding(horizontal = 10.dp)
                .align(Alignment.Center), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = ReviewDataNavigationType.entries.first {
                    it.route.contains(currentRoute)
                }.cateName,
                color = MainTextColor,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            YBImage(
                modifier = Modifier
                    .size(20.dp)
                    .rotate(animRotate.value),
                placeholder = painterResource(R.mipmap.ic_arrow_down)
            )
            YBDropdownMenu(
                expanded = showReviewDataMenu,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(top = 12.dp),
                data = ReviewDataNavigationType.entries.map {
                    it.cateName
                },
                initialIndex = ReviewDataNavigationType.entries.first {
                    it.route.contains(currentRoute)
                }.ordinal,
                onItemClick = { name, index ->
                    ReviewDataNavigationType.entries[index].navigation(navController)
                }
            )
        }
        YBBadge(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.CenterEnd),
            msgCount = unreadMessageCount,
            showNumber = true

            ) {
            YBImage(
                modifier = Modifier
                    .padding(5.dp)
                    .size(18.dp)
                    .clickableWithoutRipple {
                        routeToMsgScreen()
                    },
                placeholder = painterResource(YBIcons.Custom.Msg)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ReviewDataPreview() {
    YBTheme {
        ReviewDataScreen()
    }
}
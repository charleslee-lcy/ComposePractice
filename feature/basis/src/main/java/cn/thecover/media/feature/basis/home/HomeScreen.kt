package cn.thecover.media.feature.basis.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import cn.thecover.media.core.data.HomeInfo
import cn.thecover.media.core.network.HttpStatus
import cn.thecover.media.core.network.previewRetrofit
import cn.thecover.media.core.widget.R
import cn.thecover.media.core.widget.component.YBBadge
import cn.thecover.media.core.widget.component.YBImage
import cn.thecover.media.core.widget.component.YBToast
import cn.thecover.media.core.widget.component.picker.DateType
import cn.thecover.media.core.widget.component.picker.YBDatePicker
import cn.thecover.media.core.widget.component.showToast
import cn.thecover.media.core.widget.event.clickableWithoutRipple
import cn.thecover.media.core.widget.theme.MainColor
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.PageBackgroundColor
import cn.thecover.media.core.widget.theme.TertiaryTextColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.core.widget.ui.ComponentPreview
import cn.thecover.media.feature.basis.home.navigation.navigateToMessage
import cn.thecover.media.feature.basis.home.ui.LeaderUserContent
import cn.thecover.media.feature.basis.home.ui.ManuscriptTopRankingItem
import cn.thecover.media.feature.basis.home.ui.ReporterUserContent
import java.time.LocalDate


/**
 *
 * <p> Created by CharlesLee on 2025/7/28
 * 15708478830@163.com
 */
@Composable
internal fun HomeRoute(navController: NavController) {
//    val feedState by viewModel..collectAsStateWithLifecycle()
    val goToMessageRoute = {
        navController.navigateToMessage()
    }
    YBTheme {
        HomeScreen(
            routeToMessageScreen = goToMessageRoute, navController = navController
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    routeToMessageScreen: (() -> Unit)? = null,
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()
    val refreshState = rememberPullToRefreshState()
    val isRefreshing = remember { mutableStateOf(false) }
    val shouldScrollToTop = remember { mutableStateOf(false) }

    val userInfo by viewModel.userUiState.collectAsStateWithLifecycle()
    val homeInfo by viewModel.homeUiState.collectAsStateWithLifecycle()
    val homeManuscript by viewModel.homeManuscriptUiState.collectAsStateWithLifecycle()
    val homeManuscriptDiffusion by viewModel.homeManuscriptDiffusionUiState.collectAsStateWithLifecycle()
    val unreadMessageCount by viewModel.unreadMessageCount.collectAsState()

    fun fetchHomeData() {
        // 获取用户信息
        viewModel.getUserInfo(context, navController)
        // 获取首页信息
        viewModel.getHomeInfo(viewModel.curYear.intValue, viewModel.curMonth.intValue)

        // 获取首页信息
        viewModel.getUnreadMessageCount()

        // 获取首页稿件排行榜
        viewModel.getHomeManuscript()
        // 获取首页稿件传播数据
        viewModel.getHomeManuscriptDiffusion()
    }

    LaunchedEffect(Unit) {
        if (!viewModel.hasHomeDataFetched) {
            isRefreshing.value = true
            fetchHomeData()
            viewModel.hasHomeDataFetched = true
        }
    }

    // 监听滚动到顶部的请求
    LaunchedEffect(shouldScrollToTop.value) {
        if (shouldScrollToTop.value) {
            scrollState.animateScrollTo(0)
            shouldScrollToTop.value = false
        }
    }

    LaunchedEffect(homeInfo) {
        isRefreshing.value = homeInfo.status == HttpStatus.LOADING

        if (homeInfo.status == HttpStatus.ERROR && viewModel.canShowToast) {
            snackBarHostState.showToast(homeInfo.errorMsg)
            viewModel.canShowToast = false
        }

        if (homeInfo.status == HttpStatus.SUCCESS) {
            homeInfo.data?.jobType?.apply {
                viewModel.roleState = this
            }
            // 当数据加载成功时，触发滚动到顶部
            shouldScrollToTop.value = true
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TopBar(
            userInfo.nickname,
            viewModel.curYear,
            viewModel.curMonth,
            unreadMessageCount,
            titleClick = {

            }, onDatePick = {
                isRefreshing.value = true
//                viewModel.getHomeInfo(viewModel.curYear.intValue, viewModel.curMonth.intValue)
                // 在日期选择后，触发滚动到顶部再获取数据
                shouldScrollToTop.value = true
                fetchHomeData()
            }, messageClick = {
                routeToMessageScreen?.invoke()
            })

        PullToRefreshBox(
            state = refreshState,
            isRefreshing = isRefreshing.value,
            onRefresh = {
                isRefreshing.value = true
                // 在下拉刷新时，触发滚动到顶部
                shouldScrollToTop.value = true
                fetchHomeData()
            },
            modifier = modifier,
            indicator = {
                Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = isRefreshing.value,
                    containerColor = Color.White,
                    color = MainColor,
                    state = refreshState
                )
            }
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .background(PageBackgroundColor),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Spacer(modifier = Modifier.height(1.dp))
                Crossfade(viewModel.roleState) {
                    if (it == 3) {
                        LeaderUserContent(homeInfo.data ?: HomeInfo())
                    } else if (it == 1){
                        ReporterUserContent(homeInfo.data ?: HomeInfo())
                    }
                }

                // 只有当两个数据列表都不为空时才显示稿件TOP榜单
                if (homeManuscript.dataList?.isNotEmpty() == true && homeManuscriptDiffusion.dataList?.isNotEmpty() == true) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 15.dp, end = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(cn.thecover.media.feature.basis.R.mipmap.icon_home_manu),
                            tint = Color.Unspecified,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "稿件TOP榜单",
                            color = MainTextColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(start = 5.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Row(
                            modifier = Modifier
                                .padding(10.dp)
                                .clickable {
                                    // 使用底部导航栏相同的方式导航到考核数据页面
                                    val navOptions = androidx.navigation.navOptions {
                                        popUpTo(0) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                    navController.navigate(
                                        route = "cn.thecover.media.feature.review_data.navigation.ReviewDataRoute",
                                        navOptions = navOptions
                                    )
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "查看更多",
                                color = TertiaryTextColor,
                                lineHeight = 14.sp,
                                fontSize = 14.sp,
                            )
                            Icon(
                                painterResource(R.drawable.icon_right_arrow),
                                contentDescription = "Localized description",
                                Modifier
                                    .size(18.dp)
                                    .padding(2.dp),
                                tint = TertiaryTextColor
                            )
                        }
                    }

                    ManuscriptTopRankingItem(homeManuscript, homeManuscriptDiffusion, viewModel)
                }

            }
        }
    }

    YBToast(snackBarHostState = snackBarHostState, verticalRate = 0.9f)
}

@Composable
private fun TopBar(
    userName: String,
    currentYear: MutableState<Int>,
    currentMonth: MutableState<Int>,
    unreadMessageCount: Int,
    titleClick: () -> Unit = {},
    onDatePick: () -> Unit = {},
    messageClick: () -> Unit = {}
) {
    var datePickerShow by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .statusBarsPadding()
            .height(40.dp)
    ) {
        Text(
            text = userName,
            color = MainTextColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .align(Alignment.CenterStart)
                .clickableWithoutRipple {
                    titleClick.invoke()
                }
        )
        Row(
            modifier = Modifier
                .clickableWithoutRipple {
                    datePickerShow = true
                }
                .padding(horizontal = 10.dp)
                .align(Alignment.Center), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${currentYear.value}年${currentMonth.value}月",
                color = MainTextColor,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            YBImage(
                modifier = Modifier.size(20.dp),
                placeholder = painterResource(R.mipmap.ic_arrow_down)
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
                    .clickable {
                        messageClick()
                    },
                placeholder = painterResource(R.mipmap.ic_home_msg)
            )
        }
    }

    YBDatePicker(
        visible = datePickerShow,
        value = LocalDate.of(currentYear.value, currentMonth.value, 1),
        start = LocalDate.of(LocalDate.now().year - 5, 1, 1),
        end = LocalDate.of(LocalDate.now().year + 4, 1, 1),
        type = DateType.MONTH,
        onCancel = { datePickerShow = false },
        onChange = {
            currentYear.value = it.year
            currentMonth.value = it.monthValue
            onDatePick.invoke()
        }
    )
}

@ComponentPreview
@Composable
private fun HomeScreenPreview() {
    YBTheme {
        HomeScreen(
            navController = NavController(LocalContext.current),
            viewModel = HomeViewModel(
                SavedStateHandle(),
                retrofit = { previewRetrofit }
            ))
    }
}
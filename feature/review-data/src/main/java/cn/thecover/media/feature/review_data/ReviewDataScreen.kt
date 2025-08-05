package cn.thecover.media.feature.review_data

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import cn.thecover.media.core.widget.R
import cn.thecover.media.core.widget.component.YBBadge
import cn.thecover.media.core.widget.component.YBImage
import cn.thecover.media.core.widget.component.popup.YBDropdownMenu
import cn.thecover.media.core.widget.event.clickableWithoutRipple
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.feature.review_data.navigation.DepartmentTaskReviewRoute
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
    viewModel: ReviewDataViewModel = hiltViewModel()
) {
    ReviewDataScreen(modifier)
}

@Composable
internal fun ReviewDataScreen(
    modifier: Modifier = Modifier
) {
    val reviewNavController = rememberNavController()
    Column {
        TopBar(reviewNavController)
        NavHost(
            navController = reviewNavController,
            startDestination = DepartmentTaskReviewRoute,
            modifier = modifier.padding(start = 16.dp,top=12.dp, end = 16.dp)
        ) {
            reviewDataPage()
        }
    }
}

@Composable
private fun TopBar(navController: NavController) {

    val currentTitle = ReviewDataNavigationType.entries.first {
        it.route.contains(navController.currentDestination?.route ?: "")
    }.cateName

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .statusBarsPadding()
            .height(40.dp)
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
                    showReviewDataMenu.value = !showReviewDataMenu.value
                }
                .padding(horizontal = 10.dp)
                .align(Alignment.Center), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text =currentTitle,
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

                modifier = Modifier.align(Alignment.CenterVertically),
                data = ReviewDataNavigationType.entries.map {
                    it.cateName
                },
                onItemClick = { name, index ->
                    ReviewDataNavigationType.entries[index].navigation(navController)

                }
            )
        }
        YBBadge(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .align(Alignment.CenterEnd),
            msgCount = 10,

            ) {
            YBImage(
                modifier = Modifier
                    .padding(2.dp)
                    .size(18.dp),
                placeholder = painterResource(R.drawable.icon_message)
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
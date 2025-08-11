package cn.thecover.media.feature.review_manager

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cn.thecover.media.core.widget.component.YBBadge
import cn.thecover.media.core.widget.component.YBImage
import cn.thecover.media.core.widget.component.popup.YBDropdownMenu
import cn.thecover.media.core.widget.event.clickableWithoutRipple
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.core.widget.ui.PhonePreview
import cn.thecover.media.feature.review_manager.appeal.AppealManageScreen
import cn.thecover.media.feature.review_manager.assign.DepartmentAssignScreen


/**
 * 稿件打分
 * <p> Created by CharlesLee on 2025/7/28
 * 15708478830@163.com
 */

@Composable
internal fun ReviewManageRoute(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    ReviewManageScreen(modifier = modifier, navController = navController)
}

internal enum class ReviewManageType(val index: Int) {
    ARCHIVE_SCORE(0),
    DEPARTMENT_ASSIGN(1),
    APPEAL_MANAGE(2)
}

@Composable
internal fun ReviewManageScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    var pageType by remember { mutableIntStateOf(ReviewManageType.ARCHIVE_SCORE.index) }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TopBar(pageType) { text, index ->
            if (pageType != index) {
                pageType = index
            }
        }
        when(pageType) {
            ReviewManageType.DEPARTMENT_ASSIGN.index -> {
                // 部门内分配
                DepartmentAssignScreen(navController = navController)
            }
            ReviewManageType.APPEAL_MANAGE.index -> {
                // 申诉管理
                AppealManageScreen(navController = navController)
            }
            else -> {
                // 稿件打分
                ArchiveScoreScreen(navController = navController)
            }
        }
    }
}

@Composable
private fun TopBar(initialIndex: Int, titleClick: (String, Int) -> Unit = {_, _ -> }) {
    val list = listOf("稿件打分", "部门内分配", "申诉管理")
    var expanded = remember { mutableStateOf(false) }
    var title by remember { mutableStateOf(list[initialIndex]) }
    val animRotate = remember { Animatable(0f) }

    // 当菜单状态改变时触发动画
    LaunchedEffect(expanded.value) {
        animRotate.animateTo(
            targetValue = if (expanded.value) 180f else 0f,
            animationSpec = tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .statusBarsPadding()
            .height(40.dp)
    ) {
        YBDropdownMenu(
            data = list,
            initialIndex = initialIndex,
            expanded = expanded,
            modifier = Modifier.align(Alignment.Center),
            onItemClick = { text, index ->
                title = text
                titleClick.invoke(text, index)
            }
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .clickableWithoutRipple {
                        expanded.value = !expanded.value
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = MainTextColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
                YBImage(
                    modifier = Modifier.size(20.dp).rotate(animRotate.value),
                    placeholder = painterResource(cn.thecover.media.core.widget.R.mipmap.ic_arrow_down)
                )
            }
        }
        YBBadge(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .align(Alignment.CenterEnd),
            msgCount = 10,
            showNumber = false
        ) {
            YBImage(
                modifier = Modifier.size(20.dp),
                placeholder = painterResource(cn.thecover.media.core.widget.R.mipmap.ic_home_msg)
            )
        }
    }
}

@PhonePreview
@Composable
private fun ReviewManagePreview() {
    YBTheme {
        ReviewManageScreen(navController = NavController(LocalContext.current))
    }
}
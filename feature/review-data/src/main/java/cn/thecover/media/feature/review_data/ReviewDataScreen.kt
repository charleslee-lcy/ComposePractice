package cn.thecover.media.feature.review_data

import cn.thecover.media.core.widget.R
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cn.thecover.media.core.widget.component.YBBadge
import cn.thecover.media.core.widget.component.YBImage
import cn.thecover.media.core.widget.event.clickableWithoutRipple
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.feature.review_data.department_review.DepartmentReviewScreen
import cn.thecover.media.feature.review_data.department_review.DepartmentTaskReviewScreen


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
        TopBar()
        NavHost(
            navController = reviewNavController,
            startDestination = "total_review_screen",
            modifier = modifier
        ) {
            composable("review_data_screen") {
                DepartmentTaskReviewScreen()
            }
            composable("total_review_screen") {
                DepartmentReviewScreen()
            }
        }
    }
}

@Composable
private fun TopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .statusBarsPadding()
            .height(40.dp)
    ) {
        Row(
            modifier = Modifier
                .clickableWithoutRipple {

                }
                .padding(horizontal = 10.dp)
                .align(Alignment.Center), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "部门总数据排行",
                color = MainTextColor,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            YBImage(
                modifier = Modifier.size(20.dp).clickableWithoutRipple{

                },
                placeholder = painterResource(R.mipmap.ic_arrow_down)
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
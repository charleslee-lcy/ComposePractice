package cn.thecover.media.feature.review_manager.assign

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.feature.review_manager.ReviewManageViewModel


/**
 * 部门内分配
 * <p> Created by CharlesLee on 2025/7/28
 * 15708478830@163.com
 */
@Composable
internal fun DepartmentAssignScreen(
    modifier: Modifier = Modifier,
    viewModel: ReviewManageViewModel = hiltViewModel()
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text("部门内分配")
    }
}

@Preview(showSystemUi = true)
@Composable
private fun DepartmentAssignPreview() {
    YBTheme {
        DepartmentAssignScreen()
    }
}
package cn.thecover.media.core.widget.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay


/**
 *
 * <p> Created by CharlesLee on 2025/7/31
 * 15708478830@163.com
 */

suspend fun SnackbarHostState.showToast(message: String) {
    currentSnackbarData?.dismiss()
    showSnackbar(message, actionLabel = null, duration = SnackbarDuration.Indefinite)
}

/**
 * 自定义toast
 * @param verticalRate 纵向显示的位置，默认为父容器高度的85%
 */
@Composable
fun YBToast(
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    duration: Long = 2000L,
    verticalRate: Float = 0.85f
) {
    LaunchedEffect(snackBarHostState.currentSnackbarData) {
        delay(duration)
        snackBarHostState.currentSnackbarData?.dismiss()
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(Modifier.fillMaxHeight(verticalRate))
        // 手动放置 SnackBarHost 到中央
        SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            CenterSnackBar(it)
        }
        Spacer(Modifier.weight(1f))
    }
}

@Composable
private fun CenterSnackBar(
    snackBarData: SnackbarData,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = Color(0xFFE1E1E1),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 24.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = snackBarData.visuals.message,
            textAlign = TextAlign.Center,
        )
    }
}
package cn.thecover.media.core.widget.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import cn.thecover.media.core.widget.R
import cn.thecover.media.core.widget.theme.MainTextColor
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
 */
@Composable
fun CommonToast(
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    duration: Long = 2000L
) {
    val currentSnackbar = snackBarHostState.currentSnackbarData

    if (currentSnackbar != null) {
        Popup(
            alignment = Alignment.BottomCenter,
            content = {
                CenterSnackBar(currentSnackbar, modifier = Modifier.padding(bottom = 100.dp))
            }
        )

        LaunchedEffect(currentSnackbar) {
            delay(duration)
            currentSnackbar.dismiss()
        }
    }
}

@Composable
private fun CenterSnackBar(
    snackBarData: SnackbarData,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        shape = RoundedCornerShape(25.dp),
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            if (!snackBarData.visuals.actionLabel.isNullOrEmpty()) {
                InlineImageText(
                    image = painterResource(id = when(snackBarData.visuals.actionLabel) {
                        TOAST_TYPE_SUCCESS -> R.mipmap.ic_toast_success
                        TOAST_TYPE_ERROR -> R.mipmap.ic_toast_error
                        else -> R.mipmap.ic_toast_warning
                    }),
                    text = snackBarData.visuals.message
                )
            } else {
                Text(
                    text = snackBarData.visuals.message,
                    textAlign = TextAlign.Center,
                    color = MainTextColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

const val TOAST_TYPE_SUCCESS = "success"
const val TOAST_TYPE_ERROR = "error"
const val TOAST_TYPE_WARNING = "warning"

@Composable
fun InlineImageText(image: Painter, text: String) {
    val inlineContent = mapOf(
        "image" to InlineTextContent(
            placeholder = Placeholder(
                width = 24.sp,
                height = 24.sp,
                placeholderVerticalAlign = PlaceholderVerticalAlign.Center
            )
        ) {
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    )

    val annotatedString = buildAnnotatedString {
        appendInlineContent("image", alternateText = "icon")
        append(" ")
        append(text)
    }

    Text(
        text = annotatedString,
        inlineContent = inlineContent,
        color = MainTextColor,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
    )
}
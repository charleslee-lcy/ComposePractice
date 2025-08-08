package cn.thecover.media.core.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cn.thecover.media.core.widget.theme.MainColor
import cn.thecover.media.core.widget.theme.MsgColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.core.widget.ui.ComponentPreview


/**
 * 定义Shape组件，渐变，圆角
 * <p> Created by CharlesLee on 2025/8/6
 * 15708478830@163.com
 */

val GradientLeftTop = Offset(0f, 0f)
val GradientRightTop = Offset(Float.POSITIVE_INFINITY, 0f)
val GradientLeftBottom = Offset(0f, Float.POSITIVE_INFINITY)
val GradientRightBottom = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)

@Composable
fun YBShape(
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(Color(0xFF00BCD4), Color(0xFF3F51B5)),
    start: Offset = GradientLeftTop,
    end: Offset = GradientRightTop,
    borderWidth: Dp = 0.dp,
    borderColor: Color = Color.White,
    shape: Shape = RoundedCornerShape(0.dp)
) {
    Box(
        modifier = modifier
            .border(
                width = borderWidth,
                color = borderColor,
                shape = shape
            )
            .background(
                brush = Brush.linearGradient(
                    colors = colors,
                    start = start,
                    end = end,
                ),
                shape = shape
            )

    )
}

fun Modifier.gradientShape(
    colors: List<Color> = listOf(Color(0xFF00BCD4), Color(0xFF3F51B5)),
    start: Offset = GradientLeftTop,
    end: Offset = GradientRightTop,
    borderWidth: Dp = 0.dp,
    borderColor: Color = Color.White,
    shape: Shape = RoundedCornerShape(0.dp)
) = this
    .border(
        width = borderWidth,
        color = borderColor,
        shape = shape
    )
    .background(
        brush = Brush.linearGradient(
            colors = colors,
            start = start,
            end = end,
        ),
        shape = shape
    )

@ComponentPreview
@Composable
private fun YBShapePreview() {
    YBTheme {
        Column {
            YBShape(
                modifier = Modifier.size(100.dp, 100.dp),
                colors = listOf(MainColor, MsgColor),
                start = GradientLeftTop,
                end = GradientRightTop
            )
            YBShape(
                modifier = Modifier.size(100.dp, 100.dp),
                start = GradientLeftTop,
                end = GradientLeftBottom,
                shape = RoundedCornerShape(10.dp)
            )
            YBShape(
                modifier = Modifier.size(100.dp, 100.dp),
                colors = listOf(MainColor, MsgColor),
                start = GradientLeftTop,
                end = GradientRightBottom,
                shape = CircleShape
            )
            YBShape(
                modifier = Modifier.size(100.dp, 100.dp),
                colors = listOf(MainColor, MsgColor),
                start = GradientLeftTop,
                end = GradientLeftBottom,
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}
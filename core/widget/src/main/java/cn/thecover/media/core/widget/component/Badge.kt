package cn.thecover.media.core.widget.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.thecover.media.core.widget.theme.MsgColor
import cn.thecover.media.core.widget.theme.CommonTheme
import cn.thecover.media.core.widget.ui.ComponentPreview


/**
 *
 * <p> Created by CharlesLee on 2025/7/31
 * 15708478830@163.com
 */

/**
 * 云报小红点
 * @param content 显示内容
 * @param size 小红点大小
 * @param color 小红点颜色
 * @param alignment 小红点位置
 * @param holder 显示holder
 */
@Composable
fun CommonBadge(
    content: String? = null,
    size: Dp = 10.dp,
    color: Color = Color.Red,
    alignment: Alignment = Alignment.TopEnd,
    holder: (@Composable () -> Unit)? = null
) {
    Box {
        val density = LocalDensity.current
        var localWidth by remember {
            mutableStateOf(0.dp)
        }

        val offsetX = when (alignment) {
            Alignment.TopEnd,
            Alignment.BottomEnd -> localWidth / 2

            Alignment.TopCenter,
            Alignment.BottomCenter,
            Alignment.Center -> 0.dp

            Alignment.CenterStart -> -(localWidth + 8.dp)
            Alignment.CenterEnd -> localWidth + 8.dp

            else -> -localWidth / 2
        }
        val offsetY = when (alignment) {
            Alignment.BottomStart,
            Alignment.BottomCenter,
            Alignment.BottomEnd -> size / 2

            Alignment.CenterEnd,
            Alignment.CenterStart,
            Alignment.Center -> 0.dp

            else -> -size / 2
        }

        holder?.invoke()
        Box(
            modifier = Modifier
                .align(alignment)
                .widthIn(size)
                .height(size)
                .onSizeChanged {
                    with(density) {
                        localWidth = it.width.toDp()
                    }
                }
                .offset(x = offsetX, y = offsetY)
                .clip(if (localWidth > size) RoundedCornerShape(20.dp) else CircleShape)
                .background(color)
                .padding(horizontal = if (localWidth > size && content != null) 6.dp else 0.dp),
            contentAlignment = Alignment.Center
        ) {
            content?.let {
                Text(text = it, color = Color.White, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun CommonBadge(
    modifier: Modifier = Modifier,
    msgCount: Int = 0,
    showNumber: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    BadgedBox(
        modifier = modifier,
        badge = {
            if (msgCount > 0) {
                if (showNumber) {
                    Badge(
                        modifier = Modifier.align(Alignment.TopStart),
                        containerColor = MsgColor,
                        contentColor = Color.White
                    ) {
                        Text(text = if (msgCount > 99) "99+" else "$msgCount", fontSize = 10.sp)
                    }
                } else {
                    Badge(
                        containerColor = MsgColor,
                        contentColor = Color.White
                    )
                }
            }
        },
        content = content
    )
}

@ComponentPreview
@Composable
fun CommonBadgeNumberPreview() {
    CommonTheme {
        CommonBadge(msgCount = 5, showNumber = true) {
            Icon(imageVector = Icons.Filled.Message, contentDescription = null)
        }
    }
}

@ComponentPreview
@Composable
fun CommonBadgeDotPreview() {
    CommonTheme {
        CommonBadge(msgCount = 5, showNumber = false) {
            Icon(imageVector = Icons.Filled.Message, contentDescription = null)
        }
    }
}

@ComponentPreview
@Composable
fun CommonBadgeNoMsgPreview() {
    CommonTheme {
        CommonBadge(msgCount = 0, showNumber = false) {
            Icon(imageVector = Icons.Filled.Message, contentDescription = null)
        }
    }
}
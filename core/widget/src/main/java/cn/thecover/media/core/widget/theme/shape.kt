package cn.thecover.media.core.widget.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 *  Created by Wing at 11:23 on 2025/8/1
 *
 */

val CommonShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(20.dp)
)

// 单独定义常用形状以便直接使用
val SmallShape = RoundedCornerShape(8.dp)
val MediumShape = RoundedCornerShape(12.dp)
val LargeShape = RoundedCornerShape(16.dp)
val CircleShape = RoundedCornerShape(50.dp)
val ButtonShape= RoundedCornerShape(2.dp)
val TopRoundedShape = RoundedCornerShape(
    topStart = 12.dp,
    topEnd = 12.dp,
    bottomStart = 0.dp,
    bottomEnd = 0.dp
)
val BottomRoundedShape = RoundedCornerShape(
    topStart = 0.dp,
    topEnd = 0.dp,
    bottomStart = 12.dp,
    bottomEnd = 12.dp
)
package cn.thecover.media.feature.review_data.basic_widget.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cn.thecover.media.core.widget.event.clickableWithoutRipple
import cn.thecover.media.core.widget.icon.YBIcons
import cn.thecover.media.core.widget.theme.CardOutlineColor
import cn.thecover.media.core.widget.theme.YBShapes
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.feature.review_data.basic_widget.chooseRankingColor

/**
 *  Created by Wing at 09:41 on 2025/8/5
 *  考核数据内部通用卡片样式
 */


/**
 * 通用数据项卡片组件，用于展示考核数据的基本容器。
 *
 * @param content 卡片内部的内容组件。
 */
@Composable
internal fun DataItemCard(
    containerColor: Color = MaterialTheme.colorScheme.surface,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .border(width = 0.5.dp, color = CardOutlineColor, shape = MaterialTheme.shapes.small)
            .fillMaxWidth()
            .background(containerColor, shape = YBShapes.small)
            .padding(horizontal = 12.dp),
        shape = YBShapes.small,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
    ) {
        Column {
            Spacer(modifier = Modifier.height(12.dp))
            content()
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}


/**
 * 可展开/折叠的内容列组件，支持带动画的展开与收起效果。
 *
 * @param offset 展开按钮相对于左边的偏移量（单位：dp）。
 * @param content 主要内容部分。
 * @param foldContent 折叠后隐藏的内容部分。
 */
@Composable
internal fun ExpandItemColumn(
    offset: Int = 0,
    content: @Composable () -> Unit,
    foldContent: @Composable () -> Unit
) {
    var expand by remember { mutableStateOf(false) }
    val animRotate = remember {
        Animatable(0f)
    }
    LaunchedEffect(expand) {
        animRotate.animateTo(
            targetValue = if (expand) 180f else 0f,
            animationSpec = tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing
            )
        )
    }
    Column {
        content()
        Spacer(modifier = Modifier.height(12.dp))
        // 折叠内容区域，使用动画控制显示/隐藏
        AnimatedVisibility(visible = expand) {
            Column {
                foldContent()
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = offset.dp)
                .clickableWithoutRipple {
                    expand = !expand
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(YBIcons.Custom.Expand),
                "折叠展开按钮",
                modifier = Modifier.rotate(animRotate.value),
                tint = Color.Unspecified
            )
        }
    }
}

/**
 * 带有排名信息的数据卡片组件。
 *
 * @param ranking 当前排名数值，默认为 0。
 * @param content 排名之后的主要内容组件。
 */
@Composable
internal fun DataItemRankingRow(ranking: Int = 0, content: @Composable () -> Unit) {
    Row(verticalAlignment = Alignment.Top) {
        Text(
            text = ranking.toString(),
            style = MaterialTheme.typography.titleSmall,
            color = ranking.chooseRankingColor(),
            modifier = Modifier.padding(top = 0.dp, end = 10.dp)
        )
        content()
    }
}


@Composable
@Preview
fun DepartmentReviewDataRankingPreview() {
    YBTheme {
        DataItemRankingRow { }
    }
}


@Composable
@Preview
fun DepartmentReviewDataViewPreview() {
    YBTheme {
        DataItemCard { }
    }
}


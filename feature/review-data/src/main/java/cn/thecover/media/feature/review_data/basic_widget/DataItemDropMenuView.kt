package cn.thecover.media.feature.review_data.basic_widget

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastCoerceAtLeast
import cn.thecover.media.core.widget.R
import cn.thecover.media.core.widget.component.popup.YBAlignDropdownMenu
import cn.thecover.media.core.widget.event.clickableWithoutRipple
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.TertiaryTextColor
import cn.thecover.media.core.widget.theme.YBShapes
import cn.thecover.media.core.widget.theme.YBTheme

/**
 *  Created by Wing at 09:45 on 2025/8/5
 *  考核数据页面选择与下拉框通用组件
 */

@Composable
fun DataItemDropMenuView(
    data: MutableState<String> = mutableStateOf(""),
    label: String = data.value,
    dataList: List<String> = listOf(
        "部门总稿费",
        "部门总完成度",
        "部门总完成人数",
        "部门总完成率",
        "部门总完成时间"
    ),
) {
    val showDrop = remember { mutableStateOf(false) }
    val animRotate = remember { Animatable(0f) }
    // 当菜单状态改变时触发动画
    LaunchedEffect(showDrop.value) {
        animRotate.animateTo(
            targetValue = if (showDrop.value) 180f else 0f,
            animationSpec = tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing
            )
        )
    }
    YBAlignDropdownMenu(
        data = dataList,
        expanded = showDrop,
        initialIndex = dataList.indexOf(data.value).fastCoerceAtLeast(0),
        isItemWidthAlign = true,
        offset = DpOffset(0.dp, 0.dp),
        onItemClick = { text, index ->
            data.value = text
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.background,
                    shape = YBShapes.extraSmall
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .clickableWithoutRipple {
                    showDrop.value = !showDrop.value

                },
            verticalAlignment = Alignment.CenterVertically,

            ) {
            Text(label, style = MaterialTheme.typography.labelMedium, color = MainTextColor)
            Spacer(Modifier.weight(1f))
            Icon(
                painterResource(R.mipmap.ic_arrow_down),
                contentDescription = "${label}下拉筛选按钮",
                tint = TertiaryTextColor,
                modifier = Modifier.rotate(animRotate.value)
            )
        }

    }
}

@Composable
fun DataItemSelectionView(label: String = "", onClick: (() -> Unit)? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.background,
                shape = YBShapes.extraSmall
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clickableWithoutRipple {
                onClick?.invoke()
            },
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = MainTextColor)
        Spacer(Modifier.weight(1f))
        Icon(
            painterResource(R.mipmap.ic_arrow_down),
            contentDescription = "${label}下拉筛选按钮",
            tint = TertiaryTextColor
        )
    }
}


@Composable
@Preview
fun DataItemDropMenuPreview() {
    YBTheme {
        DataItemSelectionView(label = "部门总稿费")
    }
}
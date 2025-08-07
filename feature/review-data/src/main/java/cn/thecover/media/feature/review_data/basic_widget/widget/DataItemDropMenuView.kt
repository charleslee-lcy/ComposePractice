package cn.thecover.media.feature.review_data.basic_widget.widget

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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


/**
 * 下拉菜单选择组件，用于展示和选择数据项。
 *
 * 该组件提供一个带动画效果的下拉菜单，用户点击后可展开选项列表进行选择。
 * 选中项会更新传入的 [data] 状态，并在界面上显示当前选中值。
 *
 * @param data 用于存储和更新当前选中项的可变状态，默认值为空字符串。
 * @param label 显示在下拉框中的标签文本，默认为 [data] 的当前值。
 * @param dataList 下拉菜单中可供选择的数据列表，默认包含部门相关统计项。
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

    // 当菜单展开状态改变时，触发动画旋转图标
    LaunchedEffect(showDrop.value) {
        animRotate.animateTo(
            targetValue = if (showDrop.value) 180f else 0f,
            animationSpec = tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing
            )
        )
    }

    // 构建下拉菜单及其触发区域
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
        // 下拉触发区域：显示当前选中项和箭头图标
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.background,
                    shape = YBShapes.extraSmall
                )
                .border(
                    width = 0.5.dp,
                    shape = MaterialTheme.shapes.extraSmall,
                    color = MaterialTheme.colorScheme.outlineVariant
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


/**
 * 数据项选择视图组件
 *
 * 这是一个可组合的UI组件，用于显示一个可点击的选择项，通常用于下拉筛选功能
 *
 * @param label 显示的标签文本，默认为空字符串
 * @param onClick 点击事件回调函数，可为空，默认为null
 */
@Composable
fun DataItemSelectionView(label: String = "", onClick: (() -> Unit)? = null) {
    // 创建一个行布局容器，包含标签文本、弹性间距和下拉图标
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.background,
                shape = YBShapes.extraSmall
            )
            .border(
                width = 0.5.dp,
                shape = MaterialTheme.shapes.extraSmall,
                color = MaterialTheme.colorScheme.outlineVariant
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
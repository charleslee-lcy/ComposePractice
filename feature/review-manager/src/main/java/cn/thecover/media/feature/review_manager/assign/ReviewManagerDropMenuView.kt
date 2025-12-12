package cn.thecover.media.feature.review_manager.assign

import android.R.attr.label
import android.R.attr.text
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import cn.thecover.media.core.data.DepartmentListData
import cn.thecover.media.core.widget.R
import cn.thecover.media.core.widget.component.popup.DepartmentDropdownMenu
import cn.thecover.media.core.widget.component.popup.YBAlignDropdownMenu
import cn.thecover.media.core.widget.event.clickableWithoutRipple
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.TertiaryTextColor
import cn.thecover.media.core.widget.theme.YBShapes
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.core.widget.ui.ComponentPreview
import cn.thecover.media.feature.review_manager.appeal.FilterType


/**
 *
 *
 * <p> Created by CharlesLee on 2025/8/7
 * 15708478830@163.com
 */


/**
 * 适配多层级的筛选
 */
@Composable
fun DepartmentMultiDropMenuView(
    curDepartItem: DepartmentListData,
    filterData: List<DepartmentListData>,
    filterClick: (DepartmentListData) -> Unit = { _ -> }
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
    DepartmentDropdownMenu(
        curDepartItem = curDepartItem,
        data = filterData,
        expanded = showDrop,
        offset = DpOffset(0.dp, 0.dp),
        onItemClick = { item ->
            filterClick.invoke(item)
        }
    ) {
        // 下拉触发区域：显示当前选中项和箭头图标
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .background(
                    MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(4.dp)
                )
                .border(
                    width = 0.5.dp,
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFFEAEAEB)
                )
                .clickableWithoutRipple {
                    showDrop.value = !showDrop.value
                }
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = curDepartItem.name,
                style = MaterialTheme.typography.labelMedium,
                color = MainTextColor,
                maxLines = 1,
                overflow = TextOverflow.MiddleEllipsis,
            )
            Icon(
                modifier = Modifier
                    .size(18.dp)
                    .rotate(animRotate.value),
                painter = painterResource(R.mipmap.ic_arrow_down_light_grey),
                contentDescription = "${label}下拉筛选按钮",
                tint = TertiaryTextColor
            )
        }
    }
}

@Composable
fun FilterDropMenuView(
    initialIndex: Int = 0,
    filterData: List<FilterType>,
    filterClick: (String, Int) -> Unit = { _, _ -> }
) {
    val showDrop = remember { mutableStateOf(false) }
    val animRotate = remember { Animatable(0f) }
    var title by remember { mutableStateOf(filterData[initialIndex].desc) }

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
        data = filterData.map { it.desc },
        expanded = showDrop,
        initialIndex = initialIndex,
        isItemWidthAlign = true,
        offset = DpOffset(0.dp, 0.dp),
        onItemClick = { text, index ->
            title = text
            filterClick.invoke(text, index)
        }
    ) {
        // 下拉触发区域：显示当前选中项和箭头图标
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .background(
                    MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(4.dp)
                )
                .border(
                    width = 0.5.dp,
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFFEAEAEB)
                )
                .clickableWithoutRipple {
                    showDrop.value = !showDrop.value
                }
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(title, style = MaterialTheme.typography.labelMedium, color = MainTextColor)
            Spacer(Modifier.weight(1f))
            Icon(
                modifier = Modifier
                    .size(18.dp)
                    .rotate(animRotate.value),
                painter = painterResource(R.mipmap.ic_arrow_down_light_grey),
                contentDescription = "${label}下拉筛选按钮",
                tint = TertiaryTextColor
            )
        }
    }
}

@Composable
fun DateSelectionView(
    label: String = "",
    textAlignCenter: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    // 创建一个行布局容器，包含标签文本、弹性间距和下拉图标
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
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
        Text(
            modifier = Modifier.weight(1f),
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MainTextColor,
            textAlign = if (textAlignCenter) TextAlign.Center else TextAlign.Start
        )
        Icon(
            modifier = Modifier.size(18.dp),
            painter = painterResource(R.mipmap.ic_arrow_down_light_grey),
            contentDescription = "${label}下拉筛选按钮",
            tint = TertiaryTextColor
        )
    }
}

@ComponentPreview
@Composable
private fun DataItemDropMenuPreview() {
    YBTheme {
        val filters = listOf(
            FilterType(type = 1, desc = "全媒体编辑中心"),
            FilterType(type = 2, desc = "部门总完成度"),
            FilterType(type = 3, desc = "部门总完成人数"),
            FilterType(type = 4, desc = "部门总完成率"),
            FilterType(type = 5, desc = "部门总完成时间")
        )
        Column {
            FilterDropMenuView(filterData = filters)
            DateSelectionView(label = "时间", textAlignCenter = true)
        }
    }
}
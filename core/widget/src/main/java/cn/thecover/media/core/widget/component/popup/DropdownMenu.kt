package cn.thecover.media.core.widget.component.popup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.thecover.media.core.data.DepartmentListData
import cn.thecover.media.core.widget.component.YBImage
import cn.thecover.media.core.widget.event.clickableWithoutRipple
import cn.thecover.media.core.widget.theme.MainColor
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.OutlineColor
import cn.thecover.media.core.widget.theme.SecondaryTextColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.core.widget.ui.PhonePreview


/**
 *
 * <p> Created by CharlesLee on 2025/7/31
 * 15708478830@163.com
 */

/**
 * 定义与页面等宽的下拉菜单，item项文字居中显示
 */
@Composable
fun DepartmentDropdownMenu(
    modifier: Modifier = Modifier,
    curDepartItem: DepartmentListData,
    data: List<DepartmentListData>,
    expanded: MutableState<Boolean>,
    onItemClick: (item: DepartmentListData) -> Unit = { _ -> },
    cornerRadius: Dp = 0.dp,
    backgroundColor: Color = Color.White,
    offset: DpOffset = DpOffset(0.dp, 10.dp),
    anchor: @Composable ColumnScope.() -> Unit = {},
) {
    // 保存每个item的展开状态
    val expandStates = remember(data) {
        mutableStateMapOf<Long, Boolean>().apply {
            fun initializeExpandStates(items: List<DepartmentListData>?) {
                items?.takeIf {
                    it.isNotEmpty()
                }?.forEach { item ->
                    this[item.id] = false // 默认不展开
                    // 递归处理子层级
                    initializeExpandStates(item.children)
                }
            }
            initializeExpandStates(data)
        }
    }

    Column(modifier = modifier) {
        anchor()
        DropdownMenu(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 300.dp),
            expanded = expanded.value,
            containerColor = backgroundColor,
            shadowElevation = 5.dp,
            offset = offset,
            shape = RoundedCornerShape(cornerRadius),
            onDismissRequest = { expanded.value = false }
        ) {
            data.forEachIndexed { index, item ->
                DepartmentMultiMenuItem(
                    item,
                    curDepartItem,
                    expanded,
                    onItemClick,
                    expandStates,
                    isExpanded = expandStates[item.id] ?: false,
                    onExpandChange = { isExpand ->
                        expandStates[item.id] = isExpand
                    }
                )
                if (index != data.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = OutlineColor,
                        thickness = 0.5.dp
                    )
                }
            }
        }
    }
}

@Composable
fun DepartmentMultiMenuItem(
    item: DepartmentListData,
    currentItem: DepartmentListData,
    expanded: MutableState<Boolean>,
    onItemClick: (item: DepartmentListData) -> Unit = { _ -> },
    expandStates: SnapshotStateMap<Long, Boolean>,
    isExpanded: Boolean = false,
    onExpandChange: (Boolean) -> Unit = {}
) {
    DropdownMenuItem(
        text = {
            val animRotate = remember { Animatable(-90f) }

            // 当菜单状态改变时触发动画
            LaunchedEffect(isExpanded) {
                animRotate.animateTo(
                    targetValue = if (isExpanded) 0f else -90f,
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                )
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    if (!item.children.isNullOrEmpty()) {
                        YBImage(
                            modifier = Modifier
                                .size(20.dp)
                                .rotate(animRotate.value)
                                .clickableWithoutRipple {
                                    onExpandChange(!isExpanded)
                                },
                            placeholder = painterResource(cn.thecover.media.core.widget.R.mipmap.ic_arrow_down)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                    } else {
                        Spacer(modifier = Modifier.width(25.dp))
                    }
                    Text(
                        text = "${item.name}",
                        color = if (currentItem.id == item.id) MainColor else SecondaryTextColor,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 0.dp, top = 12.dp, end = 16.dp, bottom = 12.dp)
                    )
                }
                AnimatedVisibility(visible = !item.children.isNullOrEmpty() && isExpanded) {
                    Column {
                        item.children?.forEachIndexed { childIndex, childItem ->
                            DepartmentMultiMenuItem(childItem, currentItem, expanded, onItemClick, expandStates, isExpanded = expandStates[childItem.id] ?: false,
                                onExpandChange = { isExpand ->
                                    expandStates[childItem.id] = isExpand
                                })
                        }
                    }
                }

            }
        },
        onClick = {
            expanded.value = false
            onItemClick.invoke(item)
        }
    )

}


@Composable
fun YBDropdownMenu(
    modifier: Modifier = Modifier,
    initialIndex: Int = 0,
    data: List<String> = listOf<String>(),
    expanded: MutableState<Boolean>,
    onItemClick: (text: String, index: Int) -> Unit = { _, _ -> },
    cornerRadius: Dp = 0.dp,
    backgroundColor: Color = Color.White,
    offset: DpOffset = DpOffset(0.dp, 10.dp),
    anchor: @Composable ColumnScope.() -> Unit = {},
) {
    var currentIndex by remember(initialIndex) { mutableIntStateOf(initialIndex) }

    Column(modifier = modifier) {
        anchor()
        DropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            expanded = expanded.value,
            containerColor = backgroundColor,
            shadowElevation = 5.dp,
            offset = offset,
            shape = RoundedCornerShape(cornerRadius),
            onDismissRequest = { expanded.value = false }
        ) {
            data.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "$item",
                            color = if (index == currentIndex) MainColor else SecondaryTextColor,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                        )
                    },
                    onClick = {
                        expanded.value = false
                        currentIndex = index
                        onItemClick.invoke(item, index)
                    }
                )
                if (index != data.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = OutlineColor,
                        thickness = 0.5.dp
                    )
                }
            }
        }
    }
}

/**
 * 定义自适应item宽度或与anchor组件等宽的下拉菜单，item项文字居左显示
 * @param isItemWidthAlign true item宽度与组件Modifier.width()对齐，false 不对齐，宽度自适应
 */
@Composable
fun YBAlignDropdownMenu(
    modifier: Modifier = Modifier,
    initialIndex: Int = 0,
    data: List<String> = listOf<String>(),
    expanded: MutableState<Boolean>,
    onItemClick: (text: String, index: Int) -> Unit = { _, _ -> },
    isItemWidthAlign: Boolean = false,
    cornerRadius: Dp = 4.dp,
    backgroundColor: Color = Color.White,
    offset: DpOffset = DpOffset(0.dp, 10.dp),
    anchor: @Composable ColumnScope.() -> Unit = {},
) {
    var currentIndex by remember { mutableIntStateOf(initialIndex) }
    var targetWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Column(modifier = modifier.onSizeChanged {
        with(density) {
            targetWidth = it.width.toDp()
        }
    }) {
        anchor()
        DropdownMenu(
            expanded = expanded.value,
            modifier = if (isItemWidthAlign) Modifier.width(targetWidth) else Modifier.wrapContentWidth(),
            containerColor = backgroundColor,
            shadowElevation = 2.dp,
            offset = offset,
            shape = RoundedCornerShape(cornerRadius),
            onDismissRequest = { expanded.value = false }
        ) {
            data.forEachIndexed { index, item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickableWithoutRipple {
                            expanded.value = false
                            currentIndex = index
                            onItemClick.invoke(item, index)
                        }
                        .padding(horizontal = 12.dp, vertical = 8.5.dp)
                ) {
                    Text(
                        text = "$item",
                        color = if (index == currentIndex) MainColor else SecondaryTextColor,
                        fontSize = 13.sp,
                        lineHeight = 13.sp,
                    )
                }
                if (index != data.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        color = OutlineColor,
                        thickness = 0.5.dp
                    )
                }
            }
        }
    }
}

//@PhonePreview
@Composable
fun YBDropdownMenuPreview() {
    YBTheme {
        val list = listOf(
            "稿件打分",
            "部门内分配",
            "申诉管理"
        )
        var expanded = remember { mutableStateOf(false) }
        var title by remember { mutableStateOf(list[0]) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .statusBarsPadding()
                .height(40.dp)
        ) {
            YBDropdownMenu(
                data = list,
                expanded = expanded,
                modifier = Modifier.align(Alignment.Center),
                onItemClick = { text, index ->
                    title = text
                }
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .clickableWithoutRipple {
                            expanded.value = !expanded.value
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        color = MainTextColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                    YBImage(
                        modifier = Modifier.size(20.dp),
                        placeholder = painterResource(cn.thecover.media.core.widget.R.mipmap.ic_arrow_down)
                    )
                }
            }
        }
    }
}

@PhonePreview
@Composable
fun YBAlignDropdownMenuPreview() {
    YBTheme {
        val list = listOf(
            "稿件打分",
            "部门内分配",
            "申诉管理"
        )
        val initialIndex = 2
        var expanded = remember { mutableStateOf(false) }
        var title by remember { mutableStateOf(list[initialIndex]) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .statusBarsPadding()
                .height(40.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            YBAlignDropdownMenu(
                data = list,
                expanded = expanded,
                initialIndex = initialIndex,
                isItemWidthAlign = true,
                onItemClick = { text, index ->
                    title = text
                },
                modifier = Modifier
                    .width(200.dp)
                    .padding(start = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .clickableWithoutRipple {
                            expanded.value = !expanded.value
                        }
                        .padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        color = MainTextColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                    YBImage(
                        modifier = Modifier.size(20.dp),
                        placeholder = painterResource(cn.thecover.media.core.widget.R.mipmap.ic_arrow_down)
                    )
                }
            }
        }
    }
}
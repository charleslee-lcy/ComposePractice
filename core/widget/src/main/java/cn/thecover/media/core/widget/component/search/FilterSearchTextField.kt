package cn.thecover.media.core.widget.component.search

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastCoerceAtLeast
import cn.thecover.media.core.widget.R
import cn.thecover.media.core.widget.component.popup.YBAlignDropdownMenu
import cn.thecover.media.core.widget.event.clickableWithoutRipple
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.TertiaryTextColor
import cn.thecover.media.core.widget.theme.YBShapes

/**
 *  Created by Wing at 09:41 on 2025/8/8
 *  带筛选项的搜索框
 */


/**
 * 筛选搜索组合项，包含一个下拉筛选菜单和一个文本输入框。
 *
 * @param dataList 下拉菜单的数据列表，默认为 ["稿件名称", "记者", "稿件ID"]
 * @param data 当前选中的下拉菜单项，使用 MutableState 包装以便响应式更新
 * @param label 输入框的提示文本
 */
@Composable
fun FilterSearchTextField(
    dataList: List<String> = listOf(
        "稿件名称", "稿件 ID", "记者"
    ),
    data: MutableState<String>, label: String = "请输入搜索内容",
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    onValueChange: (String, String) -> Unit = { _, _ -> },
    onSearch: (String, String) -> Unit = { _, _ -> },
) {
    // 控制下拉菜单是否展开的状态
    val showDrop = remember { mutableStateOf(false) }
    // 动画旋转值，用于下拉箭头图标旋转效果
    val animRotate = remember { Animatable(0f) }
    // 文本输入框的内容状态
    val textState = remember { mutableStateOf("") }

    // 当菜单展开状态改变时，触发动画旋转箭头图标
    LaunchedEffect(showDrop.value) {
        animRotate.animateTo(
            targetValue = if (showDrop.value) 180f else 0f,
            animationSpec = tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing
            )
        )
    }

    // 主容器布局：包含下拉菜单和输入框
    Row(
        modifier = Modifier
            .border(
                width = 0.5.dp,
                shape = MaterialTheme.shapes.extraSmall,
                color = MaterialTheme.colorScheme.outlineVariant
            )
            .fillMaxWidth()
            .background(backgroundColor, shape = YBShapes.extraSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 下拉菜单组件，用于选择筛选类型
        YBAlignDropdownMenu(
            data = dataList,
            expanded = showDrop,
            initialIndex = dataList.indexOf(data.value).fastCoerceAtLeast(0),
            isItemWidthAlign = true,
            offset = DpOffset(0.dp, 0.dp),
            onItemClick = { text, index ->
                data.value = text
            },
            backgroundColor = backgroundColor
        ) {
            // 触发下拉菜单展开的点击区域
            Row(
                modifier = Modifier
                    .background(
                        backgroundColor,
                        shape = YBShapes.extraSmall
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .clickableWithoutRipple {
                        showDrop.value = !showDrop.value
                    },
                verticalAlignment = Alignment.CenterVertically,

                ) {
                // 显示当前选中的筛选项文本
                Text(
                    data.value,
                    style = MaterialTheme.typography.labelMedium,
                    color = MainTextColor
                )
                Spacer(Modifier.width(4.dp))
                // 下拉箭头图标，根据菜单状态旋转
                Icon(
                    painterResource(R.mipmap.ic_arrow_down),
                    contentDescription = "${label}下拉筛选按钮",
                    tint = TertiaryTextColor,
                    modifier = Modifier.rotate(animRotate.value)
                )

            }

        }

        // 分割线，分隔下拉菜单和输入框
        VerticalDivider(
            modifier = Modifier
                .padding(end = 12.dp)
                .width(0.5.dp)
                .height(20.dp),
            color = MaterialTheme.colorScheme.outline,
        )

        // 文本输入框，用于输入搜索关键词
        BasicTextField(
            value = textState.value,
            onValueChange = {
                textState.value = it
                onValueChange(data.value, textState.value)
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch(data.value, textState.value)
                },
                onDone = {
                    // 兼容低版本系统，当回车键按下时也执行搜索
                    onSearch(data.value, textState.value)
                },
                onGo = {
                    // 兼容更多键盘类型，执行搜索
                    onSearch(data.value, textState.value)
                }
            ),
            modifier = Modifier.fillMaxWidth(),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Box(modifier = Modifier.weight(1f)) {
                            // 当输入框为空时显示提示文本
                            if (textState.value.isEmpty()) {
                                Text(
                                    label,
                                    color = TertiaryTextColor,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                            // 实际的输入框内容
                            innerTextField()
                        }

                    }
                }
            }
        )
    }
}

 
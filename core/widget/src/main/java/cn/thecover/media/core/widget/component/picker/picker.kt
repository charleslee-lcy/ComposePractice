package cn.thecover.media.core.widget.component.picker


import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.thecover.media.core.widget.component.YBButton
import cn.thecover.media.core.widget.component.popup.YBPopup
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.TertiaryTextColor

import kotlin.math.roundToInt

/**
 *  Created by Wing at 11:29 on 2025/7/31
 *  基础选择器组件
 */


/**
 * 选择器
 * @param visible 是否显示
 * @param ranges 可选范围
 * @param values 初始值
 * @param title 标题
 * @param onCancel 取消回调
 * @param onValuesChange 选项改变回调
 * @param onColumnValueChange 列选项改变回调
 *
 */
@Composable
fun YBPicker(
    visible: Boolean,
    ranges: Array<List<String>>,
    values: Array<Int>,
    title: String? = null,
    onCancel: () -> Unit,
    onColumnValueChange: ((column: Int, value: Int, values: Array<Int>) -> Unit)? = null,
    onValuesChange: (Array<Int>) -> Unit
) {
    val localValues = remember(visible) { values.copyOf() }

    YBPopup(
        visible,
        title = title,
        enterTransition = fadeIn(tween(150)) + slideInVertically(tween(150)) { it / 3 },
        exitTransition = fadeOut(tween(150)) + slideOutVertically(tween(150)) { it / 3 },
        draggable = false,
        onClose = onCancel
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.background(color=MaterialTheme.colorScheme.background)) {
            Box(
                modifier = Modifier
                 .height(280.dp)
                 .drawIndicator(
                     //选中时间上方蒙层
                     if (isSystemInDarkTheme()) {
                         Color(0xff202020)
                     } else {
                         Color(0xFFF7F7F7)
                     }
                 )
            ) {
                // 可选列表
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ranges.forEachIndexed { index, options ->
                        ColumnItem(
                            options = options,
                            index = localValues[index]
                        ) {
                            localValues[index] = it
                            onColumnValueChange?.invoke(index, it, localValues.copyOf())
                        }
                    }
                }
                // 遮罩层
                Mask()
            }

            Spacer(modifier = Modifier.height(20.dp))
            // 操作栏
            ActionBar(onCancel) {
                onValuesChange(localValues)
                onCancel()
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RowScope.ColumnItem(
    options: List<String>,
    index: Int,
    onChange: (Int) -> Unit
) {
    val itemHeight = 56.dp
    val verticalPadding = remember { (280.dp - itemHeight) / 2 }
    val listState = rememberLazyListState(index)



    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect {
                onChange(it)
                if (it != index) {
                }
            }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.weight(1f),
        contentPadding = PaddingValues(vertical = verticalPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        flingBehavior = rememberSnapFlingBehavior(listState)
    ) {
        items(options) {
            Box(
                modifier = Modifier
                 .fillMaxWidth()
                 .height(itemHeight),
                contentAlignment = Alignment.Center
            ) {
                Text(text = it, color = MainTextColor, fontSize = 17.sp)
            }
        }
    }
}

@Composable
private fun Mask() {
    @Composable
    fun ColumnScope.MaskItem(lightColors: List<Color>, darkColors: List<Color>) {
        Box(
            modifier = Modifier
             .fillMaxWidth()
             .weight(1f)
             .background(
              Brush.verticalGradient(
               colors = if (isSystemInDarkTheme()) {
                darkColors
               } else {
                lightColors
               }
              )
             )
        )
    }

    Column {
        MaskItem(
            lightColors = listOf(
                Color(255, 255, 255, (255 * 0.95).roundToInt()),
                Color(255, 255, 255, (255 * 0.6).roundToInt())
            ),
            darkColors = listOf(
                Color(25, 25, 25, (25 * 0.95).roundToInt()),
                Color(25, 25, 25, (25 * 0.6).roundToInt())
            )
        )
        Box(modifier = Modifier.height(56.dp))
        MaskItem(
            lightColors = listOf(
                Color(255, 255, 255, (255 * 0.6).roundToInt()),
                Color(255, 255, 255, (255 * 0.95).roundToInt())
            ),
            darkColors = listOf(
                Color(25, 25, 25, (25 * 0.6).roundToInt()),
                Color(25, 25, 25, (25 * 0.95).roundToInt())
            )
        )
    }
}

@Composable
private fun Modifier.drawIndicator(color: Color) = this.drawBehind {
    drawRoundRect(
        color,
        topLeft = Offset(0f, size.height / 2 - 56.dp.toPx() / 2),
        size = Size(size.width, 56.dp.toPx()),
        cornerRadius = CornerRadius(6.dp.toPx())
    )
}

@Composable
private fun ActionBar(onCancel: () -> Unit, onConfirm: () -> Unit) {
    Row {
        YBButton(
            onClick = {
                onCancel.invoke()

            },
            modifier = Modifier.weight(1f),
            textColor = TertiaryTextColor,
            backgroundColor = MaterialTheme.colorScheme.background,
            borderColor = TertiaryTextColor,
            shape = MaterialTheme.shapes.extraSmall
        ) {
            Text(text = "取消")
        }
        Spacer(modifier = Modifier.width(20.dp))
        YBButton(
            onClick = {
                onConfirm.invoke()

            },
            modifier = Modifier.weight(1f),
            shape = MaterialTheme.shapes.extraSmall
        ) {
            Text(text = "确认")
        }
    }
}
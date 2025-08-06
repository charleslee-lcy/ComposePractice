package cn.thecover.media.feature.review_data.basic_widget

import androidx.compose.animation.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.SecondaryTextColor
import cn.thecover.media.core.widget.theme.YBShapes
import cn.thecover.media.core.widget.theme.YBTheme

/**
 *  Created by Wing at 16:13 on 2025/8/6
 *  分数容器组件
 */


/**
 * 数据评分项视图组件
 *
 * 该组件用于显示数据评分中的单个选项，支持选中状态的动画效果和不同的颜色主题
 *
 * @param modifier 修饰符，用于设置组件的布局属性
 * @param item 评分项的标签文本
 * @param value 评分项的值文本
 * @param isSelected 是否为选中状态，默认为false
 */
@Composable
internal fun DataScoreItemView(
    modifier: Modifier = Modifier,
    item: String,
    value: String,
    isSelected: Boolean = false,
) {
    // 根据选中状态计算背景颜色目标值
    val targetBgColor =
        if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
    val animatedColor = remember { Animatable(targetBgColor) }

    // 根据选中状态计算文本颜色目标值
    val targetTextColor =
        if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MainTextColor
    val animatedTextColor = remember { Animatable(targetTextColor) }

    // 根据选中状态计算项目标签颜色
    val targetItemColor =
        if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer.copy(0.7f) else SecondaryTextColor

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                shape = YBShapes.small
            )
            .background(
                color = animatedColor.value,
                shape = YBShapes.small
            )
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            item,
            style = MaterialTheme.typography.bodySmall,
            color = targetItemColor,
            maxLines = 2,
            textAlign = TextAlign.Center
            // modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE)
        )
        Spacer(Modifier.height(5.dp))
        Text(
            value,
            style = MaterialTheme.typography.titleSmall,
            color = animatedTextColor.value,
            maxLines = 2,
            textAlign = TextAlign.Center,
            //modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE)
        )
    }
}


/**
 * 评分数据项行组件，用于水平排列多个数据评分项
 *
 * @param items 可变参数，每个元素为Pair类型，包含标签和值的字符串对
 * @param modifier 修饰符，用于设置组件的样式和布局属性
 */
@Composable
fun ReviewDataItemScoreRow(
    vararg items: Pair<String, String>,
    modifier: Modifier = Modifier
) {
    // 创建水平排列的行容器，填充最大宽度，元素间间距12dp
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 遍历所有数据项，为每个项创建数据评分视图
        items.forEach { item ->
            DataScoreItemView(
                item = item.first,
                value = item.second,
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Composable
@Preview
fun DepartmentReviewDataItemPreview() {
    YBTheme {
        DataScoreItemView(item = "一级媒体转载数", value = "2222")
    }
}
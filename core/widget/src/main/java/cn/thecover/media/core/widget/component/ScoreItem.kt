package cn.thecover.media.core.widget.component

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
import androidx.compose.ui.graphics.Color
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



const val SCORE_ITEM_TYPE_NORMAL = 0
const val SCORE_ITEM_TYPE_NORMAL_WITH_BORDER = 1
const val SCORE_ITEM_TYPE_SELECTED = 2
const val SCORE_ITEM_TYPE_SELECTED_WITH_BORDER = 3

/**
 * 数据评分项视图组件
 *
 * 该组件用于显示数据评分中的单个选项，支持选中状态的动画效果和不同的颜色主题
 *
 * @param modifier 修饰符，用于设置组件的布局属性
 * @param item 评分项的标签文本
 * @param value 评分项的值文本
 * @param backgroundColor 评分项的背景颜色
 * @param textColor 评分项的文本颜色
 * @param labelColor 评分项的标签颜色
 * @param borderColor 评分项的边框颜色
 *
 */
@Composable
internal fun DataScoreItem(
    modifier: Modifier = Modifier,
    item: String,
    value: String,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    textColor: Color = MainTextColor,
    labelColor: Color = SecondaryTextColor,
    borderColor: Color=backgroundColor
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = borderColor,
                shape = YBShapes.small
            )
            .background(
                color = backgroundColor,
                shape = YBShapes.small
            )
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            item,
            style = MaterialTheme.typography.bodySmall,
            color = labelColor,
            maxLines = 2,
            textAlign = TextAlign.Center
            // modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE)
        )
        Spacer(Modifier.height(5.dp))
        Text(
            value,
            style = MaterialTheme.typography.titleSmall,
            color = textColor,
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
fun ItemScoreRow(
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
            DataScoreItem(
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
        Column {
            DataScoreItem(item = "一级媒体转载数", value = "2222")

            Spacer(Modifier.height(12.dp))
            ItemScoreRow( items = arrayOf(
                Pair("阅读数", "1"),
                Pair("分享数", "10"),
                Pair("点赞数", "100"),
                Pair("评论数", "1000"),
            )
            )
        }

    }
}
package cn.thecover.media.feature.review_data.basic_widget.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.SecondaryTextColor

/**
 *  Created by Wing at 10:50 on 2025/8/6
 *  稿件Item的头部信息，都是文章加标题加排序号所以单独抽成一个通用组件
 *
 *  @param num 稿件排序号
 *  @param title 稿件名称
 *  @param author 作者名称
 *  @param editor 编辑名称
 */

@Composable
internal fun ManuScriptItemHeader(
    num: Int = 0,
    title: String = "",
    author: String = "",
    editor: String = "",
) {
    // 稿件名称
    Column {
        Text(title, style = MaterialTheme.typography.titleSmall, color = MainTextColor)
        Spacer(Modifier.height(8.dp))
        // 显示作者和编辑信息
        when {
            author.isNotEmpty() && editor.isNotEmpty() -> {
                // 记者和编辑都存在，使用均分的布局
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                    ) {
                        Text(
                            "记者：${author}",
                            style = MaterialTheme.typography.bodySmall,
                            color = SecondaryTextColor,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(Modifier.width(32.dp))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            "编辑：${editor}",
                            style = MaterialTheme.typography.bodySmall,
                            color = SecondaryTextColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            author.isNotEmpty() -> {
                // 只有记者
                Text(
                    "记者：${author}",
                    style = MaterialTheme.typography.bodySmall,
                    color = SecondaryTextColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            editor.isNotEmpty() -> {
                // 只有编辑
                Text(
                    "编辑：${editor}",
                    style = MaterialTheme.typography.bodySmall,
                    color = SecondaryTextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

 
 
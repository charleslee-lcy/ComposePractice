package cn.thecover.media.feature.review_manager.appeal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.thecover.media.core.data.AppealListData
import cn.thecover.media.core.widget.theme.HintTextColor
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.PageBackgroundColor
import cn.thecover.media.core.widget.theme.TertiaryTextColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.core.widget.ui.PhonePreview
import java.text.SimpleDateFormat
import java.util.Locale


/**
 * 申诉管理列表item
 * <p> Created by CharlesLee on 2025/8/5
 * 15708478830@163.com
 */
@Composable
fun AppealListItem(
    modifier: Modifier = Modifier,
    item: AppealListData,
) {
    val formattedTime = remember(item.submitTime) {
        try {
            val originalFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val targetFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val date = originalFormat.parse(item.submitTime)
            targetFormat.format(date)
        } catch (e: Exception) {
            item.submitTime
        }
    }

    Box(
        modifier = modifier.padding(top = 12.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            shape = RoundedCornerShape(8.dp),
        ) {
            Text(
                modifier = Modifier.padding(start = 12.dp, top = 12.dp),
                text = "申诉ID：${item.id}",
                style = TextStyle(
                    color = TertiaryTextColor, fontSize = 14.sp
                )
            )
            if (item.appealTitle.isNotEmpty()) {
                Text(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    text = item.appealTitle,
                    style = TextStyle(
                        color = MainTextColor, fontSize = 14.sp, fontWeight = FontWeight.SemiBold
                    )
                )
            } else {
                Spacer(modifier = Modifier.height(8.dp))
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                colors = CardDefaults.cardColors(containerColor = PageBackgroundColor),
                shape = RoundedCornerShape(0.dp),
            ) {
                Text(
                    modifier = Modifier.padding(start = 12.dp, top = 8.dp),
                    text = "申诉内容：",
                    style = TextStyle(
                        color = TertiaryTextColor, fontSize = 14.sp
                    )
                )
                Text(
                    modifier = Modifier.padding(
                        start = 12.dp,
                        top = 4.dp,
                        end = 12.dp,
                        bottom = 8.dp
                    ),
                    text = item.content,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        color = MainTextColor, fontSize = 14.sp
                    )
                )
            }
            Row(
                modifier = Modifier.padding(start = 12.dp, top = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(HintTextColor)
                        .padding(horizontal = 4.dp, vertical = 1.dp),
                    text = item.typeName,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        color = Color.White, fontSize = 12.sp
                    )
                )
                Text(
                    modifier = Modifier.padding(start = 12.dp),
                    text = formattedTime,
                    style = TextStyle(
                        color = TertiaryTextColor, fontSize = 14.sp
                    )
                )
            }

        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .labelStyle(item.status)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = labelText(item.status),
                color = Color.White, fontSize = 14.sp, textAlign = TextAlign.Center,
            )
        }
    }
}

private fun Modifier.labelStyle(type: Int = 0): Modifier {
    val containerColor = when (type) {
        1 -> Color(0xFF306CFF) // 审批中
        2 -> Color(0xFFFFB833) // 待回复
        3 -> Color(0xFF52D988)   // 已回复
        4 -> Color(0xFFF95252)   // 驳回
        5 -> Color(0xFFBBD1E5)   // 已撤回
        else -> Color(0xFFFFB833) // 待处理
    }
    return this
        .clip(RoundedCornerShape(bottomStart = 8.dp, topEnd = 8.dp))
        .background(containerColor)
        .width(66.dp)
        .height(32.dp)
}

private fun labelText(type: Int = 0) = when (type) {
    1 -> "审批中" // 审批中
    2 -> "待回复"   // 待回复
    3 -> "已回复"   // 已回复
    4 -> "驳回"   // 驳回
    5 -> "已撤回"   // 已撤回
    else -> "待处理" // 待回复
}

private fun handleAppealType(type: Int = 0) = when (type) {
    2 -> "稿件加分"   // 驳回
    3 -> "人员加分"   // 已撤回
    else -> "稿件补录" // 待回复
}

@PhonePreview
@Composable
fun AppealListItemPreview() {
    YBTheme {
        LazyColumn {
            repeat(5) {
                item {
                    val item = AppealListData()
                    AppealListItem(item = item)
                }
            }
        }
    }
}
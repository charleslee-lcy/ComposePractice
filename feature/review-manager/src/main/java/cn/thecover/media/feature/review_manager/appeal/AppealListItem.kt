package cn.thecover.media.feature.review_manager.appeal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import cn.thecover.media.core.widget.theme.HintTextColor
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.PageBackgroundColor
import cn.thecover.media.core.widget.theme.TertiaryTextColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.core.widget.ui.PhonePreview
import kotlin.random.Random


/**
 * 申诉管理列表item
 * <p> Created by CharlesLee on 2025/8/5
 * 15708478830@163.com
 */
@Composable
fun AppealListItem(
    modifier: Modifier = Modifier,
    index: Int = 0
) {
    val type = Random.nextInt(4)

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
                text = "${index}申诉ID：298237821321",
                style = TextStyle(
                    color = TertiaryTextColor, fontSize = 14.sp
                )
            )
            Text(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                text = "关于云南，你不知道的20个冷知识，带你了解最真实的云南风貌",
                style = TextStyle(
                    color = MainTextColor, fontSize = 14.sp, fontWeight = FontWeight.SemiBold
                )
            )
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
                    text = "申诉内容最多两排超过申诉内容最多多两排超过申诉内容最多两排超过申诉内容最多两排超申诉内容最多两排超过申诉内容最多多两排超过申诉内容最多两排超过申诉内容最多两排超申诉内容最多两排超过申诉内容最多多两排超过申诉内容最多两排超过申诉内容最多两排超",
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
                    text = "稿件补录",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        color = Color.White, fontSize = 12.sp
                    )
                )
                Text(
                    modifier = Modifier.padding(start = 12.dp),
                    text = "2025-06-07 14:32",
                    style = TextStyle(
                        color = TertiaryTextColor, fontSize = 14.sp
                    )
                )
            }

        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .labelStyle(type)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = labelText(type),
                color = Color.White, fontSize = 14.sp, textAlign = TextAlign.Center,
            )
        }
    }
}

private fun Modifier.labelStyle(type: Int = 0): Modifier {
    val containerColor = when (type) {
        0 -> Color(0xFF306CFF) // 审批中
        1 -> Color(0xFF52D988)   // 已回复
        2 -> Color(0xFFF95252)   // 驳回
        3 -> Color(0xFFBBD1E5)   // 已撤回
        else -> Color(0xFFFFB833) // 待回复
    }
    return this
        .clip(RoundedCornerShape(bottomStart = 8.dp, topEnd = 8.dp))
        .background(containerColor)
        .width(66.dp)
        .height(32.dp)
}

private fun labelText(type: Int = 0) = when (type) {
    0 -> "审批中" // 审批中
    1 -> "已回复"   // 已回复
    2 -> "驳回"   // 驳回
    3 -> "已撤回"   // 已撤回
    else -> "待回复" // 待回复
}

@PhonePreview
@Composable
fun AppealListItemPreview() {
    YBTheme {
        LazyColumn {
            repeat(5) {
                item {
                    AppealListItem()
                }
            }
        }
    }
}
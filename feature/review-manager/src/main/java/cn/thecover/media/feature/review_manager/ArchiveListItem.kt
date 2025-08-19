package cn.thecover.media.feature.review_manager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.core.text.toHtml
import cn.thecover.media.core.widget.component.YBImage
import cn.thecover.media.core.widget.event.clickableWithoutRipple
import cn.thecover.media.core.widget.theme.MainColor
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.OutlineColor
import cn.thecover.media.core.widget.theme.SecondaryAuxiliaryColor
import cn.thecover.media.core.widget.theme.TertiaryTextColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.core.widget.ui.PhonePreview
import kotlinx.serialization.Serializable


/**
 * 部门内分配列表item
 * <p> Created by CharlesLee on 2025/8/7
 * 15708478830@163.com
 */

@Composable
fun ArchiveListItem(
    modifier: Modifier = Modifier,
    item: ArchiveListData,
    onDetailClick: (() -> Unit)? = null,
    onScoreClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier.padding(top = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            Text(
                text = HtmlCompat.fromHtml(item.title, HtmlCompat.FROM_HTML_MODE_COMPACT).toString(),
                style = TextStyle(
                    color = MainTextColor,
                    fontSize = 15.sp,
                    lineHeight = 15.sp * 1.3f,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.padding(top = 12.dp)
            )
            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "周国超 / 张大文 / 李四",
                    color = TertiaryTextColor,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "图片",
                    color = TertiaryTextColor,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .background(
                            color = Color(0xFFF2F2F2),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 5.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "待打分",
                    color = Color.White,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .background(
                            color = MainColor,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 5.dp)
                )
            }
            Row(
                modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.niceDate,
                    color = TertiaryTextColor,
                    lineHeight = 13.sp,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "3人已打",
                    color = SecondaryAuxiliaryColor,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(end = 10.dp)
                )
                Text(
                    text = "未完成",
                    color = Color.White,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .background(
                            color = SecondaryAuxiliaryColor,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 5.dp)
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(top = 10.dp),
                thickness = 0.5.dp,
                color = OutlineColor
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxHeight().weight(1f).clickableWithoutRipple {
                        // 查看稿件详情
                        onDetailClick?.invoke()
                    }
                ) {
                    YBImage(
                        modifier = Modifier.size(16.dp),
                        placeholder = painterResource(R.mipmap.ic_archive_check)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "查看稿件",
                        style = TextStyle(
                            color = MainTextColor,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxHeight().weight(1f).clickableWithoutRipple {
                        // 稿件评分
                        onScoreClick?.invoke()
                    }
                ) {
                    YBImage(
                        modifier = Modifier.size(16.dp),
                        placeholder = painterResource(R.mipmap.ic_archive_score)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "评分",
                        style = TextStyle(
                            color = MainTextColor,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }

            }
        }
    }
}

@Serializable
data class ArchiveListData(
    val title: String = "",
    val niceDate: String = "",
    val link: String = "",
)


@PhonePreview
@Composable
private fun ArchiveListItemPreview() {
    YBTheme {
        val item = ArchiveListData(
            title = "关于云南，你不知道的20个冷知识，带你了解最真实的云南风貌",
            niceDate = "2025-06-07 14:32",
            link = ""
        )
        ArchiveListItem(
            item = item
        )
    }
}
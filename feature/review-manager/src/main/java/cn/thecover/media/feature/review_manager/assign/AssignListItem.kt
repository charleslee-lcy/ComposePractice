package cn.thecover.media.feature.review_manager.assign

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import cn.thecover.media.core.widget.event.clickableWithoutRipple
import cn.thecover.media.core.widget.icon.YBIcons
import cn.thecover.media.core.widget.theme.MainColor
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.PageBackgroundColor
import cn.thecover.media.core.widget.theme.SecondaryTextColor
import cn.thecover.media.core.widget.theme.TertiaryTextColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.core.widget.ui.PhonePreview


/**
 * 部门内分配列表item
 * <p> Created by CharlesLee on 2025/8/7
 * 15708478830@163.com
 */

@Composable
fun AssignListItem(
    modifier: Modifier = Modifier,
    index: Int = 0,
    onAssignClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier.padding(top = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "周国庆$index",
                    style = TextStyle(
                        color = MainTextColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = "ID:1002",
                    style = TextStyle(
                        color = TertiaryTextColor,
                        fontSize = 12.sp
                    ),
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .background(
                            color = Color(0xFFF2F2F2),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickableWithoutRipple {
                        // 分配分数
                        onAssignClick?.invoke()
                    }
                ) {
                    Text(
                        text = "分配分数",
                        style = TextStyle(
                            color = MainColor,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Icon(
                        modifier = Modifier
                            .size(18.dp)
                            .padding(3.dp),
                        painter = painterResource(YBIcons.Custom.RightArrow),
                        contentDescription = null,
                        tint = MainColor
                    )
                }
            }

            DepartmentAnnualAssign()
        }
    }
}

@Composable
fun DepartmentAnnualAssign() {
    LazyVerticalGrid (
        modifier = Modifier.padding(top = 16.dp).height(136.dp),
        columns = GridCells.Fixed(6),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        repeat(12) {
            item {
                Column(
                    modifier = Modifier
                        .height(64.dp)
                        .background(
                            color = PageBackgroundColor,
                            shape = RoundedCornerShape(4.dp)
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${it + 1}月",
                        color = SecondaryTextColor,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "0",
                        color = MainTextColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@PhonePreview
@Composable
private fun AssignListItemPreview() {
    YBTheme {
        AssignListItem()
    }
}
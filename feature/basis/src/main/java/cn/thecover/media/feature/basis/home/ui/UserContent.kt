package cn.thecover.media.feature.basis.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.thecover.media.core.data.HomeInfo
import cn.thecover.media.core.network.BaseUiState
import cn.thecover.media.core.widget.theme.HintTextColor
import cn.thecover.media.core.widget.theme.MainColor
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.MsgColor
import cn.thecover.media.core.widget.theme.SecondaryTextColor
import cn.thecover.media.core.widget.theme.TertiaryTextColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.core.widget.ui.ComponentPreview


/**
 *
 * <p> Created by CharlesLee on 2025/8/1
 * 15708478830@163.com
 */

val normalCardElevation = 0.5.dp

@Composable
internal fun ReporterUserContent(homeInfo: HomeInfo) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = null,
                tint = MainColor,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "个人概览",
                color = MainTextColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 5.dp)
            )
            Text(
                text = "更多信息请前往电脑端查看",
                color = HintTextColor,
                fontSize = 11.sp,
                lineHeight = 11.sp,
                modifier = Modifier.padding(start = 5.dp, bottom = 2.dp)
            )
        }
        Row(
            modifier = Modifier
                .padding(top = 12.dp)
                .height(70.dp)
        ) {
            Card(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .weight(1f)
                    .fillMaxHeight(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = normalCardElevation),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "本月绩效最终得分",
                    color = SecondaryTextColor,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 12.dp, top = 12.dp)
                )
                Text(
                    text = homeInfo.finalScore,
                    color = MainTextColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 12.dp, top = 2.dp, bottom = 12.dp)
                )
            }
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = normalCardElevation),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "本月稿费编辑费",
                    color = SecondaryTextColor,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 12.dp, top = 12.dp)
                )
                Text(
                    text = homeInfo.money,
                    color = MainTextColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 12.dp, top = 2.dp, bottom = 12.dp)
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .height(70.dp)
        ) {
            Card(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .fillMaxHeight()
                    .weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = normalCardElevation),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "定额基数分",
                    color = SecondaryTextColor,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 12.dp, top = 12.dp)
                )
                Text(
                    text = homeInfo.quotaBasicScore,
                    color = MainTextColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 12.dp, top = 2.dp, bottom = 12.dp)
                )
            }
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = normalCardElevation),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "本月考核结果",
                    color = SecondaryTextColor,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 12.dp, top = 12.dp)
                )
                Text(
                    text = if (homeInfo.assessmentResult) "合格" else "不合格",
                    color = MsgColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom
        ) {
            Icon(
                imageVector = Icons.Filled.Task,
                contentDescription = null,
                tint = MainColor,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "任务完成情况",
                color = MainTextColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 5.dp)
            )
            Text(
                text = "更多信息请前往电脑端查看",
                color = HintTextColor,
                fontSize = 11.sp,
                lineHeight = 11.sp,
                modifier = Modifier.padding(start = 5.dp, bottom = 2.dp)
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = normalCardElevation),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(modifier = Modifier.height(70.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "定额任务",
                    color = SecondaryTextColor,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(0.8f)
                )
                VerticalDivider(
                    modifier = Modifier.height(38.dp),
                    thickness = 0.5.dp,
                    color = TertiaryTextColor
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "定额基数分",
                        color = MainTextColor,
                        fontSize = 14.sp,
                    )
                    Text(
                        text = homeInfo.quotaBasicScore,
                        color = MainTextColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "实际完成",
                        color = MainTextColor,
                        fontSize = 14.sp,
                    )
                    Text(
                        text = homeInfo.finalScore,
                        color = MsgColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = normalCardElevation),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(modifier = Modifier.height(70.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "内参任务",
                    color = SecondaryTextColor,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(0.8f)
                )
                VerticalDivider(
                    modifier = Modifier.height(38.dp),
                    thickness = 0.5.dp,
                    color = TertiaryTextColor
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "目标任务",
                        color = MainTextColor,
                        fontSize = 14.sp,
                    )
                    Text(
                        text = "2条",
                        color = MainTextColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "实际完成",
                        color = MainTextColor,
                        fontSize = 14.sp,
                    )
                    Text(
                        text = "1条",
                        color = MsgColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}

@Composable
internal fun LeaderUserContent(homeInfo: HomeInfo) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = null,
                tint = MainColor,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "个人概览",
                color = MainTextColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 5.dp)
            )
            Text(
                text = "更多信息请前往电脑端查看",
                color = HintTextColor,
                fontSize = 11.sp,
                lineHeight = 11.sp,
                modifier = Modifier.padding(start = 5.dp, bottom = 2.dp)
            )
        }
        Row(
            modifier = Modifier
                .padding(top = 12.dp)
                .height(70.dp)
        ) {
            Card(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .weight(1f)
                    .fillMaxHeight(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = normalCardElevation),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "本月部门参加考核人数",
                    color = SecondaryTextColor,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 12.dp, top = 12.dp)
                )
                Text(
                    text = "240",
                    color = MainTextColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 12.dp, top = 2.dp, bottom = 12.dp)
                )
            }
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = normalCardElevation),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "本月部门合格人数",
                    color = SecondaryTextColor,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 12.dp, top = 12.dp)
                )
                Text(
                    text = "240",
                    color = MainTextColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 12.dp, top = 2.dp, bottom = 12.dp)
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .height(70.dp)
        ) {
            Card(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .fillMaxHeight()
                    .weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = normalCardElevation),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "本月合格率",
                    color = SecondaryTextColor,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 12.dp, top = 12.dp)
                )
                Text(
                    text = "100%",
                    color = MainTextColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 12.dp, top = 2.dp, bottom = 12.dp)
                )
            }
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = normalCardElevation),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "本月部门平均分",
                    color = SecondaryTextColor,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 12.dp, top = 12.dp)
                )
                Text(
                    text = "100.5",
                    color = MainTextColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .height(70.dp)
        ) {
            Card(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .fillMaxHeight()
                    .weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = normalCardElevation),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "本月最终系数",
                    color = SecondaryTextColor,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 12.dp, top = 12.dp)
                )
                Text(
                    text = "1.2",
                    color = MainTextColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 12.dp, top = 2.dp, bottom = 12.dp)
                )
            }
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = normalCardElevation),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "本月绩效最终得分",
                    color = SecondaryTextColor,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 12.dp, top = 12.dp)
                )
                Text(
                    text = "1292832",
                    color = MainTextColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .height(70.dp)
        ) {
            Card(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .fillMaxHeight()
                    .weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = normalCardElevation),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "本月稿费编辑费",
                    color = SecondaryTextColor,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 12.dp, top = 12.dp)
                )
                Text(
                    text = "19278.5",
                    color = MainTextColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 12.dp, top = 2.dp, bottom = 12.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom
        ) {
            Icon(
                imageVector = Icons.Filled.Task,
                contentDescription = null,
                tint = MainColor,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "任务完成情况",
                color = MainTextColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 5.dp)
            )
            Text(
                text = "更多信息请前往电脑端查看",
                color = HintTextColor,
                fontSize = 11.sp,
                lineHeight = 11.sp,
                modifier = Modifier.padding(start = 5.dp, bottom = 2.dp)
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = normalCardElevation),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(modifier = Modifier.height(70.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "定额任务",
                    color = SecondaryTextColor,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(0.8f)
                )
                VerticalDivider(
                    modifier = Modifier.height(38.dp),
                    thickness = 0.5.dp,
                    color = TertiaryTextColor
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "定额系数",
                        color = MainTextColor,
                        fontSize = 14.sp,
                    )
                    Text(
                        text = "140",
                        color = MainTextColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "实际系数",
                        color = MainTextColor,
                        fontSize = 14.sp,
                    )
                    Text(
                        text = "75",
                        color = MsgColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = normalCardElevation),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(modifier = Modifier.height(70.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "内参任务",
                    color = SecondaryTextColor,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(0.8f)
                )
                VerticalDivider(
                    modifier = Modifier.height(38.dp),
                    thickness = 0.5.dp,
                    color = TertiaryTextColor
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "目标任务",
                        color = MainTextColor,
                        fontSize = 14.sp,
                    )
                    Text(
                        text = "2条",
                        color = MainTextColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "实际完成",
                        color = MainTextColor,
                        fontSize = 14.sp,
                    )
                    Text(
                        text = "1条",
                        color = MsgColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = normalCardElevation),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(modifier = Modifier.height(70.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "客户端\n" +
                            "“拉新”任务",
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    color = SecondaryTextColor,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(0.8f)
                )
                VerticalDivider(
                    modifier = Modifier.height(38.dp),
                    thickness = 0.5.dp,
                    color = TertiaryTextColor
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "目标任务",
                        color = MainTextColor,
                        fontSize = 14.sp,
                    )
                    Text(
                        text = "2场",
                        color = MainTextColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "实际完成",
                        color = MainTextColor,
                        fontSize = 14.sp,
                    )
                    Text(
                        text = "1场",
                        color = MsgColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}

@ComponentPreview
@Composable
private fun ReporterUserContentPreview() {
    YBTheme {
        ReporterUserContent(HomeInfo())
    }
}

@ComponentPreview
@Composable
private fun LeaderUserContentPreview() {
    YBTheme {
        LeaderUserContent(HomeInfo())
    }
}
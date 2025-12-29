package cn.thecover.media.feature.basis.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.thecover.media.core.data.HomeInfo
import cn.thecover.media.core.widget.theme.HintTextColor
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.MsgColor
import cn.thecover.media.core.widget.theme.SecondaryTextColor
import cn.thecover.media.core.widget.theme.TertiaryTextColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.core.widget.ui.ComponentPreview
import java.math.BigDecimal


/**
 *
 * <p> Created by CharlesLee on 2025/8/1
 * 15708478830@163.com
 */

val normalCardElevation = 0.5.dp

// 格式化分数，确保整数不显示小数点
fun formatScore(score: String): String {
    return try {
        BigDecimal(score).stripTrailingZeros().toPlainString()
    } catch (e: NumberFormatException) {
        score
    }
}

@Composable
internal fun ReporterUserContent(homeInfo: HomeInfo) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(cn.thecover.media.feature.basis.R.mipmap.icon_home_personal),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(16.dp)
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
                modifier = Modifier.padding(start = 5.dp)
            )
        }

        if (homeInfo.status == 1) {
            // 当status为1时显示"当前功能未开启"
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = homeInfo.statusInfo,
                    color = MainTextColor,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 20.dp)
                )
            }
        } else {
            // 原有的个人概览内容
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                // 第一行卡片
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Card(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .weight(1f),
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
                            text = formatScore(homeInfo.finalScore),
                            color = MainTextColor,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(start = 12.dp, top = 2.dp, bottom = 12.dp)
                        )
                    }

                    if (homeInfo.showMoney) {
                        Card(
                            modifier = Modifier
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
                                text = formatScore(homeInfo.money),
                                color = MainTextColor,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(
                                    start = 12.dp,
                                    top = 2.dp,
                                    bottom = 12.dp
                                )
                            )
                        }
                    } else {
                        // 当不显示稿费时，显示定额基数分
                        Card(
                            modifier = Modifier
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
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(
                                    start = 12.dp,
                                    top = 2.dp,
                                    bottom = 12.dp
                                )
                            )
                        }
                    }
                }

                // 第二行卡片
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    if (homeInfo.showMoney) {
                        // 当显示稿费时，显示定额基数分和本月考核结果
                        Card(
                            modifier = Modifier
                                .padding(end = 8.dp)
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
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(
                                    start = 12.dp,
                                    top = 2.dp,
                                    bottom = 12.dp
                                )
                            )
                        }

                        Card(
                            modifier = Modifier
                                .weight(1f),
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
                                text = when (homeInfo.assessmentResult) {
                                    null -> "本月不考核"
                                    true -> "合格"
                                    else -> "不合格"
                                },
                                color = if (homeInfo.assessmentResult == null || homeInfo.assessmentResult == true) MainTextColor else MsgColor,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(
                                    start = 12.dp,
                                    top = 2.dp,
                                    bottom = 12.dp
                                )
                            )
                        }
                    } else {
                        // 当不显示稿费时，只显示考核结果
                        Card(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .weight(1f),
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
                                text = when (homeInfo.assessmentResult) {
                                    null -> "本月不考核"
                                    true -> "合格"
                                    else -> "不合格"
                                },
                                color = if (homeInfo.assessmentResult == null || homeInfo.assessmentResult == true) MainTextColor else MsgColor,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(
                                    start = 12.dp,
                                    top = 2.dp,
                                    bottom = 12.dp
                                )
                            )
                        }
                        // 右侧添加一个空白区域来保持宽度一致
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(cn.thecover.media.feature.basis.R.mipmap.icon_home_task),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(16.dp)
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
                modifier = Modifier.padding(start = 5.dp)
            )
        }

        if (homeInfo.status == 1) {
            // 当status为1时显示"当前功能未开启"
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = homeInfo.statusInfo,
                    color = MainTextColor,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 20.dp)
                )
            }
        } else {
            // 原有的任务完成情况内容
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = normalCardElevation),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.height(70.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                            text = if (homeInfo.layoutScore + homeInfo.newsScore > 0) formatScore((homeInfo.layoutScore + homeInfo.newsScore).toString()) else "0",
                            color = if (homeInfo.layoutScore + homeInfo.newsScore > 0) {
                                // 检查是否完成了定额任务（定额基数分 > 0）
                                if (homeInfo.quotaBasicScore.isNotEmpty() && homeInfo.quotaBasicScore != "0") {
                                    val quotaBasicScore =
                                        homeInfo.quotaBasicScore.toDoubleOrNull() ?: 0.0
                                    // 实际完成数大于等于定额基数分时不标红，小于时标红
                                    if ((homeInfo.layoutScore + homeInfo.newsScore) >= quotaBasicScore) MainTextColor else MsgColor
                                } else {
                                    MainTextColor
                                }
                            } else MsgColor,
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
                Row(
                    modifier = Modifier.height(70.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                    // 判断目标任务是否为0
                    val isNoInnerTask =
                        homeInfo.innerTaskGoalNum.isEmpty() || homeInfo.innerTaskGoalNum == "0"

                    if (isNoInnerTask || !homeInfo.intraAssess) {
                        // 无任务时显示单列
                        Column(
                            modifier = Modifier.weight(2f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (!homeInfo.intraAssess) "不参与考核" else "无任务",
                                color = MainTextColor,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    } else {
                        // 有任务时显示目标任务和实际完成两列
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
                                text = "${homeInfo.innerTaskGoalNum}条",
                                color = MainTextColor,
                                fontSize = 20.sp,
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
                                text = if (homeInfo.innerTaskFinishedNum.isNotEmpty()) "${homeInfo.innerTaskFinishedNum}条" else "0",
                                color = {
                                    if (homeInfo.innerTaskFinishedNum.isEmpty()) {
                                        MsgColor // 未完成
                                    } else {
                                        // 比较实际完成数和目标任务数
                                        val finishedNum =
                                            homeInfo.innerTaskFinishedNum.toDoubleOrNull() ?: 0.0
                                        val goalNum =
                                            homeInfo.innerTaskGoalNum.toDoubleOrNull() ?: 0.0
                                        // 实际完成数大于等于目标任务数时不标红，小于时标红
                                        if (finishedNum >= goalNum) MainTextColor else MsgColor
                                    }
                                }(),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
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
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(cn.thecover.media.feature.basis.R.mipmap.icon_home_personal),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(16.dp)
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
                modifier = Modifier.padding(start = 5.dp)
            )
        }

        if (homeInfo.status == 1) {
            // 当status为1时显示"当前功能未开启"
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = homeInfo.statusInfo,
                    color = MainTextColor,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 20.dp)
                )
            }
        } else {
            // 原有的个人概览内容
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                // 第一行卡片
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Card(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .weight(1f),
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
                            text = homeInfo.participateAssessmentCount,
                            color = MainTextColor,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(start = 12.dp, top = 2.dp, bottom = 12.dp)
                        )
                    }

                    Card(
                        modifier = Modifier
                            .weight(1f),
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
                            text = homeInfo.passCount,
                            color = MainTextColor,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(start = 12.dp, top = 2.dp, bottom = 12.dp)
                        )
                    }
                }

                // 第二行卡片
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .padding(end = 8.dp)
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
                            text = if (homeInfo.passRate == "0") "0%" else "${formatScore(homeInfo.passRate)}%",
                            color = MainTextColor,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(start = 12.dp, top = 2.dp, bottom = 12.dp)
                        )
                    }

                    Card(
                        modifier = Modifier
                            .weight(1f),
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
                            text = if (homeInfo.deptAverageScore == "0") "0" else formatScore(
                                homeInfo.deptAverageScore
                            ),
                            color = MainTextColor,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(start = 12.dp, top = 2.dp, bottom = 12.dp)
                        )
                    }
                }

                // 第三行卡片
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .padding(end = 8.dp)
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
                            text = if (homeInfo.finalCoefficient == "0") "0" else formatScore(
                                homeInfo.finalCoefficient
                            ),
                            color = MainTextColor,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(start = 12.dp, top = 2.dp, bottom = 12.dp)
                        )
                    }

                    if (homeInfo.showMoney) {
                        Card(
                            modifier = Modifier
                                .weight(1f),
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
                                text = formatScore(homeInfo.finalScore),
                                color = MainTextColor,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(
                                    start = 12.dp,
                                    top = 2.dp,
                                    bottom = 12.dp
                                )
                            )
                        }
                    } else {
                        // 当不显示稿费时，显示本月绩效最终得分
                        Card(
                            modifier = Modifier
                                .weight(1f),
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
                                text = formatScore(homeInfo.finalScore),
                                color = MainTextColor,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(
                                    start = 12.dp,
                                    top = 2.dp,
                                    bottom = 12.dp
                                )
                            )
                        }
                    }
                }

                // 第四行卡片
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    if (homeInfo.showMoney) {
                        Card(
                            modifier = Modifier
                                .padding(end = 8.dp)
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
                                text = formatScore(homeInfo.money),
                                color = MainTextColor,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(
                                    start = 12.dp,
                                    top = 2.dp,
                                    bottom = 12.dp
                                )
                            )
                        }
                        // 右侧添加一个空白区域来保持宽度一致
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(cn.thecover.media.feature.basis.R.mipmap.icon_home_personal),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(16.dp)
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
                modifier = Modifier.padding(start = 5.dp)
            )
        }

        if (homeInfo.status == 1) {
            // 当status为1时显示"当前功能未开启"
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = homeInfo.statusInfo,
                    color = MainTextColor,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 20.dp)
                )
            }
        } else {
            // 原有的任务完成情况内容
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = normalCardElevation),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.height(70.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                            text = if (homeInfo.quotaCoefficient == "0") "0" else formatScore(
                                homeInfo.quotaCoefficient
                            ),
                            color = MainTextColor,
                            fontSize = 20.sp,
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
                            text = if (homeInfo.finalCoefficient == "0") "0" else formatScore(
                                homeInfo.finalCoefficient
                            ),
                            color = if (homeInfo.finalCoefficient == "0" || homeInfo.finalCoefficient.toDouble() >= homeInfo.quotaCoefficient.toDouble()) MainTextColor else MsgColor,
                            fontSize = 20.sp,
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
                Row(
                    modifier = Modifier.height(70.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                    // 判断目标任务是否为0
                    val isNoInnerTask =
                        homeInfo.innerTaskGoalNum.isEmpty() || homeInfo.innerTaskGoalNum == "0"

                    if (isNoInnerTask || !homeInfo.intraAssess) {
                        // 无任务时显示单列
                        Column(
                            modifier = Modifier.weight(2f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (!homeInfo.intraAssess) "不参与考核" else "无任务",
                                color = MainTextColor,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    } else {
                        // 有任务时显示目标任务和实际完成两列
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
                                text = "${homeInfo.innerTaskGoalNum}条",
                                color = MainTextColor,
                                fontSize = 20.sp,
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
                                text = if (homeInfo.innerTaskFinishedNum.isNotEmpty()) "${homeInfo.innerTaskFinishedNum}条" else "0",
                                color = {
                                    if (homeInfo.innerTaskFinishedNum.isEmpty()) {
                                        MsgColor // 未完成
                                    } else {
                                        // 比较实际完成数和目标任务数
                                        val finishedNum =
                                            homeInfo.innerTaskFinishedNum.toDoubleOrNull() ?: 0.0
                                        val goalNum =
                                            homeInfo.innerTaskGoalNum.toDoubleOrNull() ?: 0.0
                                        // 实际完成数大于等于目标任务数时不标红，小于时标红
                                        if (finishedNum >= goalNum) MainTextColor else MsgColor
                                    }
                                }(),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
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
                Row(
                    modifier = Modifier.height(70.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "客户端\n" +
                                "“拉新”任务",
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp,
                        color = SecondaryTextColor,
                        fontSize = 14.sp,
                        modifier = Modifier.weight(0.8f)
                    )
                    VerticalDivider(
                        modifier = Modifier.height(38.dp),
                        thickness = 0.5.dp,
                        color = TertiaryTextColor
                    )
                    // 客户端拉新任务目标任务始终为0，直接显示"无任务"
                    Column(
                        modifier = Modifier.weight(2f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "无任务",
                            color = MainTextColor,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
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

@Composable
internal fun ReviewerUserContent(homeInfo: HomeInfo) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(cn.thecover.media.feature.basis.R.mipmap.icon_home_personal),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(16.dp)
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
                modifier = Modifier.padding(start = 5.dp)
            )
        }

        if (homeInfo.status == 1) {
            // 当status为1时显示"当前功能未开启"
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = homeInfo.statusInfo,
                    color = MainTextColor,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 20.dp)
                )
            }
        } else {
            // 原有的个人概览内容
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                // 第一行卡片
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Card(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .weight(1f),
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
                            text = formatScore(homeInfo.finalScore),
                            color = MainTextColor,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(start = 12.dp, top = 2.dp, bottom = 12.dp)
                        )
                    }

                    if (homeInfo.showMoney) {
                        Card(
                            modifier = Modifier
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
                                text = formatScore(homeInfo.money),
                                color = MainTextColor,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(
                                    start = 12.dp,
                                    top = 2.dp,
                                    bottom = 12.dp
                                )
                            )
                        }
                    } else {
                        // 当不显示稿费时，显示本月分配得分
                        Card(
                            modifier = Modifier
                                .weight(1f),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = normalCardElevation),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "本月分配得分",
                                color = SecondaryTextColor,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(start = 12.dp, top = 12.dp)
                            )
                            Text(
                                text = formatScore(homeInfo.verifierDistributeScore),
                                color = MainTextColor,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(
                                    start = 12.dp,
                                    top = 2.dp,
                                    bottom = 12.dp
                                )
                            )
                        }
                    }
                }

                // 第二行卡片
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    if (homeInfo.showMoney) {
                        // 当显示稿费时，显示本月分配得分和本月最终系数
                        Card(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .weight(1f),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = normalCardElevation),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "本月分配得分",
                                color = SecondaryTextColor,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(start = 12.dp, top = 12.dp)
                            )
                            Text(
                                text = formatScore(homeInfo.verifierDistributeScore),
                                color = MainTextColor,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(
                                    start = 12.dp,
                                    top = 2.dp,
                                    bottom = 12.dp
                                )
                            )
                        }

                        Card(
                            modifier = Modifier
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
                                text = if (homeInfo.finalCoefficient == "0") "0" else formatScore(
                                    homeInfo.finalCoefficient
                                ),
                                color = MainTextColor,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(
                                    start = 12.dp,
                                    top = 2.dp,
                                    bottom = 12.dp
                                )
                            )
                        }
                    } else {
                        // 当不显示稿费时，只显示本月最终系数
                        Card(
                            modifier = Modifier
                                .padding(end = 8.dp)
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
                                text = if (homeInfo.finalCoefficient == "0") "0" else formatScore(
                                    homeInfo.finalCoefficient
                                ),
                                color = MainTextColor,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(
                                    start = 12.dp,
                                    top = 2.dp,
                                    bottom = 12.dp
                                )
                            )
                        }
                        // 右侧添加空白区域保持宽度一致
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(cn.thecover.media.feature.basis.R.mipmap.icon_home_task),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(16.dp)
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
                modifier = Modifier.padding(start = 5.dp)
            )
        }

        if (homeInfo.status == 1) {
            // 当status为1时显示"当前功能未开启"
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = homeInfo.statusInfo,
                    color = MainTextColor,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 20.dp)
                )
            }
        } else {
            // 原有的任务完成情况内容
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = normalCardElevation),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.height(70.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                            text = if (homeInfo.quotaCoefficient == "0") "0" else formatScore(
                                homeInfo.quotaCoefficient
                            ),
                            color = MainTextColor,
                            fontSize = 20.sp,
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
                            text = if (homeInfo.finalCoefficient == "0") "0" else formatScore(
                                homeInfo.finalCoefficient
                            ),
                            color = if (homeInfo.finalCoefficient == "0" || homeInfo.finalCoefficient.toDouble() >= homeInfo.quotaCoefficient.toDouble()) MainTextColor else MsgColor,
                            fontSize = 20.sp,
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
                Row(
                    modifier = Modifier.height(70.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                    // 判断目标任务是否为0
                    val isNoInnerTask =
                        homeInfo.innerTaskGoalNum.isEmpty() || homeInfo.innerTaskGoalNum == "0"

                    if (isNoInnerTask || !homeInfo.intraAssess) {
                        // 无任务时显示单列
                        Column(
                            modifier = Modifier.weight(2f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (!homeInfo.intraAssess) "不参与考核" else "无任务",
                                color = MainTextColor,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    } else {
                        // 有任务时显示目标任务和实际完成两列
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
                                text = "${homeInfo.innerTaskGoalNum}条",
                                color = MainTextColor,
                                fontSize = 20.sp,
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
                                text = if (homeInfo.innerTaskFinishedNum.isNotEmpty()) "${homeInfo.innerTaskFinishedNum}条" else "0",
                                color = {
                                    if (homeInfo.innerTaskFinishedNum.isEmpty()) {
                                        MsgColor // 未完成
                                    } else {
                                        // 比较实际完成数和目标任务数
                                        val finishedNum =
                                            homeInfo.innerTaskFinishedNum.toDoubleOrNull() ?: 0.0
                                        val goalNum =
                                            homeInfo.innerTaskGoalNum.toDoubleOrNull() ?: 0.0
                                        // 实际完成数大于等于目标任务数时不标红，小于时标红
                                        if (finishedNum >= goalNum) MainTextColor else MsgColor
                                    }
                                }(),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }
            }
        }
    }
}

@ComponentPreview
@Composable
private fun ReviewerUserContentPreview() {
    YBTheme {
        ReviewerUserContent(HomeInfo())
    }
}

@ComponentPreview
@Composable
private fun LeaderUserContentPreview() {
    YBTheme {
        LeaderUserContent(HomeInfo())
    }
}
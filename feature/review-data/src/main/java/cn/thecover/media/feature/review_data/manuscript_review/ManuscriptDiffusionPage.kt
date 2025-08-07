package cn.thecover.media.feature.review_data.manuscript_review

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastCoerceAtLeast
import cn.thecover.media.core.widget.R
import cn.thecover.media.core.widget.component.ItemScoreRow
import cn.thecover.media.core.widget.component.picker.DateType
import cn.thecover.media.core.widget.component.picker.YBDatePicker
import cn.thecover.media.core.widget.component.popup.YBAlignDropdownMenu
import cn.thecover.media.core.widget.event.clickableWithoutRipple
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.TertiaryTextColor
import cn.thecover.media.core.widget.theme.YBShapes
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.feature.review_data.basic_widget.widget.DataItemCard
import cn.thecover.media.feature.review_data.basic_widget.widget.DataItemDropMenuView
import cn.thecover.media.feature.review_data.basic_widget.widget.DataItemRankingRow
import cn.thecover.media.feature.review_data.basic_widget.widget.DataItemSelectionView
import cn.thecover.media.feature.review_data.basic_widget.widget.ExpandItemColumn
import cn.thecover.media.feature.review_data.basic_widget.widget.ManuScriptItemHeader
import cn.thecover.media.feature.review_data.data.DiffusionDataEntity
import cn.thecover.media.feature.review_data.data.ManuscriptReviewDataEntity
import java.time.LocalDate

/**
 *  Created by Wing at 10:27 on 2025/8/6
 *  稿件传播效果页面
 */

/**
 * 稿件传播数据展示页面的 Composable 函数。
 *
 * 该页面用于展示多个稿件的传播相关数据，包括稿件基本信息、评分以及传播详情。
 * 页面结构包含一个头部组件、记录总数提示文本以及一个列表项组件用于展示每条稿件数据。
 */
@Composable
fun ManuscriptDiffusionPage() {

    // 模拟稿件传播数据列表
    val data = listOf(
        ManuscriptReviewDataEntity(
            title = "《三体》",
            author = "刘慈欣",
            editor = "王伟",
            score = 4,
            basicScore = 3,
            qualityScore = 4,
            diffusionScore = 5,
            diffusionDataEntity = DiffusionDataEntity(
                28888, 288888, 222, 222, 222, 222, 222, 222, 222
            )
        ),
        ManuscriptReviewDataEntity(
            title = "2025年12月份的云南省让“看一种云南生活”富饶世界云南生活富饶世界",
            author = "张明明",
            editor = "李华",
            score = 22,
            basicScore = 3,
            qualityScore = 4,
            diffusionScore = 5,
            diffusionDataEntity = DiffusionDataEntity(
                28888, 288888, 222, 222, 222, 222, 222, 222, 222
            )
        )
    )

    // 使用 LazyColumn 垂直排列页面内容，item 之间间隔 12.dp
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(horizontal = 12.dp)
    ) {
        // 显示页面头部组件
        item {
            ManuscriptDiffusionHeader()
        }

        // 显示记录总数提示文本，其中数字部分使用主色调高亮显示
        item {
            Text(
                text = buildAnnotatedString {
                    append("共 ")
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append(data.size.toString())
                    }
                    append(" 条记录")
                },
                style = MaterialTheme.typography.labelMedium,
                color = TertiaryTextColor,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .offset(y = (-4).dp)
            )
        }

        // 遍历数据列表，为每条数据渲染 DiffusionItem 组件
        items(data.size) {
            DiffusionItem(it + 1, data[it])
        }
    }
}


/**
 * 用于展示稿件传播数据的UI组件。主要功能包括：
 * 显示稿件的基本信息（标题、作者、编辑）
 * 展示传播评分数据（公式传播分、最终传播分）
 * 折叠显示详细的转载数据（核心媒体、一级媒体、二级媒体转载数）
 * 展示用户互动数据（阅读数、分享数、点赞数、评论数）
 *
 * @param rank 稿件在列表中的排名序号，用于展示排名信息
 * @param data 稿件的详细传播数据实体，包含基本信息和各项统计数据
 */
@Composable
private fun DiffusionItem(rank: Int, data: ManuscriptReviewDataEntity) {
    // 使用排名卡片包装整个内容区域
    DataItemCard {
        DataItemRankingRow(ranking = rank) {
            // 可折叠的内容区域，包含基础信息和详细数据
            ExpandItemColumn(offset = -12, content = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // 显示稿件头部信息：标题、作者、编辑
                    ManuScriptItemHeader(
                        title = data.title,
                        author = data.author,
                        editor = data.editor
                    )
                    // 显示传播评分数据行：公式传播分和最终传播分
                    ItemScoreRow(
                        items = arrayOf(
                            Pair(
                                "公式传播分",
                                data.diffusionDataEntity.basicDiffusionScore.toString()
                            ),
                            Pair(
                                "最终传播分",
                                data.diffusionDataEntity.ultimateDiffusionScore.toString()
                            ),
                        )
                    )
                }
            }, foldContent = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // 显示转载数据：核心媒体、一级媒体、二级媒体转载数
                    ItemScoreRow(
                        items = arrayOf(
                            Pair(
                                "核心媒体转载数",
                                data.diffusionDataEntity.coreMediaReprint.toString()
                            ),
                            Pair(
                                "一级媒体转载数",
                                data.diffusionDataEntity.level1MediaReprint.toString()
                            ),
                            Pair(
                                "二级媒体转载数",
                                data.diffusionDataEntity.level2MediaReprint.toString()
                            ),
                        )
                    )
                    // 显示用户互动数据：阅读数、分享数、点赞数、评论数
                    ItemScoreRow(
                        items = arrayOf(
                            Pair("阅读数", data.diffusionDataEntity.readNumber.toString()),
                            Pair("分享数", data.diffusionDataEntity.shareNumber.toString()),
                            Pair("点赞数", data.diffusionDataEntity.thumbNumber.toString()),
                            Pair("评论数", data.diffusionDataEntity.commentNumber.toString()),
                        )
                    )
                }

            })
        }
    }
}


/**
 * 稿件传播数据筛选头部组件。
 *
 * 该 Composable 函数用于展示和操作稿件传播数据的筛选条件，包括排序方式、时间范围和关键词搜索。
 * 用户可以通过下拉菜单选择排序指数，通过日期选择器选择月份，并通过搜索框输入关键词进行过滤。
 *
 * 注意：此函数为私有函数，仅在当前文件内使用。
 */
@Composable
private fun ManuscriptDiffusionHeader() {
    // 获取当前日期并格式化为“年月”文本，作为默认显示的时间
    val currentDate = LocalDate.now()
    val currentMonthText = "${currentDate.year}年${currentDate.monthValue}月"

    // 控制日期选择器弹窗的显示状态
    var showDatePicker by remember { mutableStateOf(false) }
    // 存储用户选择的日期文本
    var datePickedText by remember { mutableStateOf(currentMonthText) }
    // 存储用户选择的排序字段
    val selectFilterChoice = remember { mutableStateOf("一级媒体转载数") }
    // 存储用户选择的搜索字段
    val selectSearchChoice = remember { mutableStateOf("稿件标题") }

    // 主体内容卡片容器
    DataItemCard {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // 第一行：排序指数选择 和 时间选择
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 排序字段选择区域
                Column(modifier = Modifier.weight(1f)) {
                    Text("排序指数", style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.height(8.dp))
                    DataItemDropMenuView(
                        data = selectFilterChoice, dataList = listOf(
                            "公式传播分",
                            "最终传播分",
                            "核心媒体转载数",
                            "一级媒体转载数",
                            "二级媒体转载数",
                            "阅读数",
                            "分享数",
                            "点赞数",
                            "评论数",
                        )
                    )
                }
                Spacer(Modifier.width(12.dp))

                // 时间选择区域
                Column(modifier = Modifier.weight(1f)) {
                    Text("时间", style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.height(8.dp))
                    DataItemSelectionView(label = datePickedText, onClick = {
                        showDatePicker = true
                    })
                }
            }

            // 搜索输入区域
            FilterSearchView(data = selectSearchChoice, label = "请输入搜索内容")
        }
    }

    // 日期选择器弹窗组件
    YBDatePicker(
        visible = showDatePicker,
        type = DateType.MONTH,
        onCancel = { showDatePicker = false },
        onChange = {
            datePickedText = "${it.year}年${it.monthValue}月"
        }
    )
}


/**
 * 筛选搜索组合项，包含一个下拉筛选菜单和一个文本输入框。
 *
 * @param dataList 下拉菜单的数据列表，默认为 ["稿件标题", "稿件作者", "稿件编辑"]
 * @param data 当前选中的下拉菜单项，使用 MutableState 包装以便响应式更新
 * @param label 输入框的提示文本，默认显示当前选中的下拉项文本
 */
@Composable
private fun FilterSearchView(
    dataList: List<String> = listOf(
        "稿件标题", "稿件作者", "稿件编辑"
    ), data: MutableState<String>, label: String = data.value
) {
    // 控制下拉菜单是否展开的状态
    val showDrop = remember { mutableStateOf(false) }
    // 动画旋转值，用于下拉箭头图标旋转效果
    val animRotate = remember { Animatable(0f) }
    // 文本输入框的内容状态
    val textState = remember { mutableStateOf("") }

    // 当菜单展开状态改变时，触发动画旋转箭头图标
    LaunchedEffect(showDrop.value) {
        animRotate.animateTo(
            targetValue = if (showDrop.value) 180f else 0f,
            animationSpec = tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing
            )
        )
    }

    // 主容器布局：包含下拉菜单和输入框
    Row(
        modifier = Modifier
            .padding(bottom = 2.dp)
            .border(
                width = 0.5.dp,
                shape = MaterialTheme.shapes.extraSmall,
                color = MaterialTheme.colorScheme.outlineVariant
            )
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.background,
                shape = YBShapes.extraSmall
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 下拉菜单组件，用于选择筛选类型
        YBAlignDropdownMenu(
            data = dataList,
            expanded = showDrop,
            initialIndex = dataList.indexOf(data.value).fastCoerceAtLeast(0),
            isItemWidthAlign = true,
            offset = DpOffset(0.dp, 0.dp),
            onItemClick = { text, index ->
                data.value = text
            }
        ) {
            // 触发下拉菜单展开的点击区域
            Row(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.background,
                        shape = YBShapes.extraSmall
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .clickableWithoutRipple {
                        showDrop.value = !showDrop.value
                    },
                verticalAlignment = Alignment.CenterVertically,

                ) {
                // 显示当前选中的筛选项文本
                Text(
                    data.value,
                    style = MaterialTheme.typography.labelMedium,
                    color = MainTextColor
                )
                Spacer(Modifier.width(4.dp))
                // 下拉箭头图标，根据菜单状态旋转
                Icon(
                    painterResource(R.mipmap.ic_arrow_down),
                    contentDescription = "${label}下拉筛选按钮",
                    tint = TertiaryTextColor,
                    modifier = Modifier.rotate(animRotate.value)
                )

            }

        }

        // 分割线，分隔下拉菜单和输入框
        VerticalDivider(
            modifier = Modifier
                .padding(end = 12.dp)
                .width(0.5.dp)
                .height(20.dp),
            color = MaterialTheme.colorScheme.outline,
        )

        // 文本输入框，用于输入搜索关键词
        BasicTextField(
            value = textState.value,
            onValueChange = { textState.value = it },
            modifier = Modifier.fillMaxWidth(),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Box(modifier = Modifier.weight(1f)) {
                            // 当输入框为空时显示提示文本
                            if (textState.value.isEmpty()) {
                                Text(
                                    label,
                                    color = TertiaryTextColor,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                            // 实际的输入框内容
                            innerTextField()
                        }

                    }
                }
            }
        )
    }

}


@Composable
@Preview
private fun ManuscriptDiffusionHeaderPreview() {
    YBTheme {
        ManuscriptDiffusionPage()
    }

}


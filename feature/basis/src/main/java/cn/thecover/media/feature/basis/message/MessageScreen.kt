package cn.thecover.media.feature.basis.message

import android.R.attr.label
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import cn.thecover.media.core.network.previewRetrofit
import cn.thecover.media.core.widget.R
import cn.thecover.media.core.widget.component.YBNormalList
import cn.thecover.media.core.widget.component.YBTopAppBar
import cn.thecover.media.core.widget.component.popup.YBAlignDropdownMenu
import cn.thecover.media.core.widget.event.clickableWithoutRipple
import cn.thecover.media.core.widget.icon.YBIcons
import cn.thecover.media.core.widget.theme.CardOutlineColor
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.MsgColor
import cn.thecover.media.core.widget.theme.TertiaryTextColor
import cn.thecover.media.core.widget.theme.YBShapes
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.feature.basis.message.data.entity.MessageDataEntity
import cn.thecover.media.feature.basis.message.intent.MessageIntent
import cn.thecover.media.feature.basis.mine.MineViewModel

/**
 *  Created by Wing at 09:33 on 2025/8/8
 *  消息通知页面
 */


@Composable
fun MessageRoute(
    viewModel: MineViewModel = hiltViewModel(),
    onPopBack: () -> Unit
) {
    MessageScreen(viewModel, onPopBack)
}


@Composable
fun MessageScreen(
    viewModel: MineViewModel,
    onPopBack: () -> Unit = {}
) {
    // 控制详情页面显示状态
    val showDetail = remember { mutableStateOf(false) }
    val selectedMessage = remember { mutableStateOf<MessageDataEntity?>(null) }

    val messageTypeList = MessageType.entries.map { it.typeName }
    val searchType = remember { mutableStateOf(messageTypeList.first()) }

    val localList = remember { mutableStateOf(emptyList<MessageDataEntity>()) }
    val refresh = remember { mutableStateOf(false) }
    val loadMore = remember { mutableStateOf(false) }
    // 控制下拉菜单是否展开的状态
    val showDrop = remember { mutableStateOf(false) }
    val animRotate = remember { Animatable(0f) }
    val canLoadMore = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.handleMessageIntent(MessageIntent.FetchMessageList(false))
    }

    val messageListState by viewModel.messageListState.collectAsState()
    
    LaunchedEffect(messageListState) {
        localList.value = messageListState.messageDataList
        refresh.value = messageListState.isRefreshing
        loadMore.value = messageListState.isLoading
        canLoadMore.value = messageListState.canLoadMore
    }
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column {
            YBTopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 16.dp),
                title = "消息通知",
                navigationIcon = {
                    Icon(
                        painter = painterResource(YBIcons.Custom.BackArrow),
                        contentDescription = "返回",
                        modifier = Modifier.clickable {
                            onPopBack()
                        }
                    )
                })

            YBNormalList(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                items = localList,
                isRefreshing = refresh,
                isLoadingMore = loadMore,
                canLoadMore = canLoadMore,
                onRefresh = {
                    viewModel.handleMessageIntent(MessageIntent.FetchMessageList(false))
                },
                onLoadMore = { viewModel.handleMessageIntent(MessageIntent.FetchMessageList(true)) },
                header = {
                    YBAlignDropdownMenu(
                        data = messageTypeList,
                        backgroundColor = MaterialTheme.colorScheme.surface,
                        expanded = showDrop,
                        modifier = Modifier.fillMaxWidth(),
                        initialIndex = 0,
                        isItemWidthAlign = true,
                        offset = DpOffset(0.dp, 0.dp),
                        onItemClick = { text, index ->
                            searchType.value = text
                            viewModel.handleMessageIntent(MessageIntent.UpdateMessageFilter(index))
                        }
                    ) {
                        // 触发下拉菜单展开的点击区域
                        Row(
                            modifier = Modifier
                                .border(
                                    width = 0.5.dp,
                                    color = CardOutlineColor,
                                    shape = MaterialTheme.shapes.small
                                )
                                .fillMaxWidth()
                                .background(
                                    MaterialTheme.colorScheme.surface,
                                    shape = YBShapes.small
                                )
                                .padding(horizontal = 12.dp)
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .clickableWithoutRipple {
                                    showDrop.value = !showDrop.value
                                },
                            verticalAlignment = Alignment.CenterVertically,

                            ) {
                            // 显示当前选中的筛选项文本
                            Text(
                                searchType.value,
                                style = MaterialTheme.typography.labelMedium,
                                color = MainTextColor
                            )
                            Spacer(Modifier.weight(1f))
                            // 下拉箭头图标，根据菜单状态旋转
                            Icon(
                                painterResource(R.mipmap.ic_arrow_down),
                                contentDescription = "${label}下拉筛选按钮",
                                tint = TertiaryTextColor,
                                modifier = Modifier.rotate(animRotate.value)
                            )

                        }
                    }
                }
            ) { item, _ ->
                MessageItem(
                    title = item.content ?: "",
                    time = item.createTime ?: "",
                    isRead = item.read,
                    msgType = MessageType.entries.first { it.ordinal == item.type }) {
                    // 设置选中的消息并显示详情页面
                    selectedMessage.value = item
                    showDetail.value = true
                    if (!item.read) {
                        viewModel.handleMessageIntent(MessageIntent.ReadMessage(item.id))
                    }
                }
            }
        }

        // 显示消息详情页面（覆盖在列表上方）
        if (showDetail.value && selectedMessage.value != null) {
            MessageDetailScreen(
                viewModel = viewModel,
                message = selectedMessage.value!!,
                onPopBack = {
                    showDetail.value = false
                    selectedMessage.value = null
                }
            )
        }
    }
}

@Composable
private fun MessageItem(
    title: String,
    time: String,
    msgType: MessageType,
    isRead: Boolean,
    callback: () -> Unit
) {
    Card(
        modifier = Modifier
            .border(width = 0.5.dp, color = CardOutlineColor, shape = MaterialTheme.shapes.small)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, shape = YBShapes.small)
            .padding(horizontal = 12.dp)
            .clickable {
                callback()
            },
        shape = YBShapes.small,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
    ) {
        Box {
            Badge(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .align(Alignment.TopEnd),
                containerColor = if (isRead) Color.Transparent else MsgColor,
                contentColor = Color.White
            )
            Column {
                Spacer(modifier = Modifier.height(12.dp))
                Text(title, style = MaterialTheme.typography.titleSmall, color = MainTextColor)
                Spacer(modifier = Modifier.height(8.dp))
                Text(time, style = MaterialTheme.typography.labelMedium, color = TertiaryTextColor)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        msgType.typeName,
                        style = MaterialTheme.typography.labelMedium,
                        color = msgType.textColor(),
                        modifier = Modifier
                            .background(
                                shape = MaterialTheme.shapes.extraSmall,
                                color = msgType.backColor()
                            )
                            .padding(horizontal = 5.dp, vertical = 3.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        "查看详情",
                        style = MaterialTheme.typography.labelMedium,
                        color = TertiaryTextColor
                    )
                    Icon(
                        painter = painterResource(YBIcons.Custom.RightArrow),
                        contentDescription = "查看详情",
                        modifier = Modifier.size(18.dp),
                        tint = TertiaryTextColor
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

enum class MessageType(val typeName: String) {
    ALL("所有消息"),
    SYSTEM("系统消息"),
    URGING("催办消息");

    @Composable
    fun backColor() =
        when (this) {
            SYSTEM -> {
                MaterialTheme.colorScheme.secondaryContainer
            }

            URGING -> {
                MaterialTheme.colorScheme.primaryContainer
            }

            else -> MaterialTheme.colorScheme.tertiaryContainer
        }


    @Composable
    fun textColor() =
        when (this) {
            SYSTEM -> {
                MaterialTheme.colorScheme.onSecondaryContainer
            }

            URGING -> {
                MaterialTheme.colorScheme.onPrimaryContainer
            }

            else -> MaterialTheme.colorScheme.onTertiaryContainer
        }

}


@Composable
@Preview(showBackground = true)
fun MessageScreenPreview() {
    YBTheme {
        MessageScreen(MineViewModel(SavedStateHandle(), retrofit = { previewRetrofit }), {})
    }

}

@Composable
@Preview(showBackground = true)
fun MessageItemPreview() {
    YBTheme {
        MessageItem(
            title = "测试消息标题",
            time = "2025-12-05 10:30",
            msgType = MessageType.SYSTEM,
            isRead = false
        ) {

        }
    }
}
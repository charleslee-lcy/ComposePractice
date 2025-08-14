package cn.thecover.media.feature.basis.message

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import cn.thecover.media.core.widget.component.YBNormalList
import cn.thecover.media.core.widget.component.YBTopAppBar
import cn.thecover.media.core.widget.component.search.FilterSearchTextField
import cn.thecover.media.core.widget.icon.YBIcons
import cn.thecover.media.core.widget.theme.CardOutlineColor
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.TertiaryTextColor
import cn.thecover.media.core.widget.theme.YBShapes
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.feature.basis.message.intent.MessageIntent
import cn.thecover.media.feature.basis.mine.MineViewModel

/**
 *  Created by Wing at 09:33 on 2025/8/8
 *  消息通知页面
 */


@Composable
fun MessageRoute(
    viewModel: MineViewModel = hiltViewModel(),
    routeToDetail: (Long) -> Unit,
    onPopBack: () -> Unit
) {
    MessageScreen(viewModel, routeToDetail, onPopBack)
}


@Composable
fun MessageScreen(
    viewModel: MineViewModel,
    routeToDetail: (Long) -> Unit = {},
    onPopBack: () -> Unit = {}
) {

    val messageTypeList = MessageType.entries.map { it.typeName }
    val searchType = remember { mutableStateOf(messageTypeList.first()) }
    val messageListState by viewModel.messageListState.collectAsState()
    val localList = remember { mutableStateOf(messageListState.messageDataList) }
    val refresh = remember { mutableStateOf(messageListState.isRefreshing) }
    val loadMore = remember { mutableStateOf(messageListState.isLoading) }

    val canLoadMore = remember { mutableStateOf(false) }
    LaunchedEffect(searchType) {
        viewModel.handleMessageIntent(MessageIntent.FetchMessageList(false))
    }

    LaunchedEffect(messageListState) {
        localList.value = messageListState.messageDataList
        refresh.value = messageListState.isRefreshing
        loadMore.value = messageListState.isLoading
    }

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
                FilterSearchTextField(
                    dataList = messageTypeList,
                    data = searchType,
                    label = "请输入搜索内容",
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    onValueChange = { type, searchText ->
                        viewModel.handleMessageIntent(MessageIntent.SearchMessage(type, searchText))
                    }
                )
            }
        ) { item, position ->
            MessageItem(
                title = item.title,
                time = item.time,
                msgType = MessageType.entries.first { it.ordinal == item.type }) {
                routeToDetail(item.messageId)
            }
        }
    }
}

@Composable
private fun MessageItem(title: String, time: String, msgType: MessageType, callback: () -> Unit) {
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
        MessageScreen(MineViewModel(SavedStateHandle()), {})
    }

}
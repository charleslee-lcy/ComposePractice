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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cn.thecover.media.core.widget.component.YBTopAppBar
import cn.thecover.media.core.widget.component.search.FilterSearchTextField
import cn.thecover.media.core.widget.icon.YBIcons
import cn.thecover.media.core.widget.theme.CardOutlineColor
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.TertiaryTextColor
import cn.thecover.media.core.widget.theme.YBShapes
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.feature.basis.home.data.MessageDataEntity

/**
 *  Created by Wing at 09:33 on 2025/8/8
 *  消息通知页面
 */


@Composable
fun MessageRoute(routeToDetail: (Long) -> Unit, onPopBack: () -> Unit) {
    MessageScreen(routeToDetail, onPopBack)
}

val messagetList = listOf(
    MessageDataEntity(
        messageId = 0,
        title = "国家科技重大专项小麦育种新突破",
        time = "2025-08-08 09:09",
        type = 1,
        content = "系统消息"
    ),
    MessageDataEntity(
        messageId = 1,
        title = "关于云南，你不知道的20个冷知识，带你了解最真实的云南风貌",
        time = "2025-08-08 09:09",
        type = 2,
        content = "系统消息"
    ),
    MessageDataEntity(
        messageId = 2,
        title = "您有一条新的催办消息",
    )
)

@Composable
fun MessageScreen(routeToDetail: (Long) -> Unit = {}, onPopBack: () -> Unit = {}) {

    val messageTypeList = MessageType.entries.map { it.typeName }
    val searchType = remember { mutableStateOf(messageTypeList.first()) }
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


        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp)
        ) {
            stickyHeader {
                FilterSearchTextField(
                    dataList = messageTypeList,
                    data = searchType,
                    label = "请输入搜索内容",
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    onValueChange = { type, searchText ->

                    }
                )
            }

            items(messagetList) { index ->
                MessageItem(
                    title = index.title,
                    time = index.time,
                    msgType = MessageType.entries.first { it.ordinal == index.type },

                    ) {
                    routeToDetail(1)
                }
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
        MessageScreen({})
    }

}
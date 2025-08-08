package cn.thecover.media.feature.basis.message

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cn.thecover.media.core.widget.component.YBTopAppBar
import cn.thecover.media.core.widget.icon.YBIcons
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.TertiaryTextColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.feature.basis.home.data.MessageDataEntity

/**
 *  Created by Wing at 10:57 on 2025/8/8
 *  消息详情页
 */


@Composable
fun MessageDetailRoute(msgId: Long,onPopBack:()-> Unit) {
    MessageDetailScreen(messagetList.first { it.messageId==msgId },onPopBack=onPopBack)
}

@Composable
fun MessageDetailScreen(msgDetail: MessageDataEntity,onPopBack:( () -> Unit)?=null) {
    val msgType= MessageType.entries.first { msgDetail.type==it.ordinal }
    Column(modifier = Modifier.fillMaxSize()) {
        YBTopAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp).clickable{
                    onPopBack?.invoke()
                },
            title = "消息详情",
            backgroundColor = MaterialTheme.colorScheme.background,
            navigationIcon = {
                Icon(
                    painter = painterResource(YBIcons.Custom.BackArrow),
                    contentDescription = "返回"
                )
            })

        HorizontalDivider(modifier = Modifier.fillMaxWidth().height(0.25.dp), color = MaterialTheme.colorScheme.outline)
        Text(
            msgDetail.title,
            style = MaterialTheme.typography.bodyLarge,
            color = MainTextColor,
            modifier = Modifier.padding(16.dp)
        )


        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 16.dp)) {
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
            Spacer(modifier = Modifier.width(20.dp))
            Text(msgDetail.time, style = MaterialTheme.typography.labelMedium, color = TertiaryTextColor)

        }
    }
}
@Composable
@Preview(showBackground = true)
fun MessageDetailScreenPreview() {
    YBTheme {
        MessageDetailScreen(MessageDataEntity(
            messageId = 1,
            content = "这是消息内容",
            time = "2025-08-08 10:57:00",
            type = MessageType.SYSTEM.ordinal,
            title = "系统消息",

        ))
    }

}

 
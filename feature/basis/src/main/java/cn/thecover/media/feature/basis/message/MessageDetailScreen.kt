package cn.thecover.media.feature.basis.message

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import cn.thecover.media.core.network.previewRetrofit
import cn.thecover.media.core.widget.component.CommonTopAppBar
import cn.thecover.media.core.widget.event.clickableWithoutRipple
import cn.thecover.media.core.widget.icon.CommonIcons
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.TertiaryTextColor
import cn.thecover.media.core.widget.theme.CommonTheme
import cn.thecover.media.feature.basis.message.data.entity.MessageDataEntity
import cn.thecover.media.feature.basis.mine.MineViewModel

/**
 *  Created by Wing at 10:57 on 2025/8/8
 *  消息详情页
 */


@Composable
fun MessageDetailScreen(
    viewModel: MineViewModel,
    message: MessageDataEntity,
    onPopBack: (() -> Unit)? = null
) {
    val currentOnPopBack by rememberUpdatedState(onPopBack)

    BackHandler(enabled = onPopBack != null) {
        currentOnPopBack?.invoke()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .clickableWithoutRipple {
                // 消耗点击事件，防止穿透到下层
            }
    ) {
        CommonTopAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable {
                    onPopBack?.invoke()
                },
            title = "消息详情",
            backgroundColor = MaterialTheme.colorScheme.background,
            navigationIcon = {
                Icon(
                    painter = painterResource(CommonIcons.Custom.BackArrow),
                    contentDescription = "返回"
                )
            })

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.25.dp),
            color = MaterialTheme.colorScheme.outline
        )

        val msgType = MessageType.entries.first { message.type == it.ordinal }

        Column(modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())) {
            Text(
                message.content ?: "",
                style = MaterialTheme.typography.bodyLarge,
                color = MainTextColor,
                modifier = Modifier.padding(16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
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
                Text(
                    message.createTime ?: "",
                    style = MaterialTheme.typography.labelMedium,
                    color = TertiaryTextColor
                )
            }
            Spacer(modifier = Modifier.height(25.dp))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun MessageDetailScreenPreview() {
    CommonTheme {
        MessageDetailScreen(
            MineViewModel(
                SavedStateHandle(),
                retrofit = { previewRetrofit }
            ), message = MessageDataEntity())
    }
}

 
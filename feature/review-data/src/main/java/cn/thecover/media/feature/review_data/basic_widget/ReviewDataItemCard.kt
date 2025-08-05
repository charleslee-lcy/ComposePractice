package cn.thecover.media.feature.review_data.basic_widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cn.thecover.media.core.widget.theme.YBShapes

/**
 *  Created by Wing at 09:41 on 2025/8/5
 *  考核数据内部通用卡片样式
 */

@Composable
internal fun DataItemCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier

            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, shape = YBShapes.small)
            .padding(12.dp),
        shape = YBShapes.small,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        content()
    }
}

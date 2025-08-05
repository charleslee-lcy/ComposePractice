package cn.thecover.media.feature.review_data.basic_widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cn.thecover.media.core.widget.R
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.TertiaryTextColor
import cn.thecover.media.core.widget.theme.YBShapes
import cn.thecover.media.core.widget.theme.YBTheme

/**
 *  Created by Wing at 09:45 on 2025/8/5
 *
 */

@Composable
fun DataItemDropMenu(label:String="") {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.background,
                shape = YBShapes.extraSmall
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = MainTextColor)
        Spacer(Modifier.weight(1f))
        Icon(
            painterResource(R.mipmap.ic_arrow_down),
            contentDescription = "${label}下拉筛选按钮",
            tint = TertiaryTextColor
        )
    }
}

@Composable
@Preview
fun DataItemDropMenuPreview() {
    YBTheme {
        DataItemDropMenu("部门总稿费")
    }
}
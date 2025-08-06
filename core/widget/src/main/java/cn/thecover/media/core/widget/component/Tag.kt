/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.thecover.media.core.widget.component

import android.R.attr.onClick
import android.R.attr.text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cn.thecover.media.core.widget.R
import cn.thecover.media.core.widget.YBShape
import cn.thecover.media.core.widget.event.clickableWithoutRipple
import cn.thecover.media.core.widget.theme.MainColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.core.widget.ui.ComponentPreview
import kotlin.text.uppercase

@Composable
fun YBTopicTag(
    modifier: Modifier = Modifier,
    followed: Boolean,
    onClick: () -> Unit,
    enabled: Boolean = true,
    text: @Composable () -> Unit,
) {
    Box(modifier = modifier) {
        val containerColor = if (followed) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceVariant.copy(
                alpha = YBTagDefaults.UNFOLLOWED_TOPIC_TAG_CONTAINER_ALPHA,
            )
        }
        TextButton(
            onClick = onClick,
            enabled = enabled,
            colors = ButtonDefaults.textButtonColors(
                containerColor = containerColor,
                contentColor = contentColorFor(backgroundColor = containerColor),
                disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = YBTagDefaults.DISABLED_TOPIC_TAG_CONTAINER_ALPHA,
                ),
            ),
        ) {
            ProvideTextStyle(value = MaterialTheme.typography.labelSmall) {
                text()
            }
        }
    }
}

@Composable
fun YBLabel(
    modifier: Modifier = Modifier,
    space: Dp = 0.dp,
    label: @Composable RowScope.() -> Unit,
    leadingIcon: @Composable (RowScope.() -> Unit)? = null,
    trailingIcon: @Composable (RowScope.() -> Unit)? = null,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = modifier.clickableWithoutRipple{
            onClick.invoke()
        },
        verticalAlignment = verticalAlignment
    ) {
        leadingIcon?.apply {
            invoke(this@Row)
            Spacer(modifier = Modifier.width(space))
        }
        label.invoke(this)
        trailingIcon?.apply {
            Spacer(modifier = Modifier.width(space))
            invoke(this@Row)
        }
    }
}

@Preview
@Composable
fun TagPreview() {
    YBTheme {
        YBTopicTag(followed = true, onClick = {}) {
            Text("Topic".uppercase())
        }
    }
}

@ComponentPreview
@Composable
private fun YBLabelPreview() {
    YBTheme {
        YBLabel(label = { Text("Label") }, leadingIcon = {
            YBShape(modifier = Modifier.size(6.dp, 16.dp), colors = listOf(MainColor, Color.Transparent))
        }, trailingIcon = {
            YBImage(placeholder = painterResource(R.drawable.icon_watch))
        }, onClick = {

        })
    }
}

/**
 * Now in Android tag default values.
 */
object YBTagDefaults {
    const val UNFOLLOWED_TOPIC_TAG_CONTAINER_ALPHA = 0.5f

    // TODO: File bug
    // Button disabled container alpha value not exposed by ButtonDefaults
    const val DISABLED_TOPIC_TAG_CONTAINER_ALPHA = 0.12f
}

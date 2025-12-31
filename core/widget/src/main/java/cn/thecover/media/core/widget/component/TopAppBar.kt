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

@file:OptIn(ExperimentalMaterial3Api::class)

package cn.thecover.media.core.widget.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.thecover.media.core.widget.icon.CommonIcons
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.CommonTheme
import cn.thecover.media.core.widget.ui.PhonePreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    navigationIcon: (@Composable () -> Unit)? = null,
    actionIcon: (@Composable () -> Unit)? = null,
    titleColor: Color = Color(0xFF333333),
    backgroundColor: Color = Color.White,
) {
    CenterAlignedTopAppBar(
        title = { Text(text = title, fontSize = 17.sp) },
        navigationIcon = {
            navigationIcon?.apply {
                navigationIcon.invoke()
            }
        },
        actions = {
            actionIcon?.apply {
                actionIcon.invoke()
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            titleContentColor = titleColor,
            containerColor = backgroundColor
        ),
        modifier = modifier,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    navigationIcon: ImageVector? = null,
    actionIcon: ImageVector? = null,
    titleColor: Color = Color(0xFF333333),
    backgroundColor: Color = Color.White,
    onNavigationClick: () -> Unit = {},
    onActionClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = { Text(text = title, fontSize = 17.sp) },
        navigationIcon = {
            navigationIcon?.apply {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = this,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        },
        actions = {
            actionIcon?.apply {
                IconButton(onClick = onActionClick) {
                    Icon(
                        imageVector = this,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            titleContentColor = titleColor,
            containerColor = backgroundColor
        ),
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleBar(
    modifier: Modifier = Modifier,
    title: String = "标题",
    titleColor: Color = MainTextColor,
    leftOnClick: () -> Unit = {},
    backgroundColor: Color = Color.White,
    left: @Composable (() -> Unit)? = null,
    center: @Composable (() -> Unit)? = null,
    right: @Composable (() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(45.dp)
            .background(backgroundColor)
    ) {
        Box(modifier = Modifier.align(Alignment.CenterStart)) {
            left?.apply {
                invoke()
            } ?: kotlin.run {
                IconButton(onClick = {
                    leftOnClick.invoke()
                }) {
                    Icon(
                        painterResource(CommonIcons.Custom.BackArrow),
                        contentDescription = "返回",
                        tint = MainTextColor
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 12.dp)
        ) {
            center?.apply {
                invoke()
            } ?: kotlin.run {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    color = titleColor,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        Row(modifier = Modifier.align(Alignment.CenterEnd)) {
            right?.apply {
                invoke()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PhonePreview
@Composable
private fun NiaTopAppBarPreview() {
    CommonTheme {
        TitleBar()
    }
}

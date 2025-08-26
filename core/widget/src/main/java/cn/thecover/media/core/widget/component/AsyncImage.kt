/*
 * Copyright 2023 The Android Open Source Project
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

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import cn.thecover.media.core.widget.theme.LocalTintTheme
import coil.compose.AsyncImagePainter.State.Error
import coil.compose.AsyncImagePainter.State.Loading
import coil.compose.rememberAsyncImagePainter

/**
 * 默认为加载本地图片，placeholder设置本地图片资源。
 * 设置imageUrl则为加载网络图片, placeholder为占位图片。
 * @param imageUrl 图片地址
 * @param placeholder 占位图
 */
@Composable
fun YBImage(
    modifier: Modifier = Modifier,
    imageUrl: String = "",
    placeholder: Painter = ColorPainter(Color.LightGray),
    contentScale: ContentScale = ContentScale.Crop,
    enableZoom: Boolean = false
) {
    val iconTint = LocalTintTheme.current.iconTint
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    val imageLoader = rememberAsyncImagePainter(
        model = imageUrl,
        contentScale = contentScale,
        onState = { state ->
            isLoading = state is Loading
            isError = state is Error
        },
    )

    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    var width by remember { mutableFloatStateOf(0f) }
    var height by remember { mutableFloatStateOf(0f) }
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale = (scale * zoomChange).coerceIn(1f, 5f) // 放大倍数限制
        val newOffset = offset + offsetChange * scale
        // 可选：限制平移范围，让图片不能拖出屏幕太远
        offset = if (scale >= 1f) {
            Offset(
                x = newOffset.x.coerceIn(
                    -(width * (scale - 1f)) / 2,
                    (width * (scale - 1f)) / 2
                ),
                y = newOffset.y.coerceIn(
                    -(height * (scale - 1f)) / 2,
                    (height * (scale - 1f)) / 2
                )
            )
        } else {
            Offset.Zero
        }
    }

    Image(
        modifier = modifier
//            .transformable(state = state)
            .pointerInput(Unit) {
                awaitEachGesture {
                    do {
                        val event = awaitPointerEvent()
                        val zoomChange = event.calculateZoom()
                        val offsetChange = event.calculatePan()
                        val pointers = event.changes.size

                        if (pointers >= 2) {              // 只有双指及以上才消费
                            scale = (scale * zoomChange).coerceIn(1f, 5f) // 放大倍数限制
                            val newOffset = offset + offsetChange * scale
                            // 可选：限制平移范围，让图片不能拖出屏幕太远
                            offset = if (scale >= 1f) {
                                Offset(
                                    x = newOffset.x.coerceIn(
                                        -(width * (scale - 1f)) / 2,
                                        (width * (scale - 1f)) / 2
                                    ),
                                    y = newOffset.y.coerceIn(
                                        -(height * (scale - 1f)) / 2,
                                        (height * (scale - 1f)) / 2
                                    )
                                )
                            } else {
                                Offset.Zero
                            }
                            event.changes.forEach { it.consume() }   // 消费掉
                        } else {
                            // 单指 -> 不消费，Pager 能收到
                        }
                    } while (event.changes.any { it.pressed })
                }
            }
            .graphicsLayer {
                width = size.width
                height = size.height
                if (enableZoom) {
                    scaleX = scale
                    scaleY = scale
                    translationX = offset.x
                    translationY = offset.y
//                    rotationZ = rotation
                }
            },
        contentScale = contentScale,
        painter = if (imageUrl.isNotEmpty()) imageLoader else placeholder,
        contentDescription = null,
        colorFilter = if (iconTint != Unspecified) ColorFilter.tint(iconTint) else null,
    )
}

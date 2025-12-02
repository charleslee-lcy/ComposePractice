package cn.thecover.media.core.widget.component

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cn.thecover.media.core.widget.event.clickableWithoutRipple
import cn.thecover.media.core.widget.ui.ComponentPreview
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay


/**
 *
 * <p> Created by CharlesLee on 2025/7/30
 * 15708478830@163.com
 */

@Composable
fun YBBanner(
    modifier: Modifier = Modifier,
    items: SnapshotStateList<String>,
    autoScroll: Boolean = false,
    autoScrollDelay: Long = 3000L
) {
    if (items.isEmpty()) return

    val pageCount = items.size
    val loopingCount = Int.MAX_VALUE
    val startIndex = loopingCount / 2
    val pagerState = rememberPagerState(initialPage = startIndex, pageCount = { loopingCount })

    // 页码转换
    fun pageMapper(index: Int) = (index - startIndex).floorMod(pageCount)

    var underDragging by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        pagerState.interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> underDragging = true
                is PressInteraction.Release -> underDragging = false
                is PressInteraction.Cancel -> underDragging = false
                is DragInteraction.Start -> underDragging = true
                is DragInteraction.Stop -> underDragging = false
                is DragInteraction.Cancel -> underDragging = false
            }
        }
    }

    if (underDragging.not()) {
        LaunchedEffect(key1 = underDragging, key2 = pagerState) {
            try {
                while (autoScroll) {
                    delay(autoScrollDelay)
                    val current = pagerState.currentPage
                    val currentPos = pageMapper(current)
                    val nextPage = current + 1
                    if (underDragging.not()) {
                        val toPage =
                            nextPage.takeIf { nextPage < pagerState.pageCount }
                                ?: (currentPos + startIndex + 1)
                        if (toPage > current) {
                            pagerState.animateScrollToPage(toPage)
                        } else {
                            pagerState.scrollToPage(toPage)
                        }
                    }
                }
            } catch (e: CancellationException) {
                e.printStackTrace()
            }
        }
    }

    Box(contentAlignment = Alignment.BottomCenter, modifier = modifier) {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { pageIndex ->
            YBImage(
                imageUrl = items[pageMapper(pageIndex)],
                modifier = Modifier
                    .fillMaxSize()
                    .clickableWithoutRipple { }
            )
        }

        //指示器
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(items.size) { iteration ->
                val select = pageMapper(pagerState.currentPage) == iteration
                val width = if (select) 12.dp else 4.dp
                val color = if (select) Color(0xCCFFFFFF) else Color(0x99EEEEEE)
                Box(
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .width(width)
                        .height(4.dp)
                )
            }
        }
    }
}

/**
 * 转换下标
 */
private fun Int.floorMod(other: Int): Int = when (other) {
    0 -> this
    else -> this - floorDiv(other) * other
}

@ComponentPreview
@Composable
fun YBBannerPreview() {
    val listData = remember {
        mutableStateListOf(
            "https://gips3.baidu.com/it/u=119870705,2790914505&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=720",
            "https://gips2.baidu.com/it/u=195724436,3554684702&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960",
            "https://gips0.baidu.com/it/u=1490237218,4115737545&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=720",
            "https://gips2.baidu.com/it/u=207216414,2485641185&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=720",
            "https://gips2.baidu.com/it/u=828570294,3060139577&fm=3028&app=3028&f=JPEG&fmt=auto?w=1024&h=1024",
        )
    }

    YBBanner(items = listData, autoScroll = true, autoScrollDelay = 3000L)
}
package cn.thecover.media.core.widget.component.coordinator

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import cn.thecover.media.core.widget.component.CommonButton
import cn.thecover.media.core.widget.component.TitleBar
import cn.thecover.media.core.widget.theme.Blue90
import cn.thecover.media.core.widget.theme.DividerColor
import cn.thecover.media.core.widget.theme.CommonTheme
import cn.thecover.media.core.widget.ui.PhonePreview
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * @param collapsableContent 可折叠的Content
 * @param content 底部的Content
 * @param enableCollapsable 是否允许折叠
 * @param nonCollapsableHeight 不允许折叠的高度，至少为0
 * @param nestedScrollableState
 *
 */
@Composable
fun CoordinatorLayout(
    modifier: Modifier = Modifier,
    nestedScrollableState: () -> ScrollableState,
    collapsableContent: @Composable () -> Unit,
    enableCollapsable: Boolean = true,
    state: CoordinatorState = rememberCoordinatorState(),
    nonCollapsableHeight: Int = 0,
    content: @Composable () -> Unit
) {
    check(nonCollapsableHeight >= 0) {
        "nonCollapsableHeight is at least 0!"
    }

    val flingBehavior = ScrollableDefaults.flingBehavior()
    Layout(
        content = {
            collapsableContent()
            content()
        }, modifier = modifier
            .clipToBounds()
            .fillMaxSize()
            .scrollable(
                state = state.scrollableState,
                orientation = Orientation.Vertical,
                enabled = !state.isFullyCollapsed,
                flingBehavior = remember {
                    object : FlingBehavior {
                        override suspend fun ScrollScope.performFling(initialVelocity: Float): Float {
                            val remain = with(flingBehavior) {
                                performFling(initialVelocity)
                            }
                            if (remain < 0) { // 向上滑动，scrollable消费Fling后，剩余的Fling交给nestedScrollableState消费
                                nestedScrollableState().scroll {
                                    with(flingBehavior) {
                                        performFling(-remain)
                                    }
                                }
                                return 0f
                            }
                            return remain
                        }
                    }
                },
            )
            .nestedScroll(state.nestedScrollConnection)
    ) { measurables, constraints ->
        check(constraints.hasBoundedHeight)
        val height = constraints.maxHeight
        val collapsablePlaceable = measurables[0].measure(
            constraints.copy(minHeight = 0, maxHeight = Constraints.Infinity)
        )
        val collapsableContentHeight = collapsablePlaceable.height

        val nonCollapsableHeightNew = if (enableCollapsable) nonCollapsableHeight else collapsableContentHeight
        val safeNonCollapsableHeight = nonCollapsableHeightNew.coerceAtMost(collapsableContentHeight)
        val contentPlaceable = measurables[measurables.lastIndex].measure(
            constraints.copy(
                minHeight = 0,
                maxHeight = (height - safeNonCollapsableHeight).coerceAtLeast(0)
            )
        )
        state.emptyHeight = (height - collapsableContentHeight).toFloat().coerceAtLeast(200f)
        state.maxCollapsableHeight =
            (collapsablePlaceable.height - safeNonCollapsableHeight).toFloat().coerceAtLeast(0f)
        layout(constraints.maxWidth, height) {
            val collapsedHeight = state.collapsedHeight.roundToInt()
            collapsablePlaceable.placeRelative(0, -collapsedHeight)
            contentPlaceable.placeRelative(0, collapsableContentHeight - collapsedHeight)
        }
    }
}

@PhonePreview
@Composable
private fun CoordinatorLayoutPreview() {
    CommonTheme {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .systemBarsPadding()
        ) {
            val coroutineScope = rememberCoroutineScope()
            val lazyListState = rememberLazyListState()
            val coordinatorState = rememberCoordinatorState()

            Box(
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                TitleBar(title = "标题", left = {})
                Button(onClick = {
                    coroutineScope.launch {
                        if (coordinatorState.isFullyCollapsed) {
                            coordinatorState.animateToExpanded()
                        } else {
                            coordinatorState.animateToCollapsed()
                        }
                    }
                }, modifier = Modifier.align(Alignment.CenterEnd)) {
                    Text("切换")
                }
            }

            HorizontalDivider(color = DividerColor)

            CoordinatorLayout(
                nestedScrollableState = { lazyListState },
                state = coordinatorState,
//                nonCollapsableHeight = with(LocalDensity.current) { 300.dp.toPx().roundToInt()},
                modifier = Modifier.fillMaxSize(),
                collapsableContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .background(Blue90)
                    )
                },
            ) {
                Column(Modifier.fillMaxSize()) {
                    CommonButton(onClick = {
                        // 吸顶
                        coroutineScope.launch {
                            coordinatorState.animateToCollapsed()
                        }
                    }) {
                        Text(text = "吸顶")
                    }
                    LazyColumn(Modifier.fillMaxSize(), state = lazyListState) {
                        items(30) {
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .padding(horizontal = 15.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = "Item $it",
                                    textAlign = TextAlign.Center,

                                    )
                                HorizontalDivider(
                                    thickness = 0.7.dp,
                                    color = DividerColor,
                                    modifier = Modifier.align(Alignment.BottomStart)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
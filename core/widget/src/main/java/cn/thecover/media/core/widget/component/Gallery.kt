package cn.thecover.media.core.widget.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.thecover.media.core.widget.component.popup.CommonFullDialog
import cn.thecover.media.core.widget.event.clickableWithoutRipple
import cn.thecover.media.core.widget.theme.MainTextColor


/**
 *
 * <p> Created by CharlesLee on 2025/8/27
 * 15708478830@163.com
 */
@Composable
fun PreviewImages(
    imagesData: List<String>,
    showImages: MutableState<Boolean>,
    initialPage: Int = 0
) {
    CommonFullDialog(
        dialogState = showImages,
        onDismissRequest = { showImages.value = false },
        content = {
            PreviewImagesCore(imagesData, showImages, initialPage = initialPage)
        },
        backgroundColor = Color.Transparent
    )
}

@Composable
fun PreviewImagesCore(
    imagesData: List<String>,
    showImages: MutableState<Boolean>,
    initialPage: Int = 0
) {
    val pagerState = rememberPagerState(initialPage = initialPage, pageCount = { imagesData.size })
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MainTextColor)
    ) {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { pageIndex ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clipToBounds()
            ) {
                CommonImage(
                    imageUrl = imagesData[pageIndex],
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit,
                    enableZoom = true
                )
            }
        }
        Icon(
            modifier = Modifier
                .statusBarsPadding()
                .clickableWithoutRipple {
                    showImages.value = false
                }.padding(10.dp),
            imageVector = Icons.Outlined.Close,
            tint = Color.White,
            contentDescription = "关闭"
        )
        Text(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(vertical = 15.dp),
            text = "${pagerState.currentPage + 1}/${imagesData.size}",
            style = TextStyle(
                color = Color.White,
                fontSize = 14.sp,
                shadow = Shadow(
                    color = Color(0x99000000),
                    offset = Offset(4f, 4f),   // x, y 偏移
                    blurRadius = 8f            // 模糊半径
                )
            )
        )
    }
}
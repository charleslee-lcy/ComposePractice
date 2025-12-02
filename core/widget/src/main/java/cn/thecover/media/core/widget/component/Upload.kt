package cn.thecover.media.core.widget.component

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import cn.thecover.media.core.widget.R
import cn.thecover.media.core.widget.event.clickableWithoutRipple
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.core.widget.ui.PhonePreview


/**
 *
 * <p> Created by CharlesLee on 2025/8/26
 * 15708478830@163.com
 */
@Composable
fun UploadMedia(
    modifier: Modifier = Modifier,
    items: List<Uri> = emptyList()
) {
    val context = LocalContext.current
    val listData = remember { mutableStateListOf<Uri>() }
    val showAdd by remember { mutableStateOf(true) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                listData.add(it)
            }
        }
    )

    val showImages = remember { mutableStateOf(false) }
    var initialPage by remember { mutableIntStateOf(0) }

    val permission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        launcher.launch("image/*")
    }

    LaunchedEffect(Unit) {
        listData.addAll(items)
    }

    LazyVerticalGrid(
        modifier = modifier.fillMaxWidth(),
        columns = GridCells.Fixed(4),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp),
    ) {
        items(listData.size) {
            MediaItem(
                modifier = Modifier.animateItem(),
                listData[it],
                showIcon = false,
                onClick = {
                    initialPage = it
                    showImages.value = true
                }, onDeleteClick = {
                    listData.removeAt(it)
                })
        }
        if (showAdd) {
            item {
                MediaItem(modifier = Modifier.animateItem(), isAdd = true, onClick = {
                    if (Build.VERSION.SDK_INT < 33 &&
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        permission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    } else {
                        // 打开系统图库，只显示图片
                        launcher.launch("image/*")
                    }
                })
            }
        }
    }

    PreviewImages(listData.map { it.toString() }, showImages, initialPage = initialPage)
}

@Composable
private fun MediaItem(
    modifier: Modifier = Modifier,
    uri: Uri = "".toUri(),
    showIcon: Boolean = false,
    isAdd: Boolean = false,
    onClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    val showIcon by remember { mutableStateOf(showIcon) }
    val showDelete by remember { mutableStateOf(true) }
    ConstraintLayout(modifier = modifier
        .clickableWithoutRipple {
            onClick.invoke()
        }) {
        val (image, icon, close) = createRefs()
        YBImage(
            modifier = Modifier
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.ratio("1:1")
                }
                .fillMaxSize(),
            imageUrl = uri.toString(),
            placeholder = if (isAdd)
                painterResource(R.mipmap.ic_media_add)
            else
                ColorPainter(Color(0xFFDDDDDD)),
            contentScale = ContentScale.Crop
        )

        if (showIcon) {
            Icon(
                modifier = Modifier
                    .constrainAs(icon) {
                        centerTo(image)
                    }
                    .size(32.dp),
                imageVector = Icons.Default.PlayCircleOutline,
                tint = Color.White,
                contentDescription = ""
            )
        }

        if (showDelete && !isAdd) {
            YBImage(
                modifier = Modifier
                    .constrainAs(close) {
                        top.linkTo(image.top)
                        end.linkTo(image.end)
                    }
                    .size(20.dp)
                    .clickableWithoutRipple {
                        onDeleteClick.invoke()
                    }
                    .padding(start = 2.dp, top = 1.dp, end = 1.dp, bottom = 2.dp),
                placeholder = painterResource(R.mipmap.ic_media_delete),
            )
        }
    }
}

@PhonePreview
@Composable
private fun UploadMediaPreview() {
    YBTheme {
        val items = remember { mutableStateOf(listOf<Uri>()) }
        UploadMedia(items = items.value)
    }
}
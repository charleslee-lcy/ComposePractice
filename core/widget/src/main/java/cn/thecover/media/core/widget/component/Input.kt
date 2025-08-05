package cn.thecover.media.core.widget.component

import android.R.attr.singleLine
import android.R.attr.text
import android.R.attr.textStyle
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.OutlinedTextFieldDefaults.contentPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.thecover.media.core.widget.theme.EditHintTextColor
import cn.thecover.media.core.widget.theme.MainColor
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.YBTheme
import cn.thecover.media.core.widget.ui.PhonePreview


/**
 * 统一文本输入框
 * <p> Created by CharlesLee on 2025/8/5
 * 15708478830@163.com
 */

@Composable
fun YBInput(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle(
        fontSize = 13.sp,
        color = MainTextColor
    ),
    showKeyboard: Boolean = false,
    hint: String = "",
    hintTextSize: TextUnit = 13.sp,
    hintTextColor: Color = EditHintTextColor,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onValueChange: (String) -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val textState = remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val textStyle = textStyle

    LaunchedEffect(Unit) {
        if (showKeyboard) {
            focusRequester.requestFocus()
        }
    }

    BasicTextField(
        value = textState.value,
        onValueChange = {
            textState.value = it
            onValueChange.invoke(it)
        },
        modifier = modifier.focusRequester(focusRequester),
        textStyle = textStyle,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        cursorBrush = SolidColor(MainColor),
        visualTransformation = visualTransformation,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues = contentPadding)
                    .background(Color.Transparent)
            ) {
                if (textState.value.isEmpty()) {
                    Text(
                        text = hint,
                        style = textStyle.copy(
                            color = hintTextColor,
                            fontSize = hintTextSize
                        )
                    )
                }
                innerTextField()
            }
        }
    )
}

@PhonePreview
@Composable
fun YBInputPreview() {
    YBTheme {
        YBInput(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            hint = "请输入搜索内容",
            hintTextSize = 13.sp,
            hintTextColor = EditHintTextColor,
            onValueChange = {
                Log.d("CharlesLee", it)
            }
        )
    }
}
package cn.thecover.media.core.widget.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.thecover.media.core.widget.theme.EditHintTextColor
import cn.thecover.media.core.widget.theme.MainColor
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.PageBackgroundColor
import cn.thecover.media.core.widget.theme.TertiaryTextColor
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
    text: String = "",
    textStyle: TextStyle = TextStyle(
        fontSize = 13.sp, color = MainTextColor
    ),
    showKeyboard: Boolean = false,
    hint: String = "",
    hintTextSize: TextUnit = textStyle.fontSize,
    hintTextColor: Color = EditHintTextColor,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    maxLength: Int = Int.MAX_VALUE,
    showCount: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isDigitLimit: Boolean = false,
    onValueChange: (String) -> Unit = {},
    contentPadding: Dp = 0.dp,
) {
    val textState = remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val textStyle = textStyle

    LaunchedEffect(Unit) {
        if (showKeyboard) {
            focusRequester.requestFocus()
        }

        textState.value = text
    }

    BasicTextField(
        value = textState.value,
        onValueChange = {
            val result = if (isDigitLimit) {
                it.filter { it.isDigit() }
            } else {
                it
            }
            if (result.length > maxLength) {
                val cutText = result.substring(0, maxLength)
                textState.value = cutText
                onValueChange.invoke(cutText)
                return@BasicTextField
            }
            textState.value = result
            onValueChange.invoke(result)
        },
        modifier = modifier.focusRequester(focusRequester),
        textStyle = textStyle.copy(lineHeight = textStyle.fontSize * 1.5f),
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        cursorBrush = SolidColor(MainColor),
        keyboardOptions = if (isDigitLimit) KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number) else KeyboardOptions.Default,
        visualTransformation = visualTransformation,
        decorationBox = { innerTextField ->
            Box(Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = contentPadding,
                            top = contentPadding,
                            end = contentPadding,
                            bottom = if (showCount && maxLength != Int.MAX_VALUE) contentPadding + 14.dp else contentPadding
                        )
                        .background(Color.Transparent)
                ) {
                    if (textState.value.isEmpty()) {
                        Text(
                            text = hint, style = textStyle.copy(
                                color = hintTextColor, fontSize = hintTextSize
                            )
                        )
                    }
                    innerTextField()
                }

                if (showCount && maxLength != Int.MAX_VALUE) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(horizontal = 10.dp),
                        text = "${textState.value.length}/${maxLength}",
                        fontSize = 10.sp,
                        color = TertiaryTextColor
                    )
                }
            }
        })
}


@PhonePreview
@Composable
fun YBInputPreview() {
    YBTheme {
        YBInput(
            modifier = Modifier
                .padding(14.dp)
                .border(0.5.dp, Color(0xFFEAEAEB), RoundedCornerShape(12.dp))
                .fillMaxWidth()
                .height(150.dp)
                .background(
                    PageBackgroundColor
                ),
            text = "输入意见，不超过200字输入意见，不超过200字输入输入意见，不超过200字输入意见，不超过200字输入意见，不超过200字输入意见，不超过20输入意见，不超过200字输入意见，不超过200字输入意见，不超过200字输入意见，不超过20输入意见，不超过200字输入意见，不超过200字输入意见，不超过200字输入意见，不超过20输入意见，不超过200字输入意见，不超过200字输入意见，不超过200字输入意见，不超过20意见，不超过200字输入意见，不超过200字输入意见，不超过200字输入意见，不超过200字输入意见，不超过200字输入意见，不超过200字输入意见，不超过200字输入意见，不超过200字输入意见，不超过200字输入意见，不超过200字输入意见，不超过200字输入意见，不超过200字输入意见，不超过200字输入意见，不超过200字输入意见，不超过200字输入意见，不超过200字输入意见，不超过200字输入意见，不超过200字输入意见，不超过200字输入意见，不超过200字输入意见，不超过200字输入意见，不超过200字输入意见，不超过200字输入意见，不超过200字输入意见，不超过200字输入意见，不超过200字输入意见，不超过200字输入意见，不超过200字输入意见，不超过200字输入意见，不超过200字输入意见，不超过200字输入意见，不超过200字输入意见，不超过200字",
            textStyle = TextStyle(
                fontSize = 15.sp, color = MainTextColor
            ),
            hint = "输入意见，不超过200字",
            singleLine = false,
            maxLength = 200,
            showCount = true,
            contentPadding = 12.dp,
            onValueChange = {

            })
    }
}
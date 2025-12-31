package cn.thecover.media.core.widget.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.thecover.media.core.widget.icon.CommonIcons
import cn.thecover.media.core.widget.theme.EditHintTextColor
import cn.thecover.media.core.widget.theme.MainColor
import cn.thecover.media.core.widget.theme.MainTextColor
import cn.thecover.media.core.widget.theme.PageBackgroundColor
import cn.thecover.media.core.widget.theme.TertiaryTextColor
import cn.thecover.media.core.widget.theme.CommonTheme
import cn.thecover.media.core.widget.ui.PhonePreview


/**
 * 统一文本输入框
 * <p> Created by CharlesLee on 2025/8/5
 * 15708478830@163.com
 */

@Composable
fun CommonInput(
    modifier: Modifier = Modifier,
    text: String = "",
    textStyle: TextStyle = TextStyle(
        fontSize = 13.sp, color = MainTextColor
    ),
    showKeyboard: Boolean = false,
    hint: String = "",
    hintTextStyle: TextStyle = TextStyle(
        fontSize = textStyle.fontSize, color = EditHintTextColor
    ),
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    maxLength: Int = Int.MAX_VALUE,
    isPassword: Boolean = false,
    showVisibleIcon: Boolean = false,
    showCount: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onValueChange: (String) -> Unit = {},
    contentPadding: Dp = 0.dp,
    contentAlignment: Alignment = Alignment.CenterStart,
    ignoreEmptyInput: Boolean = false,
) {
    var textVisible by remember { mutableStateOf(!isPassword) }
    val focusRequester = remember { FocusRequester() }
    val textStyle = textStyle
    var textState by remember { mutableStateOf(if (ignoreEmptyInput) text.trim() else text) }

    LaunchedEffect(if (ignoreEmptyInput) text.replace(Regex("[ \\t]+$"), "") else text) {
        textState = if (ignoreEmptyInput) {
            // 只过滤末尾的空格和制表符，保留换行符
            text.replace(Regex("[ \\t]+$"), "")
        } else {
            text
        }
    }

    LaunchedEffect(Unit) {
        if (showKeyboard) {
            focusRequester.requestFocus()
        }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = textState,
            onValueChange = {
                val result = if (keyboardOptions.keyboardType == KeyboardType.Number ||
                    keyboardOptions.keyboardType == KeyboardType.Phone ||
                    keyboardOptions.keyboardType == KeyboardType.NumberPassword
                ) {
                    it.filter { char -> char.isDigit() }
                } else {
                    it
                }
                if (result.length > maxLength) {
                    val cutText = result.take(maxLength)
                    textState = if (ignoreEmptyInput) {
                        // 只过滤首尾的空格和制表符，保留换行符
                        cutText.replaceFirst(Regex("^[ \\t]+"), "").replaceFirst(Regex("[ \\t]+$"), "")
                    } else {
                        cutText
                    }
                    onValueChange.invoke(textState)
                    return@BasicTextField
                }
                textState = if (ignoreEmptyInput) {
                    // 只过滤首尾的空格和制表符，保留换行符
                    result.replaceFirst(Regex("^[ \\t]+"), "").replaceFirst(Regex("[ \\t]+$"), "")
                } else {
                    result
                }
                onValueChange.invoke(textState)
            },
            modifier = Modifier
                .focusRequester(focusRequester)
                .weight(1f),
            textStyle = textStyle.copy(lineHeight = textStyle.fontSize * 1.5f),
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            cursorBrush = SolidColor(MainColor),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = if (textVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            decorationBox = { innerTextField ->
                Box(Modifier.fillMaxSize(), contentAlignment = contentAlignment) {
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
                        if (textState.isEmpty()) {
                            Text(
                                text = hint, style = hintTextStyle
                            )
                        }
                        innerTextField()
                    }

                    if (showCount && maxLength != Int.MAX_VALUE) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(horizontal = 10.dp),
                            text = "${textState.length}/${maxLength}",
                            fontSize = 10.sp,
                            color = TertiaryTextColor
                        )
                    }
                }
            })

        AnimatedVisibility(showVisibleIcon) {
            IconButton(onClick = { textVisible = !textVisible }) {
                Icon(
                    painterResource(if (textVisible) CommonIcons.Custom.PasswordIsShow else CommonIcons.Custom.PasswordIsHide),
                    tint = TertiaryTextColor,
                    contentDescription = if (textVisible) "隐藏内容" else "查看内容"
                )
            }
        }
    }
}

/**
 * 普通单行文本输入框
 */
@Composable
fun SingleLineInput(
    modifier: Modifier = Modifier,
    text: String = "",
    textStyle: TextStyle = TextStyle(
        fontSize = 13.sp, color = MainTextColor
    ),
    showKeyboard: Boolean = false,
    hint: String = "",
    hintTextStyle: TextStyle = TextStyle(
        fontSize = textStyle.fontSize, color = EditHintTextColor
    ),
    maxLength: Int = Int.MAX_VALUE,
    isPassword: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onValueChange: (String) -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(0.dp),
    ignoreEmptyInput: Boolean = false,
    isDecimal: Boolean = false // 添加小数支持参数
) {
    var textVisible by remember { mutableStateOf(!isPassword) }
    val focusRequester = remember { FocusRequester() }
    val textStyle = textStyle
    var textState by remember { mutableStateOf(if (ignoreEmptyInput) text.trim() else text) }
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = textState,
                selection = TextRange(textState.length) // 初始化时光标在最后
            )
        )
    }

    // 当外部text变化时，更新TextFieldValue
    LaunchedEffect(text) {
        val newText = if (ignoreEmptyInput) text.trim() else text
        textFieldValue = TextFieldValue(
            text = newText,
            selection = TextRange(newText.length) // 确保光标在最后
        )
        textState = newText
    }

    LaunchedEffect(Unit) {
        if (showKeyboard) {
            focusRequester.requestFocus()
        }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = textFieldValue,
            onValueChange = { newValue ->
                val result = if (isDecimal) {
                    // 支持数字和小数点的输入
                    val filtered = newValue.text.filter { char ->
                        char.isDigit() || char == '.'
                    }
                    // 确保只有一个小数点
                    val parts = filtered.split('.')
                    if (parts.size > 2) {
                        // 如果有多个小数点，只保留第一个小数点
                        parts[0] + "." + parts.drop(1).joinToString("")
                    } else {
                        filtered
                    }
                } else if (keyboardOptions.keyboardType == KeyboardType.Number ||
                    keyboardOptions.keyboardType == KeyboardType.Phone ||
                    keyboardOptions.keyboardType == KeyboardType.NumberPassword
                ) {
                    newValue.text.filter { char -> char.isDigit() }
                } else {
                    newValue.text
                }

                if (result.length > maxLength) {
                    val cutText = result.take(maxLength)
                    textState = if (ignoreEmptyInput) cutText.trim() else cutText
                } else {
                    textState = if (ignoreEmptyInput) result.trim() else result
                }
                textFieldValue = TextFieldValue(
                    text = textState,
                    selection = TextRange(textState.length)
                )
                onValueChange.invoke(textState)
            },
            modifier = Modifier
                .focusRequester(focusRequester)
                .weight(1f),
            textStyle = textStyle.copy(lineHeight = textStyle.fontSize * 1.5f),
            singleLine = true,
            cursorBrush = SolidColor(MainColor),
            keyboardOptions = if (isDecimal) {
                KeyboardOptions(keyboardType = KeyboardType.Decimal)
            } else {
                keyboardOptions
            },
            keyboardActions = keyboardActions,
            visualTransformation = if (textVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(contentPadding),
                    contentAlignment = when (textStyle.textAlign) {
                        TextAlign.Start -> Alignment.CenterStart
                        TextAlign.End -> Alignment.CenterEnd
                        TextAlign.Center -> Alignment.Center
                        else -> Alignment.CenterStart
                    }
                ) {
                    if (textState.isEmpty()) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = hint,
                            style = hintTextStyle
                        )
                    }
                    innerTextField()
                }
            })
    }
}


@PhonePreview
@Composable
fun CommonInputPreview() {
    var assignScore by remember { mutableStateOf("100") }
    CommonTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            CommonInput(
                modifier = Modifier
                    .padding(14.dp)
                    .border(0.5.dp, Color(0xFFEAEAEB), RoundedCornerShape(12.dp))
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(
                        PageBackgroundColor
                    ),
                text = "是否萨达防守打法是FS电风扇地方水电费是",
                textStyle = TextStyle(
                    fontSize = 15.sp, color = MainTextColor
                ),
                hint = "输入意见，不超过200字",
                singleLine = false,
                maxLength = 200,
                showCount = true,
                contentPadding = 12.dp,
                showVisibleIcon = true,
                contentAlignment = Alignment.Center,
                onValueChange = {

                })

            SingleLineInput(
                modifier = Modifier
                    .padding(14.dp)
                    .border(0.5.dp, Color(0xFFEAEAEB), RoundedCornerShape(12.dp))
                    .width(200.dp)
                    .height(36.dp)
                    .background(
                        PageBackgroundColor
                    ),
                text = assignScore,
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    color = MainTextColor,
                    textAlign = TextAlign.Start
                ),
                hint = "暂未输入",
                hintTextStyle = TextStyle(
                    fontSize = 14.sp,
                    color = EditHintTextColor
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                contentPadding = PaddingValues(horizontal = 12.dp),
                onValueChange = {
                    assignScore = it
                }
            )
        }
    }
}
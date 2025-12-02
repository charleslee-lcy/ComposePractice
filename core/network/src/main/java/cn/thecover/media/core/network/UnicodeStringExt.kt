package cn.thecover.media.core.network

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 *
 * @author TT
 * @since 2021-12-20
 */
private const val UNICODE_REGEX = "\\\\u[a-f0-9A-F]{1,4}"

fun String?.encodeUnicode(): String {
    val origin = this ?: return ""

    val sb = StringBuffer()
    for (element in origin) {
        // 取出每一个字符
        // 转换为unicode
        sb.append("\\u" + Integer.toHexString(element.toInt()))
    }
    return sb.toString()
}

fun String?.decodeUnicode(): String {
    val unicode = this ?: return ""

    val length: Int = unicode.length
    var count = 0
    //正则匹配条件，可匹配“\\u”1到4位，一般是4位可直接使用 String regex = "\\\\u[a-f0-9A-F]{4}";
    val pattern: Pattern = Pattern.compile(UNICODE_REGEX)
    val matcher: Matcher = pattern.matcher(unicode)
    val sb = StringBuffer()

    while (matcher.find()) {
        val oldChar: String = matcher.group() //原本的Unicode字符
        val newChar: String = oldChar.parseUnicode() //转换为普通字符
        // int index = unicodeStr.indexOf(oldChar);
        // 在遇见重复出现的unicode代码的时候会造成从源字符串获取非unicode编码字符的时候截取索引越界等
        val index: Int = matcher.start()
        sb.append(unicode.substring(count, index)) //添加前面不是unicode的字符
        sb.append(newChar) //添加转换后的字符
        count = index + oldChar.length //统计下标移动的位置
    }
    sb.append(unicode.substring(count, length)) //添加末尾不是Unicode的字符

    return sb.toString()
}

private fun String.parseUnicode(): String {
    val string = StringBuffer()
    val hex = this.split("\\\\u").toTypedArray()
    for (i in 1 until hex.size) {
        // 转换出每一个代码点
        val data = hex[i].toInt(16)
        // 追加成string
        string.append(data.toChar())
    }
    return string.toString()
}
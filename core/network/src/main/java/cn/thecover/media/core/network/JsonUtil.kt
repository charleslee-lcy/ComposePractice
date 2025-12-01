package cn.thecover.media.core.network

import java.util.regex.Pattern

/**
 * Json 工具类
 * 提供简单的 Json 字符串美化和 Json 字符串压缩方法.
 */
class JsonUtil private constructor() {
    companion object {
        /**
         * Json 字符串美化.
         *
         * @param json 待处理的 Json 字符串
         * @return 美化后的 Json 字符串.
         */
        fun jsonBeautify(json: String?): String {
            if (json == null) return ""
            val stringBuffer = StringBuilder()
            var index = 0
            var count = 0
            while (index < json.length) {
                val ch = json[index]
                if (ch == '{' || ch == '[') {
                    stringBuffer.append(ch)
                    stringBuffer.append('\n')
                    count++
                    for (i in 0 until count) {
                        stringBuffer.append('\t')
                    }
                } else if (ch == '}' || ch == ']') {
                    stringBuffer.append('\n')
                    count--
                    for (i in 0 until count) {
                        stringBuffer.append('\t')
                    }
                    stringBuffer.append(ch)
                } else if (ch == ',') {
                    stringBuffer.append(ch)
                    stringBuffer.append('\n')
                    for (i in 0 until count) {
                        stringBuffer.append('\t')
                    }
                } else {
                    stringBuffer.append(ch)
                }
                index++
            }
            return stringBuffer.toString()
        }

        /**
         * Json 压缩.
         *
         * @param json 待处理的 Json 字符串.
         * @return 压缩后的 Json 字符串.
         */
        fun compression(json: String): String {
            val regEx = "[\t\n]"
            val p = Pattern.compile(regEx)
            val m = p.matcher(json)
            return m.replaceAll("").trim { it <= ' ' }
        }
    }
}



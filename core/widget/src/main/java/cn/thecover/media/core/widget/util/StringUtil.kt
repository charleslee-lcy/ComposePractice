package cn.thecover.media.core.widget.util

import java.math.BigDecimal


/**
 *
 * <p> Created by CharlesLee on 2025/12/26
 * 15708478830@163.com
 */

fun formatDecimalString(decimalString: String): String {
    return try {
        BigDecimal(decimalString).stripTrailingZeros().toPlainString()
    } catch (e: NumberFormatException) {
        decimalString
    }
}
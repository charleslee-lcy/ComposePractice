package cn.thecover.media.feature.review_data.data

import java.time.LocalDate

/**
 *  Created by Wing at 10:23 on 2025/12/2
 *
 */

open class FilterState(
    open val selectedDate: String = "${if (LocalDate.now().monthValue == 1) LocalDate.now().year - 1 else LocalDate.now().year}年${if (LocalDate.now().monthValue == 1) 12 else LocalDate.now().monthValue - 1}月",
){
    private fun getYearStr(): String {
        return try {
            // 使用正则表达式提取年份数字，更加健壮
            val yearRegex = Regex("^(\\d{4})年")
            yearRegex.find(selectedDate)?.groupValues?.get(1) ?: ""
        } catch (e: Exception) {
            // 出现异常时返回空字符串或默认值
            ""
        }
    }


    private fun getMonthStr(): String {
        return try {
            // 使用正则表达式提取月份数字，兼容一位或两位数字
            val monthRegex = Regex("年(\\d{1,2})月")
            val month = monthRegex.find(selectedDate)?.groupValues?.get(1) ?: ""
            // 确保返回两位数字格式（例如：1月 -> 01）
            month.toIntOrNull()?.toString()?.padStart(2, '0') ?: ""
        } catch (e: Exception) {
            // 出现异常时返回空字符串或默认值
            ""
        }
    }

    // 直接返回数字类型的年月
    fun getYearAsInt(): Int {
        return getYearStr().toIntOrNull() ?: 0
    }

    fun getMonthAsInt(): Int {
        return getMonthStr().toIntOrNull() ?: 0
    }
}

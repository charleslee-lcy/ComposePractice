package cn.thecover.media.core.widget.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 *  Created by Wing at 17:51 on 2025/8/18
 *
 */

fun getCurrentTimeToDay(): String {
    val now = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return now.withSecond(0).withNano(0).format(formatter)
}
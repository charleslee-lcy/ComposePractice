package cn.thecover.media.core.common.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId


/**
 *
 * <p> Created by CharlesLee on 2025/8/13
 * 15708478830@163.com
 */

fun LocalDate.toMillisecond(): Long {
    return atStartOfDay(ZoneId.systemDefault())         // 当日 00:00
        .toInstant()
        .toEpochMilli()
}

fun Long.toLocalDate(): LocalDate {
//    return LocalDate.ofInstant(
//        Instant.ofEpochMilli(this),
//        ZoneId.systemDefault()
//    )
    return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
}
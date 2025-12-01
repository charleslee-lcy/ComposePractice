package cn.thecover.media.core.widget.datastore

import androidx.datastore.preferences.core.stringPreferencesKey


/**
 * 轻量化存储 DataStore keys
 * <p> Created by CharlesLee on 2025/8/18
 * 15708478830@163.com
 */
object Keys {
    val USER_TOKEN = stringPreferencesKey("user_token")
    val USER_INFO = stringPreferencesKey("user_info")
    val USER_CLEAR_CACHE_TIME = stringPreferencesKey("user_clear_cache_time")
}
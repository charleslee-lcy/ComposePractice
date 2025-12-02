package cn.thecover.media.core.widget.datastore

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking


/**
 *
 * <p> Created by CharlesLee on 2025/8/18
 * 15708478830@163.com
 */
private const val DS_NAME = "yb_review"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DS_NAME)

/**
 * 组件中使用
 */
@Composable
fun <T> rememberDataStoreState(key: Preferences.Key<T>, default: T): T? {
    val ctx = LocalContext.current
    return ctx.dataStore.data
        .map { it[key] ?: default}
        .collectAsState(initial = null)
        .value
}

suspend fun <T> saveData(
    context: Context,
    key: Preferences.Key<T>,
    value: T
) = context.dataStore.edit { it[key] = value }

fun <T> readData(
    context: Context,
    key: Preferences.Key<T>,
    default: T
): Flow<T> = context.dataStore.data
    .map { it[key] ?: default }

fun getToken(context: Context): String = runBlocking {
    context.dataStore.data
        .map { it[Keys.USER_TOKEN] ?: "" }.first()
}

suspend fun <T> clearData(context: Context, key: Preferences.Key<T>) {
    context.dataStore.edit { it.remove(key) }
}


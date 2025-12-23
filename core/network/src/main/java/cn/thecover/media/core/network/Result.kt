/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.thecover.media.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.ui.res.stringResource
import cn.thecover.media.core.data.NetworkResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import java.net.SocketException
import java.net.UnknownHostException

enum class HttpStatus {
    NORMAL,
    LOADING,
    SUCCESS,
    ERROR
}

const val HTTP_STATUS_SUCCESS = 0
const val HTTP_STATUS_LOGOUT = 403

fun <T> Flow<NetworkResponse<T>>.asResult(): Flow<BaseUiState<T>> =
    map<NetworkResponse<T>, BaseUiState<T>> {
        val result = if (it.status == HTTP_STATUS_SUCCESS) {
            BaseUiState(it.data, HttpStatus.SUCCESS, HTTP_STATUS_SUCCESS, it.message)
        } else {
            BaseUiState(it.data, HttpStatus.ERROR, it.status, it.message.ifEmpty { "请求失败" })
        }
        result
    }
        .onStart { emit(BaseUiState(null, HttpStatus.LOADING, -1, "")) }
        .catch {
            if (it is UnknownHostException || it is SocketException) {
                emit(BaseUiState(null, HttpStatus.ERROR, -1, "网络开小差，请稍后重试"))
            } else {
                emit(BaseUiState(null, HttpStatus.ERROR, -1, it.message ?: ""))
            }
        }

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork
    val capabilities = connectivityManager.getNetworkCapabilities(network)
    return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true &&
            capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) == true
}

data class BaseUiState<T>(
    val data: T? = null,
    val status: HttpStatus = HttpStatus.NORMAL,
    val errorCode: Int = 0,
    val errorMsg: String = "",
    val isSuccess: Boolean = status == HttpStatus.SUCCESS,
    val isError: Boolean = status == HttpStatus.ERROR,
)

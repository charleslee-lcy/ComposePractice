package cn.thecover.media

import android.util.Log
import androidx.lifecycle.ViewModel
import cn.thecover.media.core.data.NetworkResponse
import cn.thecover.media.feature.basis.MainApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Retrofit
import javax.inject.Inject


/**
 *
 * <p> Created by CharlesLee on 2025/7/25
 * 15708478830@163.com
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    val retrofit: dagger.Lazy<Retrofit>
) : ViewModel() {

    fun getHomeData(): Flow<NetworkResponse<Any>> = flow {
        val apiService = retrofit.get().create(MainApi::class.java)
        val user = apiService.getHome()
        emit(user)
    }.flowOn(Dispatchers.IO).catch { e ->
        // 处理异常
        Log.e("MainViewModel", "getHomeData: ${e.message}")
    }

}
package cn.thecover.media.feature.basis.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import cn.thecover.media.core.network.BaseUiState
import cn.thecover.media.core.network.asResult
import cn.thecover.media.feature.basis.HomeApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import javax.inject.Inject

/**
 *
 * <p> Created by CharlesLee on 2025/7/28
 * 15708478830@163.com
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    val retrofit: dagger.Lazy<Retrofit>
) : ViewModel() {
    private val loginUiData = MutableStateFlow(BaseUiState<Any>())
    val loginUiState = loginUiData

    suspend fun login() {
        flow {
            val apiService = retrofit.get().create(HomeApi::class.java)
            val result = apiService.login()
            emit(result)
        }.asResult()
           .collect { result ->
               loginUiData.value = result
            }
    }
}
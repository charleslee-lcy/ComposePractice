package cn.thecover.media.feature.basis.home

import android.R.attr.password
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.navOptions
import cn.thecover.media.core.data.HomeInfo
import cn.thecover.media.core.data.HomeRequest
import cn.thecover.media.core.data.LoginRequest
import cn.thecover.media.core.data.LoginResponse
import cn.thecover.media.core.data.UserInfo
import cn.thecover.media.core.network.BaseUiState
import cn.thecover.media.core.network.HTTP_STATUS_LOGOUT
import cn.thecover.media.core.network.asResult
import cn.thecover.media.core.widget.datastore.Keys
import cn.thecover.media.core.widget.datastore.clearData
import cn.thecover.media.core.widget.datastore.saveData
import cn.thecover.media.feature.basis.HomeApi
import cn.thecover.media.feature.basis.home.navigation.navigateToLogin
import com.google.gson.Gson
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
    private val apiService = retrofit.get().create(HomeApi::class.java)
    val loginUiState = MutableStateFlow(BaseUiState<LoginResponse>())
    val userUiState = MutableStateFlow(UserInfo())
    val homeUiState = MutableStateFlow(BaseUiState<HomeInfo>())

    suspend fun login(username: String, password: String) {
        flow {
            val result = apiService.login(
                LoginRequest(username, password)
            )
            emit(result)
        }.asResult()
            .collect { result ->
                loginUiState.value = result
            }
    }

    /**
     * 获取接口用户信息
     */
    suspend fun getUserInfo(context: Context, navController: NavController) {
        flow {
            val result = apiService.getUserInfo()
            emit(result)
        }.asResult()
            .collect { result ->
                if (result.errorCode == HTTP_STATUS_LOGOUT) {
                    Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show()
                    // 清楚token和用户信息缓存
                    clearData(context, Keys.USER_TOKEN)
                    clearData(context, Keys.USER_INFO)
                    navController.navigateToLogin(navOptions {
                        // 清除所有之前的页面
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    })
                }

                result.data?.let {
                    userUiState.value = it
                    saveData(context, Keys.USER_INFO, Gson().toJson(it))
                }
            }
    }

    /**
     * 首页数据
     */
    suspend fun getHomeInfo(year: Int, month: Int) {
        flow {
            val result = apiService.getHomeInfo(
                HomeRequest(year, month)
            )
            emit(result)
        }.asResult()
            .collect { result ->
                homeUiState.value = result
            }
    }
}
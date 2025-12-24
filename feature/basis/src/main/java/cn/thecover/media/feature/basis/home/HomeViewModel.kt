package cn.thecover.media.feature.basis.home

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.navOptions
import cn.thecover.media.core.data.DiffusionDataEntity
import cn.thecover.media.core.data.HomeInfo
import cn.thecover.media.core.data.HomeRequest
import cn.thecover.media.core.data.LoginRequest
import cn.thecover.media.core.data.LoginResponse
import cn.thecover.media.core.data.ManuscriptDiffusionRequest
import cn.thecover.media.core.data.ManuscriptReviewDataEntity
import cn.thecover.media.core.data.ManuscriptTopRequest
import cn.thecover.media.core.data.PaginatedResult
import cn.thecover.media.core.data.SortConditions
import cn.thecover.media.core.data.UserInfo
import cn.thecover.media.core.network.BaseUiState
import cn.thecover.media.core.network.HTTP_STATUS_LOGOUT
import cn.thecover.media.core.network.HttpStatus
import cn.thecover.media.core.network.asResult
import cn.thecover.media.core.widget.component.TOAST_TYPE_WARNING
import cn.thecover.media.core.widget.datastore.Keys
import cn.thecover.media.core.widget.datastore.clearData
import cn.thecover.media.core.widget.datastore.saveData
import cn.thecover.media.core.widget.event.showToast
import cn.thecover.media.feature.basis.HomeApi
import cn.thecover.media.feature.basis.home.navigation.navigateToLogin
import cn.thecover.media.feature.basis.message.MessageApi
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import java.time.LocalDate
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
    val unreadMessageCount = MutableStateFlow(0)

    // 用于跟踪当前用户ID
    private var currentUserId: Long = 0L

    // 用于临时存储用户名
    private var tempUsername: String? = null

    val homeManuscriptUiState = MutableStateFlow(PaginatedResult<ManuscriptReviewDataEntity>())
    val homeManuscriptDiffusionUiState = MutableStateFlow(PaginatedResult<DiffusionDataEntity>())
    var hasHomeDataFetched by mutableStateOf(false)
    var canShowToast by mutableStateOf(true)
    val curYear = mutableIntStateOf(LocalDate.now().minusMonths(1).year)
    val curMonth = mutableIntStateOf(LocalDate.now().minusMonths(1).monthValue)
    var roleState by mutableIntStateOf(2)

    /**
     * 重置HomeViewModel的状态，用于用户切换时
     */
    fun resetState() {
        hasHomeDataFetched = false
        canShowToast = true
        currentUserId = 0L
    }

    /**
     * 设置临时用户名
     */
    fun setTempUsername(username: String) {
        tempUsername = username
    }

    /**
     * 获取临时用户名
     */
    fun getTempUsername(): String? {
        return tempUsername
    }

    /**
     * 清除临时用户名
     */
    fun clearTempUsername() {
        tempUsername = null
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            flow {
                val start = System.currentTimeMillis()
                val result = apiService.login(
                    LoginRequest(username, password)
                )

                val timeInterval = System.currentTimeMillis() - start

                if (timeInterval < 500L) {
                    delay(500L - timeInterval)
                }
                emit(result)
            }.asResult()
                .collect { result ->
                    loginUiState.value = result
                }
        }
    }

    /**
     * 获取接口用户信息
     */
    fun getUserInfo(context: Context, navController: NavController) {
        viewModelScope.launch {
            flow {
                val result = apiService.getUserInfo()
                emit(result)
            }.asResult()
                .collect { result ->
                    result.data?.let {
                        // 检测用户ID是否变化
                        if (currentUserId != 0L && currentUserId != it.userId) {
                            // 用户发生变化，重置状态
                            hasHomeDataFetched = false
                            canShowToast = true
                        }

                        currentUserId = it.userId
                        userUiState.value = it
                        saveData(context, Keys.USER_INFO, Gson().toJson(it))
                    }
                }
        }
    }

    /**
     * 首页数据
     */
    fun getHomeInfo(year: Int, month: Int) {
        viewModelScope.launch {
            flow {
                val start = System.currentTimeMillis()
                val result = apiService.getHomeInfo(
                    HomeRequest(year, month)
                )

                val timeInterval = System.currentTimeMillis() - start

                if (timeInterval < 500L) {
                    delay(500L - timeInterval)
                }
                emit(result)
            }.asResult()
                .collect { result ->
                    canShowToast = true
                    if (result.status == HttpStatus.LOADING) {
                        homeUiState.update {
                            it.copy(status = HttpStatus.LOADING)
                        }
                    } else {
                        // 检查 errorCode 是否为 1，如果是，则将 HomeInfo 中的 status 设置为 1
                        val updatedResult = if (result.errorCode != 0) {
                            val updatedHomeInfo = HomeInfo(
                                status = 1,
                                statusInfo = result.errorMsg
                            )
                            result.copy(data = updatedHomeInfo)
                        } else {
                            result
                        }

                        homeUiState.value = updatedResult
                    }
                }
        }
    }

    /**
     * 获取未读消息数量
     */
    fun getUnreadMessageCount() {
        viewModelScope.launch {
            flow {
                val apiService = retrofit.get().create(MessageApi::class.java)
                val response = apiService.getUnreadMessageCount()
                emit(response)
            }.asResult()
                .collect { result ->
                    if (result.status == HttpStatus.SUCCESS) {
                        unreadMessageCount.value = result.data ?: 0
                    }
                }
        }
    }

    fun getHomeManuscript() {
        viewModelScope.launch {
            flow {
                val apiService = retrofit.get().create(HomeApi::class.java)
                val response = apiService.getManuscriptReviewTopData(
                    ManuscriptTopRequest(
                        year = curYear.intValue,
                        month = curMonth.intValue,
                        lastId = -1,
                        pageSize = "10"
                    )
                )
                emit(response)
            }.asResult()
                .collect { result ->
                    when (result.status) {
                        HttpStatus.LOADING -> {
                            // 设置加载状态
                            homeManuscriptUiState.update { it.copy(isLoading = true) }
                        }

                        HttpStatus.SUCCESS -> {
                            homeManuscriptUiState.update {
                                it.copy(
                                    dataList = result.data?.dataList ?: emptyList(),
                                    isLoading = false,
                                    error = null
                                )
                            }
                        }

                        else -> {
                            // 保存错误信息
                            homeManuscriptUiState.update {
                                PaginatedResult<ManuscriptReviewDataEntity>().copy(
                                    error = result.errorMsg,
                                    isLoading = false
                                )
                            }
                        }
                    }
                }
        }
    }

    fun getHomeManuscriptDiffusion() {
        viewModelScope.launch {
            flow {
                val apiService = retrofit.get().create(HomeApi::class.java)
                val response = apiService.getManuscriptDiffusionData(
                    ManuscriptDiffusionRequest(
                        year = curYear.intValue,
                        month = curMonth.intValue,
                        sortConditions = listOf(
                            SortConditions.putSortConditions("最终传播分")
                        ),
                        lastId = -1,
                        pageSize = "10"
                    )
                )
                emit(response)
            }.asResult()
                .collect { result ->
                    when (result.status) {
                        HttpStatus.LOADING -> {
                            // 设置加载状态
                            homeManuscriptDiffusionUiState.update { it.copy(isLoading = true) }
                        }

                        HttpStatus.SUCCESS -> {
                            homeManuscriptDiffusionUiState.update {
                                it.copy(
                                    dataList = result.data?.dataList ?: emptyList(),
                                    isLoading = false,
                                    error = null
                                )
                            }
                        }

                        else -> {
                            // 保存错误信息
                            homeManuscriptDiffusionUiState.update {
                                PaginatedResult<DiffusionDataEntity>().copy(
                                    error = result.errorMsg,
                                    isLoading = false
                                )
                            }
                        }
                    }
                }
        }
    }
}
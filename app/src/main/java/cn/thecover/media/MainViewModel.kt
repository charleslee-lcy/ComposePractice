package cn.thecover.media

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.thecover.media.MainActivityUiState.Loading
import cn.thecover.media.MainActivityUiState.Success
import cn.thecover.media.core.data.DarkThemeConfig
import cn.thecover.media.core.data.NetworkResponse
import cn.thecover.media.core.data.ThemeBrand
import cn.thecover.media.core.data.UserData
import cn.thecover.media.feature.basis.MainApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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
    val uiState: StateFlow<MainActivityUiState> = flow<UserData> {
        emit(
            UserData(
                themeBrand = ThemeBrand.DEFAULT,
                darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
                useDynamicColor = false,
                shouldHideOnboarding = true
            )
        )
    }.map {
        Success(it)
    }.stateIn(
        scope = viewModelScope,
        initialValue = Loading,
        started = SharingStarted.WhileSubscribed(5_000),
    )

    fun getHomeData(): Flow<NetworkResponse<Any>> = flow {
        val apiService = retrofit.get().create(MainApi::class.java)
        val user = apiService.getHome()
        emit(user)
    }.flowOn(Dispatchers.IO).catch { e ->
        // 处理异常
        Log.e("MainViewModel", "getHomeData: ${e.message}")
    }

}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState

    data class Success(val userData: UserData) : MainActivityUiState {
        override val shouldDisableDynamicTheming = !userData.useDynamicColor

        override val shouldUseAndroidTheme: Boolean = when (userData.themeBrand) {
            ThemeBrand.DEFAULT -> false
            ThemeBrand.ANDROID -> true
        }

        override fun shouldUseDarkTheme(isSystemDarkTheme: Boolean) =
            when (userData.darkThemeConfig) {
                DarkThemeConfig.FOLLOW_SYSTEM -> isSystemDarkTheme
                DarkThemeConfig.LIGHT -> false
                DarkThemeConfig.DARK -> true
            }
    }

    /**
     * Returns `true` if the state wasn't loaded yet and it should keep showing the splash screen.
     */
    fun shouldKeepSplashScreen() = this is Loading

    /**
     * Returns `true` if the dynamic color is disabled.
     */
    val shouldDisableDynamicTheming: Boolean get() = true

    /**
     * Returns `true` if the Android theme should be used.
     */
    val shouldUseAndroidTheme: Boolean get() = false

    /**
     * Returns `true` if dark theme should be used.
     */
    fun shouldUseDarkTheme(isSystemDarkTheme: Boolean) = isSystemDarkTheme
}
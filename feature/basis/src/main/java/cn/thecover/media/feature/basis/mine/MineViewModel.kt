package cn.thecover.media.feature.basis.mine

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.thecover.media.feature.basis.mine.data.UserAvatarEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *
 * <p> Created by CharlesLee on 2025/7/28
 * 15708478830@163.com
 */
@HiltViewModel
class MineViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    companion object {
        const val CACHE_CLEAR_STATE_FINISHED = 2
        const val CACHE_CLEAR_STATE_STARTED = 1
        const val CACHE_CLEAR_STATE_INITIAL = 0
        const val CACHE_CLEAR_STATE_FAILED = -1
    }

    private val _userAvatarState = MutableStateFlow(UserAvatarEntity("用户名", ""))
    val userAvatarState: StateFlow<UserAvatarEntity> = _userAvatarState

    private val _cacheClearState = MutableStateFlow(CACHE_CLEAR_STATE_INITIAL)
    val cacheClearState: StateFlow<Int> = _cacheClearState

    fun handleIntent(mineIntent: MineIntent) {
        when (mineIntent) {
            is MineIntent.GetUserInfo -> {
                savedStateHandle.set("username", mineIntent.username)
            }

            MineIntent.ClearCache -> {
                _cacheClearState.value = CACHE_CLEAR_STATE_STARTED
                viewModelScope.launch {
                    try {
                        // 模拟缓存清除
                        delay(2000L)
                        _cacheClearState.value = CACHE_CLEAR_STATE_FINISHED

                    } catch (e: Exception) {
                        _cacheClearState.value = CACHE_CLEAR_STATE_FAILED
                    }
                }
            }
        }
    }
}
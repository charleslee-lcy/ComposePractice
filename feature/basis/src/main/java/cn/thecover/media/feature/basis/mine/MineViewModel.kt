package cn.thecover.media.feature.basis.mine

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import cn.thecover.media.feature.basis.mine.data.UserAvatarEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val _userAvatarState = MutableStateFlow(UserAvatarEntity("用户名", ""))
    val userAvatarState: StateFlow<UserAvatarEntity> = _userAvatarState


    fun handleIntent(mineIntent: MineIntent) {
        when (mineIntent) {
            is MineIntent.GetUserInfo -> {
                savedStateHandle.set("username", mineIntent.username)
            }
        }
    }
}
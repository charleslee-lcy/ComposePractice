package cn.thecover.media.feature.basis.mine

import android.R.attr.password
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.thecover.media.core.data.NetworkRequest
import cn.thecover.media.core.network.BaseUiState
import cn.thecover.media.core.network.HttpStatus
import cn.thecover.media.core.network.asResult
import cn.thecover.media.feature.basis.HomeApi
import cn.thecover.media.feature.basis.message.MessageType
import cn.thecover.media.feature.basis.message.data.MessageDataListState
import cn.thecover.media.feature.basis.message.data.entity.MessageDataEntity
import cn.thecover.media.feature.basis.message.intent.MessageIntent
import cn.thecover.media.feature.basis.mine.data.OneTimeUiState
import cn.thecover.media.feature.basis.mine.data.requestParams.ModifyPasswordRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import javax.inject.Inject

/**
 *
 * <p> Created by CharlesLee on 2025/7/28
 * 15708478830@163.com
 */
@HiltViewModel
class MineViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    val retrofit: dagger.Lazy<Retrofit>
) : ViewModel() {

    companion object {
        const val CACHE_CLEAR_STATE_FINISHED = 2
        const val CACHE_CLEAR_STATE_STARTED = 1
        const val CACHE_CLEAR_STATE_INITIAL = 0
        const val CACHE_CLEAR_STATE_FAILED = -1
    }

    private val logoutUiData = MutableStateFlow(BaseUiState<Any>())
    val logoutUiState = logoutUiData

    private val _cacheClearState = MutableStateFlow(CACHE_CLEAR_STATE_INITIAL)
    val cacheClearState: StateFlow<Int> = _cacheClearState


    private val _messageListState: MutableStateFlow<MessageDataListState> =
        MutableStateFlow(MessageDataListState())
    val messageListState = _messageListState

    private val _helpCenterUrlUiData = MutableStateFlow<String?>(null)
    val helpCenterUrlUiData: StateFlow<String?> = _helpCenterUrlUiData

    //一次性 UI事件如 toast、弹窗等状态
    private val _oneTimeUiState = MutableStateFlow(OneTimeUiState())
    val oneTimeUiState: StateFlow<OneTimeUiState> = _oneTimeUiState

    private var currentMessageType = MessageType.ALL


    val messageTestList = listOf(
        MessageDataEntity(
            messageId = 0,
            title = "国家科技重大专项小麦育种新突破",
            time = "2025-08-08 09:09",
            type = 1,
            content = "系统消息"
        ),
        MessageDataEntity(
            messageId = 1,
            title = "关于云南，你不知道的20个冷知识，带你了解最真实的云南风貌",
            time = "2025-08-08 09:09",
            type = 2,
            content = "系统消息"
        ),
        MessageDataEntity(
            messageId = 2,
            title = "您有一条新的催办消息",
        )
    )

    fun handleMessageIntent(messageIntent: MessageIntent) {
        when (messageIntent) {
            is MessageIntent.FetchMessageList -> {
                _messageListState.update {
                    if (messageIntent.loadMore)
                        it.copy(isLoading = true)
                    else
                        it.copy(isRefreshing = true)
                }
                viewModelScope.launch {
                    val result = messageTestList

                    val messageDataList =
                        if (messageIntent.loadMore) messageListState.value.messageDataList + result else result
                    _messageListState.update {
                        it.copy(
                            messageDataList = messageDataList,
                            isLoading = false,
                            isRefreshing = false
                        )
                    }
                }
            }

            is MessageIntent.SearchMessage -> {

            }

            is MessageIntent.UpdateMessageFilter -> {
                //No need currently
            }
        }

    }

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

            is MineIntent.GetHelpCenterUrl -> {
                viewModelScope.launch {
                    flow {
                        val apiService = retrofit.get().create(MineApi::class.java)
                        val response = apiService.getHelpCenterUrl()
                        emit(response)
                    }.asResult()
                        .collect { result ->
                            _helpCenterUrlUiData.update {
                                result.data
                            }
                        }
                }
            }

            is MineIntent.ModifyPassword -> {
                viewModelScope.launch {
                    flow {
                        val apiService = retrofit.get().create(MineApi::class.java)
                        val response = apiService.modifyPassword(
                            ModifyPasswordRequest(
                                oldPassword = mineIntent.oldPassword,
                                password = mineIntent.password,
                                passwordVerify = mineIntent.passwordVerify,
//                                client = "android"
                            )
                        )
                        emit(response)
                    }.asResult()
                        .collect { result ->
                            if(result.status== HttpStatus.SUCCESS){
                                _oneTimeUiState.update {   OneTimeUiState(toastMessage = "修改密码成功")}
                                logout()
                            }else{
                                _oneTimeUiState.update {   OneTimeUiState(toastMessage = result.errorMsg)}
                            }
                        }
                }}
        }
    }

    fun logout() {
        viewModelScope.launch {
            flow {
                val apiService = retrofit.get().create(HomeApi::class.java)
                val result = apiService.logout(NetworkRequest())
                emit(result)
            }.asResult()
                .collect { result ->
                    logoutUiData.value = result
                }
        }
    }
}
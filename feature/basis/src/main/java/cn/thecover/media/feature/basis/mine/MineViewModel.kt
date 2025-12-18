package cn.thecover.media.feature.basis.mine

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.thecover.media.core.common.util.DESUtil
import cn.thecover.media.core.data.NetworkRequest
import cn.thecover.media.core.network.BaseUiState
import cn.thecover.media.core.network.HttpStatus
import cn.thecover.media.core.network.asResult
import cn.thecover.media.feature.basis.HomeApi
import cn.thecover.media.feature.basis.message.MessageApi
import cn.thecover.media.feature.basis.message.MessageType
import cn.thecover.media.feature.basis.message.data.MessageDataListState
import cn.thecover.media.feature.basis.message.data.entity.MessageListRequest
import cn.thecover.media.feature.basis.message.data.entity.ReadMessageRequest
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
    val savedStateHandle: SavedStateHandle,
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

    // 重置一次性 UI 状态
    fun resetOneTimeUiState() {
        _oneTimeUiState.value = OneTimeUiState()
    }


    fun handleMessageIntent(messageIntent: MessageIntent) {
        when (messageIntent) {
            is MessageIntent.FetchMessageList -> {
                _messageListState.update {
                    if (messageIntent.loadMore)
                        it.copy(isLoading = true)
                    else
                        it.copy(isRefreshing = true)
                }

                getMessageList(lastId = if (messageIntent.loadMore) messageListState.value.messageDataList.lastOrNull()?.id else null)
            }

            is MessageIntent.UpdateMessageFilter -> {
                _messageListState.update {
                    it.copy(isRefreshing = true)
                }
                currentMessageType = MessageType.entries[messageIntent.type]
                getMessageList()
            }

            is MessageIntent.ReadMessage -> {
                viewModelScope.launch {
                    flow {
                        val apiService = retrofit.get().create(MessageApi::class.java)
                        val response = apiService.readMessage(
                            readMessageRequest = ReadMessageRequest(
                                id = messageIntent.id
                            )
                        )
                        emit(response)
                    }.asResult()
                        .collect { result ->
                            if (result.status == HttpStatus.SUCCESS) {
                                _messageListState.update { state ->
                                    state.copy(messageDataList = state.messageDataList.map {
                                        if (it.id == messageIntent.id)
                                            it.copy(read = true)
                                        else
                                            it
                                    })
                                }
                            }
                        }
                }
            }

            is MessageIntent.GetUnreadMessageCount -> {
                viewModelScope.launch {
                    flow {
                        val apiService = retrofit.get().create(MessageApi::class.java)
                        val response = apiService.getUnreadMessageCount()
                        emit(response)
                    }.asResult()
                        .collect { result ->
                            if (result.status == HttpStatus.SUCCESS) {
                                _oneTimeUiState.update {
                                    it.copy(unreadMessageCount = result.data ?: 0)
                                }
                            }
                        }
                }
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
                                oldPassword = DESUtil.simpleEncrypt(mineIntent.oldPassword),
                                password = DESUtil.simpleEncrypt(mineIntent.password),
                                passwordVerify = DESUtil.simpleEncrypt(mineIntent.passwordVerify)
                            )
                        )
                        emit(response)
                    }.asResult()
                        .collect { result ->
                            if (result.status == HttpStatus.SUCCESS) {
                                _oneTimeUiState.update {
                                    OneTimeUiState(
                                        toastMessage = "修改密码成功",
                                        shouldNavigateToLogin = true
                                    )
                                }
                                logout()
                            } else {
                                _oneTimeUiState.update { OneTimeUiState(toastMessage = result.errorMsg) }
                            }
                        }
                }
            }
        }
    }

    fun getMessageList(
        type: Int = currentMessageType.ordinal,
        lastId: Long? = null,
        pageSize: Int = 20
    ) {
        viewModelScope.launch {
            flow {
                val apiService = retrofit.get().create(MessageApi::class.java)
                val result = apiService.getMessageList(
                    MessageListRequest(
                        pageSize = pageSize,
                        lastId = lastId,
                        type = if (type == 0) null else type.toString(),
                        allUser = 2,
                    )
                )
                emit(result)
            }.asResult()
                .collect { result ->
                    if (result.status == HttpStatus.SUCCESS) {
                        val resultData = result.data?.dataList ?: emptyList()
                        _messageListState.update {
                            it.copy(
                                messageDataList = if (lastId == null) resultData else it.messageDataList + resultData,
                                isLoading = false,
                                isRefreshing = false,
                                currentPage = result.data?.currentPage ?: 1,
                                canLoadMore = result.data?.lastId != -1L,
                                lastId = result.data?.lastId,
                                timeMillis = System.currentTimeMillis()
                            )
                        }
                    } else {
                        _messageListState.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false
                            )
                        }
                        _oneTimeUiState.update { OneTimeUiState(toastMessage = result.errorMsg) }
                    }
                }
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
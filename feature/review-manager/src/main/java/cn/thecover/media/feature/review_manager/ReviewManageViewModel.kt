package cn.thecover.media.feature.review_manager

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.thecover.media.core.network.HttpStatus
import cn.thecover.media.core.network.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
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
class ReviewManageViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle, val retrofit: dagger.Lazy<Retrofit>
) : ViewModel() {
    private val pageSize = 10

    // 稿件列表数据
    private val _archiveListDataState = MutableStateFlow(ArchiveListUiState())
    val archiveListDataState: StateFlow<ArchiveListUiState> = _archiveListDataState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ArchiveListUiState(),
    )

    private val _unreadMessageCount = MutableStateFlow(0)
    val unreadMessageCount: StateFlow<Int> = _unreadMessageCount

    fun getArchiveList(isRefresh: Boolean = true, page: Int = 0) {
        viewModelScope.launch {
            flow {
                val apiService = retrofit.get().create(ReviewManagerApi::class.java)
                val list = apiService.getArchiveList(page)
                emit(list)
            }.asResult().collect { result ->
                    when (result.status) {
                        HttpStatus.SUCCESS -> {
                            val data = result.data?.dataList ?: emptyList()
                            _archiveListDataState.update {
                                it.copy(
                                    list = if (isRefresh) data else it.list + data,
                                    isLoading = false,
                                    isRefreshing = false,
                                    canLoadMore = data.size >= pageSize,
                                    msg = null
                                )
                            }
                        }

                        HttpStatus.LOADING -> {
                            _archiveListDataState.update {
                                if (isRefresh) it.copy(isRefreshing = true) else it.copy(
                                    isLoading = true
                                )
                            }
                        }

                        HttpStatus.ERROR -> {
                            _archiveListDataState.update {
                                it.copy(
                                    isLoading = false,
                                    isRefreshing = false,
                                    canLoadMore = true,
                                    msg = result.errorMsg
                                )
                            }
                        }

                        else -> {}
                    }
                }
        }
    }

    fun getSearchData(isRefresh: Boolean = true, page: Int = 0, keyword: String) {
        viewModelScope.launch {
            flow {
                val apiService = retrofit.get().create(ReviewManagerApi::class.java)
                val list = apiService.searchArchiveList(page, keyword)
                emit(list)
            }.asResult().collect { result ->
                when (result.status) {
                    HttpStatus.SUCCESS -> {
                        val data = result.data?.dataList ?: emptyList()
                        _archiveListDataState.update {
                            it.copy(
                                list = if (isRefresh) data else it.list + data,
                                isLoading = false,
                                isRefreshing = false,
                                canLoadMore = data.size >= pageSize,
                                msg = null
                            )
                        }
                    }

                    HttpStatus.LOADING -> {
                        _archiveListDataState.update {
                            if (isRefresh) it.copy(isRefreshing = true) else it.copy(
                                isLoading = true
                            )
                        }
                    }

                    HttpStatus.ERROR -> {
                        _archiveListDataState.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                canLoadMore = true,
                                msg = result.errorMsg
                            )
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    fun getUnreadMessageCount() {
        viewModelScope.launch {
            flow {
                val apiService = retrofit.get().create(ReviewManagerApi::class.java)
                val response = apiService.getUnreadMessageCount()
                emit(response)
            }.asResult()
                .collect { result ->
                    if (result.status == HttpStatus.SUCCESS) {
                        _unreadMessageCount.update { result.data ?: 0 }
                    } else {
                        _unreadMessageCount.update { 0 }
                    }
                }
        }
    }
}

data class ArchiveListUiState(
    val list: List<ArchiveListData> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val canLoadMore: Boolean = true,
    val msg: String? = null
)

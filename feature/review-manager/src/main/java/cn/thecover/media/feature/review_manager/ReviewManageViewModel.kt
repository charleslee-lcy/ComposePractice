package cn.thecover.media.feature.review_manager

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.thecover.media.core.common.util.formatToDateString
import cn.thecover.media.core.common.util.toMillisecond
import cn.thecover.media.core.data.ArchiveListData
import cn.thecover.media.core.data.ScoreArchiveListRequest
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
import java.time.LocalDate
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
    private val apiService = retrofit.get().create(ReviewManagerApi::class.java)
    var pageType by mutableIntStateOf(ReviewManageType.ARCHIVE_SCORE.index)
    // ======================================== 稿件打分 ==========================================
    // 开始时间
    val startDateText = mutableStateOf("开始时间")
    var startLocalDate by mutableStateOf(LocalDate.now())
    // 结束时间
    val endDateText = mutableStateOf("结束时间")
    var endLocalDate by mutableStateOf(LocalDate.now())
    // 本人打分状态
    val userScoreStatus = mutableIntStateOf(0)
    // 稿件打分状态
    val newsScoreStatus = mutableIntStateOf(0)
    // 搜索类型
    val searchType = mutableIntStateOf(2)
    // 搜索关键词
    val searchKeyword = mutableStateOf("")
    private val pageSize = 10
    var lastId: Long? = null
    // 稿件列表数据
    private val _archiveListDataState = MutableStateFlow(ArchiveListUiState())
    val archiveListDataState: StateFlow<ArchiveListUiState> = _archiveListDataState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ArchiveListUiState(),
    )
    private val archiveRequest = ScoreArchiveListRequest()
    // ======================================== 稿件打分 ==========================================

    private val _unreadMessageCount = MutableStateFlow(0)
    val unreadMessageCount: StateFlow<Int> = _unreadMessageCount

    fun getArchiveList(isRefresh: Boolean = true, request: ScoreArchiveListRequest = archiveRequest) {
        if (isRefresh) {
            request.lastId = null
        } else {
            archiveRequest.lastId = lastId
        }
        archiveRequest.startPublishDate = if (startDateText.value == "开始时间") "" else startLocalDate.toMillisecond().formatToDateString("yyyy-MM-dd")
        archiveRequest.endPublishDate = if (endDateText.value == "结束时间") "" else endLocalDate.toMillisecond().formatToDateString("yyyy-MM-dd")
        when(userScoreStatus.intValue) {
            1 -> { archiveRequest.userScoreStatus = "0" }
            2 -> { archiveRequest.userScoreStatus = "1" }
            else -> {}
        }
        when(newsScoreStatus.intValue) {
            1 -> { archiveRequest.newsScoreStatus = "0" }
            2 -> { archiveRequest.newsScoreStatus = "1" }
            else -> {}
        }
        when(searchType.intValue) {
            0 -> { archiveRequest.searchType = 1 }
            1 -> { archiveRequest.searchType = 2 }
            else -> { archiveRequest.searchType = 3 }
        }
        archiveRequest.searchKeyword = searchKeyword.value.ifEmpty { null }
        viewModelScope.launch {
            flow {
                request.pageSize = pageSize
                val list = apiService.getScoreArchiveList(request)
                emit(list)
            }.asResult().collect { result ->
                    when (result.status) {
                        HttpStatus.SUCCESS -> {
                            val data = result.data?.dataList ?: emptyList()
                            lastId = result.data?.lastId ?: -1
                            _archiveListDataState.update {
                                it.copy(
                                    list = if (isRefresh) data else it.list + data,
                                    isLoading = false,
                                    isRefreshing = false,
                                    canLoadMore = lastId?.let {id ->
                                        id > 0
                                    } ?: kotlin.run {
                                        false
                                    },
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

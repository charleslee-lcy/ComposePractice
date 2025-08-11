package cn.thecover.media.feature.review_manager

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.thecover.media.core.network.HttpResult
import cn.thecover.media.core.network.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import retrofit2.Retrofit
import javax.inject.Inject

/**
 *
 * <p> Created by CharlesLee on 2025/7/28
 * 15708478830@163.com
 */
@HiltViewModel
class ReviewManageViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    val retrofit: dagger.Lazy<Retrofit>
) : ViewModel() {

    fun getArchiveList(page: Int = 0): StateFlow<ArchiveListUiState> = flow {
        val apiService = retrofit.get().create(ReviewManagerApi::class.java)
        val list = apiService.getArchiveList(page)
        emit(list)
    }.asResult()
        .map { result ->
            when (result) {
                is HttpResult.Success -> ArchiveListUiState.Success(result.data.data.datas)
                is HttpResult.Loading -> ArchiveListUiState.Loading
                is HttpResult.Error -> ArchiveListUiState.Error
            }
        }
        .onStart {
            emit(ArchiveListUiState.Loading)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ArchiveListUiState.Loading,
        )
}

sealed interface ArchiveListUiState {
    data class Success(val list: List<ArchiveListData>) : ArchiveListUiState
    data object Error : ArchiveListUiState
    data object Loading : ArchiveListUiState
}

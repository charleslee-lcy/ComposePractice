package cn.thecover.media.feature.review_data.data.params

/**
 *  Created by Wing at 15:10 on 2025/8/21
 *
 */

sealed class RepositoryResult<out T> {
    data class Success<out T>(val data: T) : RepositoryResult<T>()
    data class Error(val exception: Exception) : RepositoryResult<Nothing>()
    object Loading : RepositoryResult<Nothing>()
}

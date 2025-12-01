package cn.thecover.media.core.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 *  Created by Wing at 15:15 on 2025/8/21
 *
 */

data class PaginatedResult<T>(
    @SerializedName("list")
    val dataList: List<T> = emptyList<T>(),
    @SerializedName("page")
    val currentPage: Int = 0,
    @SerializedName("pages")
    val totalPages: Int = 0,
    // 分页记录数
    val pageSize: Int = 0,
    // 总记录数
    val total: Int = 0,
    // 当前页记录数
    val size: Int = 0,
    val hasNextPage: Boolean = false,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
): Serializable


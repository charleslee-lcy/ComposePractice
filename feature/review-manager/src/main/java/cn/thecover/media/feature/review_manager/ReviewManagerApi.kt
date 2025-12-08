package cn.thecover.media.feature.review_manager

import cn.thecover.media.core.data.NetworkResponse

import cn.thecover.media.core.data.PaginatedResult
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


/**
 *
 * <p> Created by CharlesLee on 2025/7/23
 * 15708478830@163.com
 */
interface ReviewManagerApi {
    @GET(value = "article/list/{page}/json?page_size=20")
    suspend fun getArchiveList(@Path("page") page: Int = 0): NetworkResponse<PaginatedResult<ArchiveListData>>

    @POST(value = "article/query/{page}/json")
    suspend fun searchArchiveList(@Path("page") page: Int = 0, @Query("k") keyword: String): NetworkResponse<PaginatedResult<ArchiveListData>>

    //获取未读消息数
    @GET(value = "mgr/unReadNotificationCount")
    suspend fun getUnreadMessageCount(): NetworkResponse<Int>
}
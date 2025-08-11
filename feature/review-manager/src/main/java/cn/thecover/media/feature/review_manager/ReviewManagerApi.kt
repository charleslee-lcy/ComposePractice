package cn.thecover.media.feature.review_manager

import cn.thecover.media.core.data.NetworkResponse
import cn.thecover.media.core.data.PageData
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
    suspend fun getArchiveList(@Path("page") page: Int = 0): NetworkResponse<PageData<ArchiveListData>>

    @POST(value = "article/query/{page}/json")
    suspend fun searchArchiveList(@Path("page") page: Int = 0, @Query("k") key: String): NetworkResponse<PageData<ArchiveListData>>
}
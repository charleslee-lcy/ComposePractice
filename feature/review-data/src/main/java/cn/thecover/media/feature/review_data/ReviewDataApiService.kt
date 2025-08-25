package cn.thecover.media.feature.review_data

import cn.thecover.media.core.data.NetworkResponse
import cn.thecover.media.core.data.PageData
import cn.thecover.media.feature.review_data.data.entity.DepartmentTotalDataEntity
import cn.thecover.media.feature.review_data.data.entity.ManuscriptReviewDataEntity
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 *  Created by Wing at 14:51 on 2025/8/21
 *
 */

interface ReviewDataApiService {
    @GET(value = "article/list/{page}/json?page_size=20")
    suspend fun getManuscriptReviewData(@Path("page") page: Int = 0): NetworkResponse<PageData<ManuscriptReviewDataEntity>>

    @POST(value = "article/query/{page}/json")
    suspend fun getDepartmentReviewData(@Path("page") page: Int = 0, @Query("k") key: String): NetworkResponse<PageData<DepartmentTotalDataEntity>>
}
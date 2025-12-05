package cn.thecover.media.feature.basis.mine

import cn.thecover.media.core.data.NetworkResponse
import cn.thecover.media.feature.basis.mine.data.requestParams.ModifyPasswordRequest
import retrofit2.http.Body
import retrofit2.http.POST

/**
 *  Created by Wing at 09:18 on 2025/12/5
 *
 */

interface MineApi {

    @POST(value = "api/user/helpCenterUrl")
    suspend fun getHelpCenterUrl(): NetworkResponse<String?>

    @POST(value = "api/user/updatePassword")
    suspend fun modifyPassword(@Body requestData: ModifyPasswordRequest): NetworkResponse<Nothing?>
}
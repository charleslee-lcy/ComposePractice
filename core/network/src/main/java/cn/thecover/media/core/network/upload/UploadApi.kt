package cn.thecover.media.core.network.upload

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by scott on 2023/2/10
 * Description:
 **/
interface UploadApi {
    @Multipart
    @POST("{url}")
    fun uploadFile(
        @Path("url") url: String,
        @QueryMap param: MutableMap<String, String>,
        @Part("description") description: RequestBody,//记录进度用的
        @Part file: MultipartBody.Part?
    ): Call<ResponseBody>

    @Multipart
    @POST("{url}")
    fun uploadFiles(
        @Path("url") url: String,
        @QueryMap param: MutableMap<String, String>,
        @Part parts: List<MultipartBody.Part>
    ): Call<ResponseBody>
}
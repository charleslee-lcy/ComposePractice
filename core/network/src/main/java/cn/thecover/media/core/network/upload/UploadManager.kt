package cn.thecover.media.core.network.upload

import cn.thecover.media.core.data.NetworkResponse
import cn.thecover.media.core.network.HttpManager
import cn.thecover.media.core.network.ThreadHelper
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import kotlin.jvm.java

/**
 * Created by scott on 2023/2/11
 * Description:
 **/
object UploadManager {

    /**
     * 单个文件上传 有进度回调
     */
    fun <T> uploadFile(url: String, file: File, param: MutableMap<String, String>, listener: UploadListener<NetworkResponse<T>>?) {
        listener?.let { l->
            ThreadHelper.postOnUIThread{
                l.onStart()
            }
        }

        val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val bodyPart: MultipartBody.Part = MultipartBody.Part.createFormData("file", file.name, requestBody)
        val callback: UploadCallBack<ResponseBody> = object : UploadCallBack<ResponseBody>(){
            override fun onSuccess(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                listener?.let { l ->
                    ThreadHelper.postOnUIThread{
                        if (response.code() == HttpManager.STATUS_SUCCESS_200) {
                            response.body()?.run {
                                val result = Gson().fromJson<NetworkResponse<T>>(string(), NetworkResponse::class.java)
                                if (result.status == HttpManager.STATUS_SUCCESS) {
                                    l.onSuccess(result)
                                } else {
                                    l.onFailed(result.status, Throwable(result.message))
                                }
                            } ?: let {
                                l.onFailed(response.code(), Throwable(response.message()))
                            }
                        } else {
                            l.onFailed(response.code(), Throwable(response.message()))
                        }
                    }
                }
            }

            override fun onProgress(total: Long, progress: Long) {
                listener?.let { l ->
                    ThreadHelper.postOnUIThread{
                        l.onProgress(total, progress)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                listener?.let { l ->
                    ThreadHelper.postOnUIThread{
                        l.onFailed(-1, t)
                    }
                }
            }

        }
        val body = FileRequestBody(requestBody, callback)
        HttpManager.retrofit().create(UploadApi::class.java)
            .uploadFile(url, param, body, bodyPart)
            .enqueue(callback)
    }

    /**
     * 多个文件上传，没有进度回调
     */
    fun <T> uploadFiles(url: String, files: MutableList<File>, param: MutableMap<String, String>, listener: UploadListener<NetworkResponse<T>>?) {
        listener?.let { l->
            ThreadHelper.postOnUIThread{
                l.onStart()
            }
        }

        val partList = mutableListOf<MultipartBody.Part>()
        files.indices.forEach {
            partList.add(MultipartBody.Part.createFormData("file${it}", files[it].name,
                files[it].asRequestBody("multipart/form-data".toMediaTypeOrNull())))
        }

        val callback: UploadCallBack<ResponseBody> = object : UploadCallBack<ResponseBody>(){
            override fun onSuccess(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                listener?.let { l ->
                    ThreadHelper.postOnUIThread{
                        if (response.code() == HttpManager.STATUS_SUCCESS_200) {
                            response.body()?.run {
                                val result = Gson().fromJson<NetworkResponse<T>>(string(), NetworkResponse::class.java)
                                if (result.status == HttpManager.STATUS_SUCCESS) {
                                    l.onSuccess(result)
                                } else {
                                    l.onFailed(result.status, Throwable(result.message))
                                }
                            } ?: let {
                                l.onFailed(response.code(), Throwable(response.message()))
                            }
                        } else {
                            l.onFailed(response.code(), Throwable(response.message()))
                        }
                    }
                }
            }

            override fun onProgress(total: Long, progress: Long) {
                listener?.let { l ->
                    ThreadHelper.postOnUIThread{
                        l.onProgress(total, progress)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                listener?.let { l ->
                    ThreadHelper.postOnUIThread{
                        l.onFailed(-1, t)
                    }
                }
            }

        }
        HttpManager.retrofit().create(UploadApi::class.java)
            .uploadFiles(url, param, partList)
            .enqueue(callback)
    }
}
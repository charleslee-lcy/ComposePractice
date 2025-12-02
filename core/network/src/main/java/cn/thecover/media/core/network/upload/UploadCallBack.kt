package cn.thecover.media.core.network.upload

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by scott on 2023/2/10
 * Description:
 **/
abstract class UploadCallBack<T> : Callback<T>{
    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful) {
            onSuccess(call, response)
        } else {
            onFailure(call, Throwable(response.message()))
        }
    }

    abstract fun onSuccess(call: Call<T>, response: Response<T>)
    abstract fun onProgress(total: Long, progress: Long)
}
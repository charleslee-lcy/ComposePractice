package cn.thecover.media.core.network.upload

/**
 * Created by scott on 2023/2/10
 * Description:
 **/
interface UploadListener<T> {
    fun onStart() {

    }

    fun onProgress(total: Long, progress: Long)  {

    }

    fun onSuccess(data: T) {

    }

    fun onFailed(code: Int, desc: Throwable) {

    }

    fun onFinish() {

    }
}
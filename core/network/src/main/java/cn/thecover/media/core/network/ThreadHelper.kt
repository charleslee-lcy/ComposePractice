package cn.thecover.media.core.network

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by scott on 2023/2/10
 * Description:线程切换工具
 **/
object ThreadHelper {
    private val mainHandler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }
    private val workHandler: Handler by lazy {
        Handler(HandlerThread("workHandler").apply {
            start()
        }.looper)
    }
    private val executorService: ExecutorService by lazy {
        val cpuCount = Runtime.getRuntime().availableProcessors()
        ThreadPoolExecutor(cpuCount + 1,
            cpuCount * 2 + 1,
            10,
            TimeUnit.SECONDS,
            ArrayBlockingQueue(128), object : ThreadFactory{
                val count = AtomicInteger(1)
            override fun newThread(r: Runnable?): Thread {
                return Thread(r, "ThreadHelper#${count.getAndIncrement()}")
            }
        })
    }

    fun postOnUIThread(runnable: Runnable): Boolean {
        return mainHandler.post(runnable)
    }

    fun postDelayOnUIThread(runnable: Runnable, delayMillis: Long): Boolean {
        return mainHandler.postDelayed(runnable, delayMillis)
    }

    fun postAtTimeOnUIThread(runnable: Runnable, uptimeMillis: Long): Boolean {
        return mainHandler.postAtTime(runnable, uptimeMillis)
    }

    fun removeUICallBacks(runnable: Runnable) {
        mainHandler.removeCallbacks(runnable)
    }

    fun postOnWorkThread(runnable: Runnable): Boolean {
        return workHandler.post(runnable)
    }

    fun postDelayOnWorkThread(runnable: Runnable, delayMillis: Long): Boolean {
        return workHandler.postDelayed(runnable, delayMillis)
    }

    fun postAtTimeOnWorkThread(runnable: Runnable, uptimeMillis: Long): Boolean {
        return workHandler.postAtTime(runnable, uptimeMillis)
    }

    fun removeWorkCallBacks(runnable: Runnable) {
        workHandler.removeCallbacks(runnable)
    }

    fun execute(runnable: Runnable) {
        try {
            executorService.execute(runnable)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun <T> submit(task: Callable<T>): Future<T>? {
        return try {
            executorService.submit(task)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun shutdown() {
        try {
            if (!executorService.isShutdown) {
                executorService.shutdown()
            }
            workHandler.looper.quitSafely()
            workHandler.looper.thread.interrupt()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
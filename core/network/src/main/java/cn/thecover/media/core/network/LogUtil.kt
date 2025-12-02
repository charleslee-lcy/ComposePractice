package cn.thecover.media.core.network

import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy


/**
 * Desc: 日志打印类
 * @author CharlesLee
 * @date   2019-12-29 23:01
 * @email  15708478830@163.com
 **/
class LogUtil private constructor() {
	companion object {
		const val COVER_LOG = "COVER_LOG"

		/**
		 * 建议放到application里面去，然后debug才进行初始化
		 */
		@JvmStatic fun init(loggable: Boolean) {
			val formatStrategy = PrettyFormatStrategy.newBuilder()
				.showThreadInfo(false) // 不显示线程信息
				.tag(COVER_LOG)
				.build()
			Logger.addLogAdapter(object: AndroidLogAdapter(formatStrategy) {
				override fun isLoggable(priority: Int, tag: String?): Boolean {
					// 控制日志是否打印
					return loggable
				}
			})
		}
		@JvmStatic inline fun <reified T> print(
			msg : T,
			level : LogLevel = DEBUG,
			tag : String = ""
		) {
			Logger.t(tag).let {
				if (msg is Throwable) {
					it.e(msg as Throwable, (msg as Throwable).message ?: "")
					return
				}
				when (level) {VERBOSE -> it.v(msg.toString())
					INFO -> it.i(msg.toString())
					DEBUG ->  it.d(msg.toString()) // 可打印任何对象，内部做了toString()处理
					WARN -> it.w(msg.toString())
					ERROR -> it.e(msg.toString())
					JSON -> it.json(msg.toString())
				}
			}
		}
	}

	sealed class LogLevel
	object VERBOSE : LogLevel()
	object INFO : LogLevel()
	object DEBUG : LogLevel()
	object WARN : LogLevel()
	object ERROR : LogLevel()
	object JSON : LogLevel()
}



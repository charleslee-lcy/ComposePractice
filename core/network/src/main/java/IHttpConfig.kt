import cn.thecover.media.core.network.HttpCoverInterceptor
import okhttp3.Interceptor
import java.util.UUID

/**
 *
 * @author TT
 * @since 2021-12-9
 */
internal const val KEY_USER_AGENT = "User-Agent"
internal const val KEY_TENANT_ID = "tenantId"
internal const val KEY_TOKEN = "token"
internal const val KEY_FMIO_TOKEN = "fmio_token"
internal const val KEY_FMIO_APP_ID = "fmio_appid"

internal const val KEY_CLIENT = "client"
internal const val KEY_DEVICE_ID = "deviceid"
internal const val KEY_TIMESTAMP = "timestamp"
internal const val KEY_SIGN = "sign"
internal const val KEY_CHANNEL = "channel"
internal const val KEY_VNO = "vno"
internal const val KEY_APP_VNO = "app_vno"
internal const val KEY_ACCOUNT = "account"
internal const val KEY_TEENAGER_MODE = "teen_mode"
internal const val KEY_RECOMMEND_MODE = "switch_suggest"

internal const val KEY_DATA = "data"
const val KEY_DATA_WITHOUT_TRANSFORM = "key_data_without_transform"

internal const val VALUE_USER_AGENT = "covermedia-android"
internal const val VALUE_CLIENT = "android"

interface IHttpConfig {
    // 有默认参数的
    val userAgent: String
        get() = VALUE_USER_AGENT

    val client: String
        get() = VALUE_CLIENT
//    val deviceId: String
//        get() = IdManagerFactory.getDeviceId(ContextProvider.appContext) ?: ""
//    val channel: String
//        get() = ChannelUtil.readChannelFromAssets(ContextProvider.appContext)
    val fmioToken: String
        get() = ""
    val fmioAppId: String
        get() = ""

    // url
    val baseUrl: String

    // 运行内不可改变的参数
    val tenantId: String

    val vno: String
    val appVno: String

    val coverInterceptor: Interceptor
        get() = HttpCoverInterceptor()

    // 运行内可改变的参数
    fun token(): String
    fun account(): String = UUID.randomUUID().toString()
//    fun sign(account: String, timeStamp: String): String? = SignFactory.getSign(account, token(), timeStamp)
    // 青少年模式
//    fun teenagerMode() : String = getTeenagerMode()
    // 个性化开关
//    fun recommendMode(): String = getRecommendMode()

    /**
     * 额外的header，
     * 如果还需要定制化，在后面加，不要直接new
     * val map = super.extraHeaderMap(); map[] = ""; return map
     */
    fun extraHeaderMap(): MutableMap<String, String> {
        return mutableMapOf<String, String>().apply {
            this[KEY_USER_AGENT] = userAgent
            this[KEY_TENANT_ID] = tenantId
            this[KEY_TOKEN] = token()
            this[KEY_FMIO_TOKEN] = fmioToken
            this[KEY_FMIO_APP_ID] = fmioAppId
        }
    }

    /**
     * 额外的参数，
     * 如果还需要定制化，在后面加，不要直接new
     * val map = super.extraParamMap(); map[] = ""; return map
     */
    fun extraParamMap(): MutableMap<String, String> {
        return mutableMapOf<String, String>().apply {
            val timeStamp = currentTimeStamp()
            val account = account()

            this[KEY_CLIENT] = client
//            this[KEY_DEVICE_ID] = deviceId
            this[KEY_TIMESTAMP] = timeStamp
//            this[KEY_SIGN] = sign(account, timeStamp) ?: ""
//            this[KEY_CHANNEL] = channel
            this[KEY_VNO] = vno
            this[KEY_APP_VNO] = appVno
            this[KEY_TOKEN] = token()
            this[KEY_ACCOUNT] = account
//            this[KEY_TEENAGER_MODE] = teenagerMode()
//            this[KEY_RECOMMEND_MODE] = recommendMode()
        }
    }

    // 状态开关，与请求参数无关
    fun enableProxy(): Boolean = false
    fun enablePreLog(): Boolean = false
    fun enableRealLog(): Boolean = false
    fun enableRecord(): Boolean = false

    fun currentTimeStamp(): String {
        return System.currentTimeMillis().toString()
    }
}
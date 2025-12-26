package cn.thecover.media.core.network


/**
 *
 * <p> Created by CharlesLee on 2025/7/25
 * 15708478830@163.com
 */
object URLConstant {
    val YB_BASE_URL = when(BuildConfig.ENV_CONFIG) {
        1 -> "https://test-ynrb-api.yndaily.com/pms-api/"  //测试
        2 -> "https://m-kaohe.yndaily.com:7080/"  //正式
        else -> ""
    }
}
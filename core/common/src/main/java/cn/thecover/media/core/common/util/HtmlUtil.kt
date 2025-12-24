package cn.thecover.media.core.common.util


/**
 *
 * <p> Created by CharlesLee on 2025/12/23
 * 15708478830@163.com
 */
fun attachMediaForHtmlData(htmlData: String, videoUrl: String? = null, audioUrl: String? = null): String {
    val videoStr = if (!videoUrl.isNullOrEmpty()) {
        val videoType = when {
            videoUrl.endsWith(".mp4", ignoreCase = true) -> "video/mp4"
            videoUrl.endsWith(".webm", ignoreCase = true) -> "video/webm"
            videoUrl.endsWith(".ogg", ignoreCase = true) -> "video/ogg"
            else -> "video/mp4" // 默认使用mp4
        }
        """
        <video 
            width="100%" 
            height="auto" 
            controls 
            preload="metadata"
            playsinline
            webkit-playsinline>
            <source src="$videoUrl" type="$videoType">
            <p>当前不支持视频播放，请<a href="$videoUrl">点击下载</a>视频文件。</p>
        </video>
    """.trimIndent()
    } else {
        ""
    }
    val audioStr = if (!audioUrl.isNullOrEmpty()) {
        // 根据URL确定音频类型
        val audioType = when {
            audioUrl.endsWith(".mp3", ignoreCase = true) -> "audio/mpeg"
            audioUrl.endsWith(".wav", ignoreCase = true) -> "audio/wav"
            audioUrl.endsWith(".ogg", ignoreCase = true) -> "audio/ogg"
            audioUrl.endsWith(".aac", ignoreCase = true) -> "audio/aac"
            else -> "audio/mpeg" // 默认类型
        }
        """
        <audio controls preload="metadata" style="width: 100%;">
            <source src="$audioUrl" type="$audioType">
            <p>当前不支持音频播放，请<a href="$audioUrl">点击下载</a>音频文件。</p>
        </audio>
        """.trimIndent()
    } else {
        ""
    }
    val resultData = StringBuilder()
    if (!videoUrl.isNullOrEmpty()) {
        resultData.append(videoStr)
    }
    if (!audioUrl.isNullOrEmpty()) {
        resultData.append(audioStr)
    }
    resultData.append(htmlData)
    return resultData.toString()
}
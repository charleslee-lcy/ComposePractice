package cn.thecover.media.feature.basis.mine.data

/**
 *  Created by Wing at 09:28 on 2025/12/5
 *
 */

data class OneTimeUiState(
    val toastMessage: String? = null,
    val dialogMessage: String? = null,
    val successMessage: String? = null,
    val unreadMessageCount: Int = 0,
    val time: Long = System.currentTimeMillis()
)

package cn.thecover.media.feature.review_data.basic_widget

import cn.thecover.media.core.widget.theme.MsgColor
import cn.thecover.media.core.widget.theme.SecondaryAuxiliaryColor
import cn.thecover.media.core.widget.theme.TertiaryAuxiliaryColor
import cn.thecover.media.core.widget.theme.TertiaryTextColor

/**
 *  Created by Wing at 14:52 on 2025/8/5
 *  考核数据内部的一些扩展方法
 */


/**
 * 排名序号颜色选择器，统一管理
 */
internal fun Int.chooseRankingColor() = when (this) {
    1 -> MsgColor
    2 -> SecondaryAuxiliaryColor
    3 -> TertiaryAuxiliaryColor
    else -> TertiaryTextColor
}
package cn.thecover.media.feature.basis.mine.modifypassword

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import cn.thecover.media.feature.basis.mine.MineScreen
import cn.thecover.media.feature.basis.mine.MineViewModel

/**
 *  Created by Wing at 11:21 on 2025/7/29
 *  修改密码页面
 */

@Composable
internal fun ModifyPasswordRoute(
    modifier: Modifier = Modifier,
    viewModel: MineViewModel = hiltViewModel()
) {
    ModifyPasswordScreen()
}
@Composable
fun ModifyPasswordScreen() {

}
 
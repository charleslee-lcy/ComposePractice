package cn.thecover.media.feature.basis.login

import android.R.attr.onClick
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.navOptions
import cn.thecover.media.core.widget.component.YBButton
import cn.thecover.media.core.widget.component.YBImage
import cn.thecover.media.core.widget.component.YBTopAppBar
import cn.thecover.media.core.widget.theme.MainColor
import cn.thecover.media.feature.basis.R
import cn.thecover.media.feature.basis.home.HomeViewModel
import cn.thecover.media.feature.basis.home.navigation.LoginRoute
import cn.thecover.media.feature.basis.home.navigation.navigateToHome
import com.google.samples.apps.nowinandroid.core.designsystem.theme.YBTheme


/**
 *
 * <p> Created by CharlesLee on 2025/7/28
 * 15708478830@163.com
 */
@Composable
fun LoginRoute(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
) {
//    val feedState by viewModel.collectAsStateWithLifecycle()
    LoginScreen(modifier, navController)
}


@Composable
internal fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val userNameState = rememberTextFieldState("")
    val passwordState = rememberTextFieldState("")

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        YBTopAppBar(
            title = stringResource(id = R.string.login_title),
            titleColor = Color.White,
            backgroundColor = MainColor,
        )
        Spacer(Modifier.size(20.dp))
        YBImage(
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = "云报绩效",
            fontSize = 18.sp,
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 10.dp)
        )
        Spacer(Modifier.size(20.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "用户名：",
                modifier = Modifier.padding(start = 20.dp)
            )
            TextField(
                state = userNameState,
                lineLimits = TextFieldLineLimits.MultiLine(maxHeightInLines = 1),
                placeholder = { Text("请输入用户名") },
                textStyle = TextStyle(color = Color(0xFF333333)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                ),
                modifier = Modifier.wrapContentHeight()
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "密    码：",
                modifier = Modifier.padding(start = 20.dp)
            )
            TextField(
                state = passwordState,
                lineLimits = TextFieldLineLimits.MultiLine(maxHeightInLines = 1),
                placeholder = { Text("请输入密码") },
                textStyle = TextStyle(color = Color(0xFF333333)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                ),
                modifier = Modifier.wrapContentHeight()
            )
        }
        Spacer(Modifier.size(20.dp))
        YBButton(
            text = { Text("登  录", fontSize = 17.sp) },
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .height(45.dp),
            onClick = {
                navController.navigateToHome(navOptions {
                    // 清除所有之前的页面
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                    launchSingleTop = true

//                    anim {
//                        enter = R.anim.slide_in_right
//                        exit = R.anim.slide_out_left
//                        popEnter = enter
//                        popExit = exit
//                    }
                })
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    YBTheme {
        LoginScreen(navController = NavController(LocalContext.current))
    }
}
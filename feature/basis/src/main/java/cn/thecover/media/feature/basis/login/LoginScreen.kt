package cn.thecover.media.feature.basis.login

import android.R.attr.enabled
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarDuration.Indefinite
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.navOptions
import cn.thecover.media.core.widget.component.YBButton
import cn.thecover.media.core.widget.component.YBImage
import cn.thecover.media.core.widget.component.YBToast
import cn.thecover.media.core.widget.component.showToast
import cn.thecover.media.core.widget.event.clickableWithoutRipple
import cn.thecover.media.core.widget.theme.DividerColor
import cn.thecover.media.core.widget.theme.HintTextColor
import cn.thecover.media.core.widget.theme.MainColor
import cn.thecover.media.core.widget.ui.ComponentVisibility
import cn.thecover.media.core.widget.ui.Visibility
import cn.thecover.media.feature.basis.R
import cn.thecover.media.feature.basis.home.HomeViewModel
import cn.thecover.media.feature.basis.home.navigation.navigateToHome
import com.google.samples.apps.nowinandroid.core.designsystem.theme.YBTheme
import kotlinx.coroutines.launch


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
    val loginScreenScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 60.dp)
            .statusBarsPadding()
    ) {
        Spacer(Modifier.size(50.dp))
        YBImage(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            placeholder = painterResource(R.mipmap.img_login_logo)
        )
        Spacer(Modifier.size(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            ComponentVisibility(visibility = Visibility.Gone) {
                Text(
                    text = "用户名：",
                    modifier = Modifier.padding(start = 20.dp),
                )
            }

            TextField(
                state = userNameState,
                lineLimits = TextFieldLineLimits.MultiLine(maxHeightInLines = 1),
                placeholder = {
                    Text(
                        text = "请输入用户名",
                        color = HintTextColor,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(bottom = 1.dp)
                    )
                },
                textStyle = TextStyle(color = Color(0xFF333333), fontSize = 15.sp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = MainColor,
                    unfocusedIndicatorColor = DividerColor
                ),
                contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(
                    start = 0.dp, end = 0.dp
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            ComponentVisibility(visibility = Visibility.Gone) {
                Text(
                    text = "密    码：",
                    modifier = Modifier.padding(start = 20.dp)
                )
            }
            TextField(
                state = passwordState,
                lineLimits = TextFieldLineLimits.MultiLine(maxHeightInLines = 1),
                placeholder = {
                    Text(
                        text = "请输入密码",
                        color = HintTextColor,
                        fontSize = 15.sp
                    )
                },
                textStyle = TextStyle(color = Color(0xFF333333), fontSize = 15.sp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = MainColor,
                    unfocusedIndicatorColor = DividerColor
                ),
                contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(
                    start = 0.dp, end = 0.dp
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
        YBButton(
            text = { Text("登录", fontSize = 16.sp, fontWeight = FontWeight.SemiBold) },
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(top = 40.dp, bottom = 10.dp)
                .height(44.dp),
            shape = RoundedCornerShape(2.dp),
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

        Text(
            text = "找回密码",
            fontSize = 15.sp,
            color = MainColor,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickableWithoutRipple {
                    loginScreenScope.launch {
                        snackBarHostState.showToast("跳转到找回密码页面")
                    }
                }
                .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)
        )
    }

    YBToast(snackBarHostState = snackBarHostState)
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    YBTheme {
        LoginScreen(navController = NavController(LocalContext.current))
    }
}
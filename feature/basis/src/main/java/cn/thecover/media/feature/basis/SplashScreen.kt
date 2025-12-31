package cn.thecover.media.feature.basis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.navOptions
import cn.thecover.media.core.network.previewRetrofit
import cn.thecover.media.core.widget.component.CommonImage
import cn.thecover.media.core.widget.datastore.Keys
import cn.thecover.media.core.widget.datastore.rememberDataStoreState
import cn.thecover.media.core.widget.theme.PageBackgroundColor
import cn.thecover.media.core.widget.theme.CommonTheme
import cn.thecover.media.feature.basis.home.HomeViewModel
import cn.thecover.media.feature.basis.home.navigation.navigateToHome
import cn.thecover.media.feature.basis.home.navigation.navigateToLogin
import dagger.Lazy
import kotlinx.coroutines.delay


/**
 *
 * <p> Created by CharlesLee on 2025/7/28
 * 15708478830@163.com
 */
@Composable
fun SplashRoute(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    CommonTheme {
        SplashScreen(modifier, navController)
    }
}


@Composable
internal fun SplashScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val userToken = rememberDataStoreState(Keys.USER_TOKEN, "")

    LaunchedEffect(userToken) {
        userToken?.apply {
            delay(2000)
            if (this.isEmpty()) {
                navController.navigateToLogin(
                    navOptions = navOptions {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    })
            } else {
                navController.navigateToHome(
                    navOptions = navOptions {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                )
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PageBackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(300.dp))
        CommonImage(placeholder = painterResource(R.mipmap.img_login_logo))
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    CommonTheme {
        SplashScreen(
            navController = NavController(LocalContext.current),
            viewModel = HomeViewModel(
                SavedStateHandle(),
                retrofit = Lazy { previewRetrofit }
            ))
    }
}
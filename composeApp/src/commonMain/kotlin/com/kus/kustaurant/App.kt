package com.kus.kustaurant

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.kus.feature.login.navigation.Login
import com.kus.feature.login.navigation.loginNavGraph
import com.kus.feature.onBoarding.navigatioin.Onboarding
import com.kus.feature.onBoarding.navigatioin.onboardingNavGraph
import com.kus.feature.splash.navigation.Splash
import com.kus.feature.splash.navigation.splashNavGraph
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        Surface {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = Splash
            ) {
                splashNavGraph(
                    navigateToOnboarding = {
                        navController.navigate(Onboarding) {
                            popUpTo(Splash) { inclusive = true }
                        }
                    },
                    navigateToLogin = {
                        navController.navigate(Login) {
                            popUpTo(Splash) { inclusive = true }
                        }
                    }
                )

                onboardingNavGraph(
                    navigateToLogin = {
                        navController.navigate(Login) {
                            popUpTo(Onboarding) { inclusive = true }
                        }
                    }
                )

                loginNavGraph(
                    navigateToHome = {
                        // TODO: Home 라우트 정의 후 여기서 이동
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun AppPreview() {
    App()
}
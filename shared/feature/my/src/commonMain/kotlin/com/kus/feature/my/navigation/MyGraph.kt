package com.kus.feature.my.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kus.feature.my.ui.subscreen.CheckedRestaurantScreen
import com.kus.feature.my.ui.subscreen.FavoriteRestaurantScreen
import com.kus.feature.my.ui.subscreen.FeedbackScreen
import com.kus.feature.my.ui.webview.MyPageWebViewScreen
import kotlinx.serialization.Serializable

fun NavController.navigateToMyWebView(
    title: String,
    url: String,
    navOptions: NavOptions? = null,
) = navigate(MyWebView(title, url), navOptions)

fun NavController.navigateToFeedback(navOptions: NavOptions? = null) =
    navigate(Feedback, navOptions)

fun NavController.navigateToFavoriteRest(navOptions: NavOptions? = null) =
    navigate(FavoriteRest, navOptions)

fun NavController.navigateToCheckedRest(navOptions: NavOptions? = null) =
    navigate(CheckedRest, navOptions)

fun NavController.navigateToMyArticle(navOptions: NavOptions? = null) =
    navigate(MyArticle, navOptions)

fun NavController.navigateToMyComment(navOptions: NavOptions? = null) =
    navigate(MyComment, navOptions)

fun NavController.navigateToScrap(navOptions: NavOptions? = null) =
    navigate(Scrap, navOptions)

fun NavGraphBuilder.myNavGraph(
    navigateToUp: () -> Unit,
    onShowMessage: (String) -> Unit,
    navController: NavController,
) {
    composable<My> {
        MyRoute(
            navigateToProfileEdit = { },
            navigateToNotice = {
                navController.navigateToMyWebView(
                    title = "공지사항",
                    url = "https://kustaurant.com/notice",
                )
            },
            navigateToTerms = {
                navController.navigateToMyWebView(
                    title = "이용약관",
                    url = "https://kustaurant.com/terms_of_use",
                )
            },
            navigateToPrivacyPolicy = {
                navController.navigateToMyWebView(
                    title = "개인정보처리방침",
                    url = "https://kustaurant.com/privacy-policy",
                )
            },
            navigateToFeedback = navController::navigateToFeedback,
            navigateToSavedRest = navController::navigateToFavoriteRest,
            navigateToCheckedRest = navController::navigateToCheckedRest,
            navigateToMyArticle = navController::navigateToMyArticle,
            navigateToMyComment = navController::navigateToMyComment,
            navigateToScrap = navController::navigateToScrap,
        )
    }

    composable<MyWebView> { backStackEntry ->
        val args = backStackEntry.toRoute<MyWebView>()
        MyPageWebViewScreen(
            title = args.title,
            url = args.url,
            onBackClick = navigateToUp,
        )
    }

    composable<Feedback> { FeedbackScreen(onBackClick = navigateToUp) }
    composable<FavoriteRest> {
        FavoriteRestaurantScreen(
            onBackClick = navigateToUp,
            onItemClick = { }
        )
    }
    composable<CheckedRest> {
        CheckedRestaurantScreen(
            onBackClick = navigateToUp,
            onItemClick = {}
        )
    }
    composable<MyArticle> { /* MyArticleScreen(onBackClick = navigateToUp) */ }
    composable<MyComment> { /* MyCommentScreen(onBackClick = navigateToUp) */ }
    composable<Scrap> { /* ScrapScreen(onBackClick = navigateToUp) */ }
}

@Serializable
data object My

@Serializable
data class MyWebView(
    val title: String,
    val url: String,
)

@Serializable
data object Feedback

@Serializable
data object FavoriteRest

@Serializable
data object CheckedRest

@Serializable
data object MyArticle

@Serializable
data object MyComment

@Serializable
data object Scrap
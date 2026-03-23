package com.kus.feature.my.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kus.feature.my.ui.community.MyArticleScreen
import com.kus.feature.my.ui.community.MyCommentScreen
import com.kus.feature.my.ui.community.MyScrapScreen
import com.kus.feature.my.ui.editprofile.EditProfileRoute
import com.kus.feature.my.ui.restaurant.CheckedRestaurantScreen
import com.kus.feature.my.ui.restaurant.FavoriteRestaurantScreen
import com.kus.feature.my.ui.feedback.FeedbackScreen
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
    navigate(MyScrap, navOptions)

fun NavController.navigateToEditProfile(
    nickName: String,
    email: String,
    phoneNumber: String,
    navOptions: NavOptions? = null,
) = navigate(EditProfile(nickName, email, phoneNumber), navOptions)

fun NavGraphBuilder.myMainNavGraph(
    onShowMessage: (String) -> Unit,
    navigateToEditProfile: (String, String, String) -> Unit,
    navigateToNotice: () -> Unit,
    navigateToTerms: () -> Unit,
    navigateToPrivacyPolicy: () -> Unit,
    navigateToFeedback: () -> Unit,
    navigateToSavedRest: () -> Unit,
    navigateToCheckedRest: () -> Unit,
    navigateToMyArticle: () -> Unit,
    navigateToMyComment: () -> Unit,
    navigateToScrap: () -> Unit,
    navigateToLogin: () -> Unit,
) {
    composable<My> {
        MyRoute(
            onShowMessage = onShowMessage,
            navigateToProfileEdit = navigateToEditProfile,
            navigateToNotice = navigateToNotice,
            navigateToTerms = navigateToTerms,
            navigateToPrivacyPolicy = navigateToPrivacyPolicy,
            navigateToFeedback = navigateToFeedback,
            navigateToSavedRest = navigateToSavedRest,
            navigateToCheckedRest = navigateToCheckedRest,
            navigateToMyArticle = navigateToMyArticle,
            navigateToMyComment = navigateToMyComment,
            navigateToScrap = navigateToScrap,
            navigateToLogin = navigateToLogin,
        )
    }
}

fun NavGraphBuilder.myFullscreenNavGraph(
    navigateToUp: () -> Unit,
    onRestItemClick: (Long) -> Unit,
    onArticleClick: (Long) -> Unit,
    onShowMessage: (String) -> Unit,
) {
    composable<MyWebView> { backStackEntry ->
        val args = backStackEntry.toRoute<MyWebView>()
        MyPageWebViewScreen(
            title = args.title,
            url = args.url,
            onBackClick = navigateToUp,
        )
    }

    composable<Feedback> {
        FeedbackScreen(
            onBackClick = navigateToUp,
            onShowMessage = onShowMessage,
        )
    }

    composable<FavoriteRest> {
        FavoriteRestaurantScreen(
            onBackClick = navigateToUp,
            onItemClick = onRestItemClick,
        )
    }

    composable<CheckedRest> {
        CheckedRestaurantScreen(
            onBackClick = navigateToUp,
            onItemClick = onRestItemClick,
        )
    }

    composable<MyArticle> {
        MyArticleScreen(
            onBackClick = navigateToUp,
            onItemClick = onArticleClick,
        )
    }

    composable<MyComment> {
        MyCommentScreen(
            onBackClick = navigateToUp,
            onItemClick = onArticleClick,
        )
    }

    composable<MyScrap> {
        MyScrapScreen(
            onBackClick = navigateToUp,
            onItemClick = onArticleClick,
        )
    }

    composable<EditProfile> { backStackEntry ->
        val args = backStackEntry.toRoute<EditProfile>()

        EditProfileRoute(
            nickName = args.nickName,
            email = args.email,
            phoneNumber = args.phoneNumber,
            onShowMessage = onShowMessage,
            onBackClick = navigateToUp,
        )
    }
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
data object MyScrap

@Serializable
data class EditProfile(
    val nickName: String,
    val email: String,
    val phoneNumber: String,
)

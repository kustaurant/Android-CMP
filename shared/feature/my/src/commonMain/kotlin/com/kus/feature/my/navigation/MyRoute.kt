package com.kus.feature.my.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kus.feature.my.ui.MyScreen

@Composable
fun MyRoute(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    navigateToProfileEdit: () -> Unit,
    navigateToNotice: () -> Unit,
    navigateToTerms: () -> Unit,
    navigateToPrivacyPolicy: () -> Unit,
    navigateToFeedback: () -> Unit,
    navigateToSavedRest: () -> Unit,
    navigateToCheckedRest: () -> Unit,
    navigateToMyArticle: () -> Unit,
    navigateToMyComment: () -> Unit,
    navigateToScrap: () -> Unit,
) {
    MyScreen(
        modifier = modifier,
        onBackClick = onBackClick,
        onProfileEditNavigate = navigateToProfileEdit,
        onNoticeNavigate = navigateToNotice,
        onTermsNavigate = navigateToTerms,
        onPrivacyPolicyNavigate = navigateToPrivacyPolicy,
        onFeedbackNavigate = navigateToFeedback,
        onSavedRestNavigate = navigateToSavedRest,
        onCheckedRestNavigate = navigateToCheckedRest,
        onMyArticleNavigate = navigateToMyArticle,
        onMyCommentNavigate = navigateToMyComment,
        onScrapNavigate = navigateToScrap,
    )
}

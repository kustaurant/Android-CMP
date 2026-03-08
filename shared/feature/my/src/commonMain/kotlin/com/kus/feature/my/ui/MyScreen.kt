package com.kus.feature.my.ui

import UiState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kus.designsystem.component.LoginRequiredDialog
import com.kus.feature.my.component.MyActivityScreen
import com.kus.feature.my.component.MyProfileScreen
import com.kus.feature.my.component.MyTabRow
import com.kus.feature.my.ui.type.MyTab
import com.kus.shared.domain.model.my.MyInfo
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MyScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onProfileEditNavigate: () -> Unit,
    onNoticeNavigate: () -> Unit,
    onTermsNavigate: () -> Unit,
    onPrivacyPolicyNavigate: () -> Unit,
    onFeedbackNavigate: () -> Unit,
    onSavedRestNavigate: () -> Unit,
    onCheckedRestNavigate: () -> Unit,
    onMyArticleNavigate: () -> Unit,
    onMyCommentNavigate: () -> Unit,
    onScrapNavigate: () -> Unit,
    viewModel: MyViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState.userProfileState) {
        is UiState.Idle -> {
            NoAccountScreen(
                modifier = modifier,
                onLoginButtonClick = {},
                onDismissRequest = onBackClick,
            )
        }

        is UiState.Success<*>, UiState.Idle -> {
            MySuccessScreen(
                modifier = Modifier,
                selectedTab = uiState.selectedTab,
                myInfo = (uiState.userProfileState as UiState.Success<MyInfo>).data,
                onTabSelected = viewModel::onTabSelected,
                onProfileEditNavigate = onProfileEditNavigate,
                onNoticeNavigate = onNoticeNavigate,
                onTermsNavigate = onTermsNavigate,
                onPrivacyPolicyNavigate = onPrivacyPolicyNavigate,
                onFeedbackNavigate = onFeedbackNavigate,
                onSavedRestNavigate = onSavedRestNavigate,
                onCheckedRestNavigate = onCheckedRestNavigate,
                onMyArticleNavigate = onMyArticleNavigate,
                onMyCommentNavigate = onMyCommentNavigate,
                onScrapNavigate = onScrapNavigate,
            )
        }

        is UiState.Loading -> {

        }

        else -> {}
    }
}

@Composable
private fun MySuccessScreen(
    modifier: Modifier,
    selectedTab: Int,
    myInfo: MyInfo,
    onTabSelected: (Int) -> Unit,
    onProfileEditNavigate: () -> Unit,
    onNoticeNavigate: () -> Unit,
    onTermsNavigate: () -> Unit,
    onPrivacyPolicyNavigate: () -> Unit,
    onFeedbackNavigate: () -> Unit,
    onSavedRestNavigate: () -> Unit,
    onCheckedRestNavigate: () -> Unit,
    onMyArticleNavigate: () -> Unit,
    onMyCommentNavigate: () -> Unit,
    onScrapNavigate: () -> Unit,
) {
    val tabs = remember { MyTab.entries }
    val pagerState = rememberPagerState { tabs.size }
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier.fillMaxSize().background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MyTabRow(
            tabs = MyTab.entries,
            selectedIndex = selectedTab,
            onTabClick = { index ->
                onTabSelected(index)
                scope.launch {
                    pagerState.animateScrollToPage(index)
                }
            }
        )

        Box(Modifier.fillMaxSize()) {
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
                modifier = Modifier.fillMaxSize(),
            ) { page ->
                when (tabs[page]) {
                    MyTab.PROFILE -> {
                        MyProfileScreen(
                            userName = myInfo.nickname,
                            userImgUrl = myInfo.iconUrl,
                            modifier = Modifier,
                            onEditProfileClick = onProfileEditNavigate,
                            onNoticeClick = onNoticeNavigate,
                            onTermsClick = onTermsNavigate,
                            onPrivacyPolicyClick = onPrivacyPolicyNavigate,
                            onFeedbackClick = onFeedbackNavigate,
                            onLogoutClick = {},
                            onDeleteAccountClick = {},
                        )
                    }

                    MyTab.MY_ACTIVITIES -> {
                        MyActivityScreen(
                            modifier = Modifier,
                            onSavedRestClick = onSavedRestNavigate,
                            onCheckedRestClick = onCheckedRestNavigate,
                            onMyArticleClick = onMyArticleNavigate,
                            onMyCommentClick = onMyCommentNavigate,
                            onScrapClick = onScrapNavigate,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NoAccountScreen(
    modifier: Modifier = Modifier,
    onLoginButtonClick: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = modifier.fillMaxSize().background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            MyTabRow(
                tabs = MyTab.entries,
                selectedIndex = 0,
                onTabClick = {}
            )

            Box(Modifier.fillMaxSize()) {
                HorizontalPager(
                    state = rememberPagerState { 2 },
                    userScrollEnabled = false,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    MyProfileScreen(
                        userName = "",
                        userImgUrl = "",
                        modifier = Modifier,
                        isGuest = true,
                        onEditProfileClick = {},
                        onNoticeClick = {},
                        onTermsClick = {},
                        onPrivacyPolicyClick = {},
                        onFeedbackClick = {},
                        onLogoutClick = {},
                        onDeleteAccountClick = {},
                    )
                }
            }
        }

        LoginRequiredDialog(
            targetFeature = "마이페이지",
            onLoginButtonClick = onLoginButtonClick,
            onDismissRequest = onDismissRequest,
            modifier = modifier,
        )
    }
}

package com.kus.feature.my.ui

import UiState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kus.designsystem.component.KusBasicDialog
import com.kus.designsystem.theme.KusTheme
import com.kus.designsystem.util.noRippleClickable
import com.kus.feature.my.component.MyActivityScreen
import com.kus.feature.my.component.MyProfileScreen
import com.kus.feature.my.component.MyTabRow
import com.kus.feature.my.ui.event.MyNavigationEvent
import com.kus.feature.my.ui.type.MyTab
import com.kus.shared.domain.model.my.MyInfo
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MyScreen(
    modifier: Modifier = Modifier,
    onShowMessage: (String) -> Unit,
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
    onLoginNavigate: () -> Unit,
    viewModel: MyViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.requireLogin()
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is MyNavigationEvent.NavigateToLogin -> onLoginNavigate()
            }
        }
    }

    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let {
            onShowMessage(it)
            viewModel.clearToastMessage()
        }
    }

    when (uiState.userProfileState) {
        is UiState.Idle -> {
            NoAccountScreen(modifier = modifier)

            Box(
                modifier = Modifier
                    .noRippleClickable {/* 클릭 방지용 */ }
                    .fillMaxSize()
                    .background(KusTheme.colors.c_000000.copy(alpha = 0.5f))
            )
        }

        is UiState.Success<*> -> {
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
                onLogoutClick = {
                    viewModel.logout()
                },
                onDeleteAccountClick = {
                  viewModel.deleteAccount()
                },
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
    onLogoutClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
) {
    val tabs = remember { MyTab.entries }
    val pagerState = rememberPagerState { tabs.size }
    val scope = rememberCoroutineScope()
    var isLogoutPopup by remember { mutableStateOf(false) }
    var isDeleteAccountPopup by remember { mutableStateOf(false) }

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
                            onLogoutClick = { isLogoutPopup = true },
                            onDeleteAccountClick = { isDeleteAccountPopup = true },
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

    if (isLogoutPopup) {
        KusBasicDialog(
            content = {
                Text(
                    text = "정말 로그아웃하시겠습니까?",
                    style = KusTheme.typography.type16m,
                    textAlign = TextAlign.Center,
                )
            },
            confirmText = "로그아웃하기",
            onConfirmButtonClick = onLogoutClick,
            onDismissRequest = { isLogoutPopup = false},
        )
    }

    if (isDeleteAccountPopup) {
        KusBasicDialog(
            content = {
                Text(
                    text = "정말 탈퇴하시겠습니까?",
                    style = KusTheme.typography.type16m,
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "회원탈퇴 시 계정과 관련된 정보는 복구되지 않습니다.",
                    style = KusTheme.typography.type13m,
                    color = KusTheme.colors.c_9BA5B0,
                    textAlign = TextAlign.Center,
                )
            },
            confirmText = "회원 탈퇴하기",
            onConfirmButtonClick = onDeleteAccountClick,
            onDismissRequest = { isDeleteAccountPopup = false},
        )
    }
}

@Composable
private fun NoAccountScreen(
    modifier: Modifier = Modifier,
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
    }
}

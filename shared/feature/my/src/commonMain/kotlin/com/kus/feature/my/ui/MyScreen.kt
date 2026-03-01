package com.kus.feature.my.ui

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
import com.kus.feature.my.component.MyActivityScreen
import com.kus.feature.my.component.MyProfileScreen
import com.kus.feature.my.component.MyTabRow
import com.kus.feature.my.ui.type.MyTab
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MyScreen(
    modifier: Modifier = Modifier,
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
    val viewModel: MyViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.fillMaxSize().background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MyTabRow(
            tabs = MyTab.entries,
            selectedIndex = uiState.selectedTab,
            onTabClick = { index ->
                viewModel.onTabSelected(index)
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
                            userName = "쿠쿠스토랑",
                            userImgUrl = "",
                            modifier = Modifier,
                            onEditProfileClick = onProfileEditNavigate,
                            onNoticeClick = onNoticeNavigate,
                            onTermsClick = onTermsNavigate,
                            onPrivacyPolicyClick = onPrivacyPolicyNavigate,
                            onFeedbackClick = onFeedbackNavigate,
                            onLogoutClick = { },
                            onDeleteAccountClick = { },
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


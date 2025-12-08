package com.kus.feature.onBoarding.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OnboardingScreen(
    state: OnboardingUiState,
    onNextClick: () -> Unit,
    onGetStartedClick: () -> Unit,
) {
    val pagerState = rememberPagerState(initialPage = state.currentPage) {
        OnboardingViewModel.TOTAL_PAGE
    }

    // 화면 변경되면 pagerState도 동일하게 업데이트
    LaunchedEffect(state.currentPage) {
        pagerState.animateScrollToPage(state.currentPage)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(state = pagerState) { page ->
            OnboardingPage(page)
        }
        PageIndicator(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 96.dp),
            page = state.currentPage
        )
        NextButton(
            page = state.currentPage,
            onNextClick = onNextClick,
            onGetStartedClick = onGetStartedClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}

@Composable
private fun OnboardingPage(page: Int) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Onboarding page #${page + 1}")
        // TODO: Replace with real images instead of Text
    }
}

@Composable
private fun PageIndicator(modifier: Modifier = Modifier, page: Int) {
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        repeat(OnboardingViewModel.TOTAL_PAGE) {
            val color = if (it == page) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
            Box(modifier = Modifier.size(8.dp).background(color, shape = MaterialTheme.shapes.small))
        }
    }
}

@Composable
private fun NextButton(
    page: Int,
    onNextClick: () -> Unit,
    onGetStartedClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(onClick = {
        if (page == OnboardingViewModel.LAST_INDEX) onGetStartedClick()
        else onNextClick()
    }, modifier = modifier) {
        Text(if (page == OnboardingViewModel.LAST_INDEX) "시작하기!" else "다음")
    }
}
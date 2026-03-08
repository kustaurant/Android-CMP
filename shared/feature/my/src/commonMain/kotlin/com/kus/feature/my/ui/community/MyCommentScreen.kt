package com.kus.feature.my.ui.community

import UiState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kus.designsystem.theme.KusTheme
import com.kus.feature.my.component.EmptyPage
import com.kus.feature.my.component.MyPageTopBar
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun MyCommentScreen(
    onBackClick: () -> Unit,
    onItemClick: (Int) -> Unit,
    viewModel: MyCommentViewModel = koinViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getMyComments()
    }

    when (uiState.value.comments) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is UiState.Success<*> -> {
            val restaurants = (uiState.value.comments as UiState.Success).data
            if (restaurants.isEmpty()) {
                EmptyPage(
                    title = "내가 남긴 댓글",
                    comment = "남긴 댓글이 없습니다.",
                    onBackClick = onBackClick,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(KusTheme.colors.c_F3F3F3),
                ) {
                    stickyHeader {
                        MyPageTopBar(
                            title = "내가 남긴 댓글",
                            onBackClick = onBackClick,
                        )
                    }

                    item {
                        Spacer(Modifier.height(30.dp))
                    }

                    itemsIndexed(restaurants) { index, item ->
                        Box(
                            modifier = Modifier.padding(horizontal = 20.dp)
                        ) {

                        }
                    }
                }
            }
        }

        is UiState.Failure -> { /* 서버 연결 실패 시 화면 */ }
        is UiState.Idle -> {}
    }
}
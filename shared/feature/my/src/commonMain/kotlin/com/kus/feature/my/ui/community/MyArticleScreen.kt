package com.kus.feature.my.ui.community

import UiState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import com.kus.designsystem.util.noRippleClickable
import com.kus.feature.my.component.EmptyPage
import com.kus.feature.my.component.MyPageTopBar
import com.kus.feature.my.component.PostItem
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun MyArticleScreen(
    onBackClick: () -> Unit,
    onItemClick: (Long) -> Unit,
    viewModel: MyArticleViewModel = koinViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getMyArticle()
    }

    when (uiState.value.articles) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is UiState.Success<*> -> {
            val articles = (uiState.value.articles as UiState.Success).data
            if (articles.isEmpty()) {
                EmptyPage(
                    title = "내 게시글",
                    comment = "작성한 글이 없습니다.",
                    onBackClick = onBackClick,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(KusTheme.colors.c_FFFFFF),
                ) {
                    stickyHeader {
                        MyPageTopBar(
                            title = "내 게시글",
                            onBackClick = onBackClick,
                        )
                    }

                    item {
                        Spacer(Modifier.height(10.dp))
                    }

                    itemsIndexed(
                        items = articles,
                        key = { _, item -> item.postId }
                    ) { index, item ->
                        PostItem(
                            item = item,
                            modifier = Modifier.noRippleClickable { onItemClick(item.postId.toLong()) }
                        )
                    }

                    item {
                        Spacer(Modifier.height(10.dp))
                    }
                }
            }
        }

        is UiState.Failure -> { /* 서버 연결 실패 시 화면 */ }
        is UiState.Idle -> {}
    }
}

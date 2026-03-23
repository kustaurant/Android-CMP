package com.kus.feature.search.ui

import UiState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kus.designsystem.theme.KusTheme
import com.kus.designsystem.util.noRippleClickable
import com.kus.feature.search.component.KusSearchBox
import com.kus.feature.search.component.SearchResultThumbnail
import com.kus.feature.search.state.SearchUiState
import kustaurant.shared.feature.search.generated.resources.Res
import kustaurant.shared.feature.search.generated.resources.ic_left_chevron
import kustaurant.shared.feature.search.generated.resources.img_no_result
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchRoute(
    onBackClick: () -> Unit,
    onRestDetailNavigate: (Long) -> Unit,
    viewModel: SearchViewModel = koinViewModel(),
) {
    val searchTerm by viewModel.searchTerm.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastIndex ->
                if (lastIndex != null && lastIndex >= uiState.items.lastIndex - 3) {
                    viewModel.loadNextPage()
                }
            }
    }

    SearchScreen(
        searchTerm = searchTerm,
        uiState = uiState,
        listState = listState,
        onBackClick = onBackClick,
        onSearchTermChange = viewModel::updateSearchTerm,
        onRestaurantItemClick = onRestDetailNavigate,
    )
}

@Composable
fun SearchScreen(
    searchTerm: TextFieldValue,
    uiState: SearchUiState,
    listState: LazyListState,
    onBackClick: () -> Unit,
    onSearchTermChange: (TextFieldValue) -> Unit,
    onRestaurantItemClick: (Long) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .collect { isScrolling ->
                if (isScrolling) {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .background(KusTheme.colors.c_F3F3F3)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
                keyboardController?.hide()
            },
        state = listState,
    ) {
        stickyHeader {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .background(KusTheme.colors.c_FFFFFF)
                    .statusBarsPadding()
                    .padding(10.dp, 16.dp, 20.dp, 16.dp),
                verticalAlignment = CenterVertically,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_left_chevron),
                    contentDescription = "뒤로가기 버튼",
                    modifier = Modifier.noRippleClickable(onBackClick),
                )

                Spacer(Modifier.width(8.dp))

                KusSearchBox(
                    searchTerm = searchTerm,
                    onValueChange = onSearchTermChange,
                    onSearchButonClick = { /* 기능 없음*/ },
                    modifier = Modifier
                        .focusRequester(focusRequester),
                )
            }
        }

        item {
            Spacer(Modifier.background(KusTheme.colors.c_F3F3F3).height(15.dp))
        }

        when (uiState.uiState) {
            is UiState.Loading -> {
                item { }
            }

            is UiState.Success -> {
                val resultItems = uiState.items

                if (resultItems.isEmpty() && searchTerm.text.isNotEmpty()) {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth().background(KusTheme.colors.c_F3F3F3)
                                .padding(top = 60.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.img_no_result),
                                contentDescription = null,
                            )

                            Spacer(Modifier.height(6.dp))

                            Text(
                                text = "해당 검색어에 맞는 식당이 없어요",
                                style = KusTheme.typography.type17sb,
                                color = KusTheme.colors.c_AAAAAA,
                            )
                        }
                    }
                } else {
                    itemsIndexed(
                        items = resultItems,
                        key = { _, item -> item.id }
                    ) { index, item ->
                        Column(
                            modifier = Modifier.fillMaxWidth()
                                .background(KusTheme.colors.c_F3F3F3)
                                .padding(start = 20.dp, bottom = 8.dp, end = 20.dp),
                        ) {
                            SearchResultThumbnail(
                                tier = item.tier,
                                restName = item.name,
                                restThumbnail = item.imgUrl,
                                cuisine = item.cuisine,
                                location = item.position,
                                isSaved = item.isFavorite,
                                isEvaluated = item.isEvaluated,
                                matchedMenus = item.matchedMenus,
                                titleHighlights = item.titleHighlights,
                                categoryHighlights = item.categoryHighlights,
                                onClick = { onRestaurantItemClick(item.id) },
                            )

                            if (resultItems.lastIndex == index) {
                                Spacer(Modifier.height(20.dp))
                            }
                        }
                    }
                }
            }

            is UiState.Failure -> {
                item { }
            }

            is UiState.Idle -> {
                item {
                    Spacer(Modifier.background(KusTheme.colors.c_F3F3F3).fillMaxSize())
                }
            }
        }
    }
}

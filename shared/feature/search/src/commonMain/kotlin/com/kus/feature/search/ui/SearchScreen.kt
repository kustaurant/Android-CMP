package com.kus.feature.search.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kus.designsystem.component.KusRestThumbnail
import com.kus.designsystem.theme.KusTheme
import com.kus.designsystem.util.noRippleClickable
import com.kus.feature.search.component.KusSearchBox
import kustaurant.shared.feature.search.generated.resources.Res
import kustaurant.shared.feature.search.generated.resources.ic_left_chevron
import kustaurant.shared.feature.search.generated.resources.img_no_result
import org.jetbrains.compose.resources.painterResource

@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    onRestDetailNavigate: (Long) -> Unit,
    viewModel: SearchViewModel = viewModel(),
) {
    val searchTerm by viewModel.searchTerm.collectAsStateWithLifecycle()
    val resultItems by viewModel.resultItems.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .background(KusTheme.colors.c_F3F3F3),
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
                    modifier = Modifier.noRippleClickable(onBackClick)
                )

                Spacer(Modifier.width(8.dp))

                KusSearchBox(
                    searchTerm = searchTerm,
                    onValueChange = viewModel::updateSearchTerm,
                    onSearchButonClick = { /* 검색 API 연결 */ }
                )
            }
        }

        item {
            Spacer(Modifier.background(KusTheme.colors.c_F3F3F3).height(15.dp))
        }

        if (resultItems.isEmpty() && searchTerm.isNotEmpty()) {
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
            itemsIndexed(resultItems) { index, item ->
                Column (
                    modifier = Modifier.fillMaxWidth()
                        .background(KusTheme.colors.c_F3F3F3)
                        .padding(start = 20.dp, bottom = 8.dp, end = 20.dp),
                ) {
                    KusRestThumbnail(
                        restName = "식당이름입니다.",
                        isSaved = true,
                        isEvaluated = false,
                        onClick = { onRestDetailNavigate(0) },
                    )

                    if (resultItems.lastIndex == index) {
                        Spacer(Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}

package com.kus.feature.home.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun HomeBannerPager(
    imageUrls: List<String>,
    modifier: Modifier = Modifier,
    autoSlideDelay: Long = 3000L,
) {
    val pageCount = imageUrls.size
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { pageCount }
    )

    LaunchedEffect(pageCount) {
        if (pageCount <= 1) return@LaunchedEffect
        while (true) {
            delay(autoSlideDelay)

            if (!pagerState.isScrollInProgress) {
                val nextPage = (pagerState.currentPage + 1) % pageCount
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            KamelImage(
                resource = { asyncPainterResource(imageUrls[page]) },
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(),
                onFailure = {
                    Box(
                        Modifier.fillMaxSize().background(KusTheme.colors.c_F5F5F5),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "이미지 로드 오류",
                            style = KusTheme.typography.type14r,
                        )
                    }
                }
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 12.dp, end = 14.dp)
                .background(
                    KusTheme.colors.c_AAAAAA.copy(alpha = 0.3f),
                    RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${pagerState.currentPage + 1}/$pageCount",
                style = KusTheme.typography.type12r,
                color = KusTheme.colors.c_FFFFFF,
            )
        }
    }
}

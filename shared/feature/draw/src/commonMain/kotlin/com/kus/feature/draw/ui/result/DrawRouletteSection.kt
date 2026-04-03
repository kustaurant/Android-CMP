package com.kus.feature.draw.ui.result

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.kus.designsystem.component.KusLoadingAnimation
import com.kus.designsystem.util.noRippleClickable
import com.kus.designsystem.theme.KusTheme
import com.kus.domain.draw.model.DrawRestaurant
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
fun DrawRouletteSection(
    items: List<DrawRestaurant>,
    targetIndex: Int,
    shouldAnimate: Boolean,
    onCurrentIndexChanged: (Int) -> Unit = {},
    modifier: Modifier = Modifier,
    onAnimationFinished: () -> Unit = {},
    onCardClick: () -> Unit = {},
) {
    val preloadedResources = items.map { item ->
        key(item.restaurantImgUrl) {
            asyncPainterResource(item.restaurantImgUrl)
        }
    }

    val allImagesLoaded =
        preloadedResources.all { it is Resource.Success || it is Resource.Failure }
    val hasAnyFailure = preloadedResources.any { it is Resource.Failure }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(310.dp)
    ) {
        if (items.isEmpty()) return@BoxWithConstraints

        if (!allImagesLoaded || hasAnyFailure) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (hasAnyFailure) {
                    Text("이미지 로딩 실패했어요.")
                } else {
                    KusLoadingAnimation()
                }
            }
            return@BoxWithConstraints
        }

        val density = LocalDensity.current

        val panoramaCardWidth = 260.dp
        val panoramaCardHeight = 260.dp
        val selectedCardWidth = 300.dp
        val selectedCardHeight = 300.dp
        val horizontalPadding = (maxWidth - panoramaCardWidth) / 2

        val itemFullWidthPx = with(density) { panoramaCardWidth.toPx() }

        val totalCount = items.size * 5
        val targetDisplayIndex = items.size * 2 + targetIndex

        val selectedAlpha = remember(items, targetIndex) { Animatable(0f) }
        val panoramaAlpha = remember(items, targetIndex) { Animatable(1f) }

        val listState = rememberLazyListState()

        val endIndex = targetDisplayIndex
        val endOffset = 0

        val startIndex = (targetDisplayIndex - 30).coerceAtLeast(0)
        val startOffset = 0

        val totalScrollDistance = (endIndex - startIndex) * itemFullWidthPx

        val currentCenterIndex by remember {
            derivedStateOf {
                val firstVisible = listState.firstVisibleItemIndex
                val firstOffset = listState.firstVisibleItemScrollOffset
                val centerRaw = firstVisible + (firstOffset / itemFullWidthPx).roundToInt()
                centerRaw % items.size
            }
        }

        LaunchedEffect(currentCenterIndex) {
            onCurrentIndexChanged(currentCenterIndex)
        }

        LaunchedEffect(items, targetIndex, shouldAnimate) {
            if (!shouldAnimate) {
                selectedAlpha.snapTo(1f)
                panoramaAlpha.snapTo(0f)
                listState.scrollToItem(
                    index = endIndex,
                    scrollOffset = endOffset
                )
                onCurrentIndexChanged(targetIndex)
                return@LaunchedEffect
            }

            selectedAlpha.snapTo(0f)
            panoramaAlpha.snapTo(1f)

            listState.scrollToItem(
                index = startIndex,
                scrollOffset = startOffset
            )

            val durationMillis = 1200
            val startTime = withFrameMillis { it }
            var prevProgress = 0f

            do {
                val frameTime = withFrameMillis { it }
                val elapsed = (frameTime - startTime).coerceAtMost(durationMillis.toLong())
                val rawProgress = elapsed / durationMillis.toFloat()

                val delta = (rawProgress - prevProgress) * totalScrollDistance
                listState.scrollBy(delta)
                prevProgress = rawProgress
            } while (rawProgress < 1f)

            listState.scrollToItem(
                index = endIndex,
                scrollOffset = endOffset
            )

            coroutineScope {
                launch {
                    panoramaAlpha.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(500)
                    )
                }
                launch {
                    selectedAlpha.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(500, easing = FastOutSlowInEasing)
                    )
                }
            }

            onAnimationFinished()
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LazyRow(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(panoramaAlpha.value),
                verticalAlignment = Alignment.CenterVertically,
                userScrollEnabled = false,
                contentPadding = PaddingValues(horizontal = horizontalPadding)
            ) {
                items(count = totalCount, key = { it }) { index ->
                    val resource = preloadedResources[index % items.size]

                    DrawPanoramaCard(
                        resource = resource,
                        modifier = Modifier.size(
                            width = panoramaCardWidth,
                            height = panoramaCardHeight
                        )
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(width = selectedCardWidth, height = selectedCardHeight)
                    .graphicsLayer {
                        alpha = selectedAlpha.value
                        scaleX = 0.96f + (0.04f * selectedAlpha.value)
                        scaleY = 0.96f + (0.04f * selectedAlpha.value)
                    }
                    .noRippleClickable { onCardClick() }
            ) {
                DrawRestaurantCard(
                    item = items[targetIndex],
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun DrawPanoramaCard(
    resource: Resource<Painter>,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(KusTheme.colors.c_FFFFFF)
    ) {
        DrawRestaurantImage(
            resource = resource,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(KusTheme.colors.c_000000.copy(alpha = 0.18f))
        )
    }
}

@Composable
private fun DrawRestaurantCard(
    item: DrawRestaurant,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(28.dp))
            .border(
                width = 2.dp,
                color = KusTheme.colors.c_43AB38,
                shape = RoundedCornerShape(28.dp)
            )
    ) {
        DrawRestaurantImage(
            resource = asyncPainterResource(item.restaurantImgUrl),
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun DrawRestaurantImage(
    resource: Resource<Painter>,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        KamelImage(
            resource = resource,
            contentDescription = "음식점 사진입니다.",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
    }
}

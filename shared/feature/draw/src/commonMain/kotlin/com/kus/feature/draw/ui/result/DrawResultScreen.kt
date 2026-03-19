package com.kus.feature.draw.ui.result

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kus.designsystem.component.KusButton
import com.kus.designsystem.component.KusLoadingAnimation
import com.kus.designsystem.component.KusRatingBar
import com.kus.designsystem.component.KusTopBar
import com.kus.designsystem.theme.KusTheme
import com.kus.domain.draw.model.DrawRestaurant
import com.kus.shared.domain.model.tier.filter.Cuisine
import com.kus.shared.domain.model.tier.filter.Location
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kustaurant.shared.core.designsystem.generated.resources.ic_alarm_off
import kustaurant.shared.core.designsystem.generated.resources.ic_arrow_back
import kustaurant.shared.core.designsystem.generated.resources.ic_search
import kustaurant.shared.feature.draw.generated.resources.Res
import kustaurant.shared.feature.draw.generated.resources.ic_retry
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.roundToInt
import kustaurant.shared.core.designsystem.generated.resources.Res as CoreRes

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawResultScreen(
    initialLocations: Set<Location>,
    initialCuisines: Set<Cuisine>,
    onBackClick: () -> Unit,
    onShowMessage: (String) -> Unit,
    viewModel: DrawResultViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val items = uiState.restaurants
    val randomIndex = uiState.randomIndex

    var isDrawLocked by remember { mutableStateOf(true) }

    var currentRestaurant by remember(items, randomIndex) {
        mutableStateOf(
            if (items.isNotEmpty() && randomIndex != null) items[randomIndex] else null
        )
    }

    BackHandler { onBackClick() }

    LaunchedEffect(Unit) {
        viewModel.initialize(
            locations = initialLocations,
            cuisines = initialCuisines
        )
    }

    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let(onShowMessage)
    }

    Scaffold(
        topBar = {
            KusTopBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 8.dp),
                leftIcon = painterResource(CoreRes.drawable.ic_arrow_back),
                rightFirstIcon = painterResource(CoreRes.drawable.ic_search),
                rightSecondIcon = painterResource(CoreRes.drawable.ic_alarm_off),
                onLeftClicked = onBackClick,
                onRightFirstClicked = {},
                onRightSecondClicked = {},
            ) {
                Text(
                    text = "랜덤 맛집 뽑기",
                    style = KusTheme.typography.type17sb,
                    color = KusTheme.colors.c_323232
                )
            }
        },
        bottomBar = {
            if (randomIndex != null && items.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .navigationBarsPadding()
                        .padding(bottom = 28.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(13.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(13.dp)
                        ) {
                            Box(
                                modifier = Modifier.shadow(
                                    elevation = 4.dp,
                                    shape = RoundedCornerShape(50.dp)
                                )
                            ) {
                                KusButton(
                                    enabled = true,
                                    buttonName = "카테고리 재설정",
                                    textStyle = KusTheme.typography.type14sb,
                                    modifier = Modifier.width(121.dp),
                                    roundedCornerShape = RoundedCornerShape(50.dp),
                                    containerColor = KusTheme.colors.c_FFFFFF,
                                    contentColor = KusTheme.colors.c_43AB38,
                                    borderColor = KusTheme.colors.c_43AB38,
                                    onClick = onBackClick
                                )
                            }

                            Box(
                                modifier = Modifier.shadow(
                                    elevation = 4.dp,
                                    shape = RoundedCornerShape(50.dp)
                                )
                            ) {
                                KusButton(
                                    enabled = !isDrawLocked && !uiState.isLoading,
                                    buttonName = "다시 뽑기",
                                    textStyle = KusTheme.typography.type14sb,
                                    modifier = Modifier.width(121.dp),
                                    roundedCornerShape = RoundedCornerShape(50.dp),
                                    borderColor = if (isDrawLocked) KusTheme.colors.c_E0E0E0 else KusTheme.colors.c_43AB38,
                                    contentColor = KusTheme.colors.c_FFFFFF,
                                    leftIcon = painterResource(Res.drawable.ic_retry),
                                    onClick = {
                                        if (!isDrawLocked) {
                                            isDrawLocked = true
                                            viewModel.redraw()
                                        }
                                    }
                                )
                            }
                        }

                    }
                }
            }
        },
        containerColor = KusTheme.colors.c_FFFFFF,
    ) { innerPadding ->
        when {
            uiState.isLoading || randomIndex == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    KusLoadingAnimation()
                }
            }

            items.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "뽑을 수 있는 맛집이 없어요.",
                        style = KusTheme.typography.type17sb,
                        color = KusTheme.colors.c_666666
                    )
                }
            }

            else -> {
                val displayRestaurant = currentRestaurant ?: items[randomIndex]
                val score = ((displayRestaurant.restaurantScore ?: 0.0) * 2).roundToInt() / 2.0

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "사진을 누르면 해당 식당의 정보를 볼 수 있어요!",
                        style = KusTheme.typography.type13r,
                        color = KusTheme.colors.c_AAAAAA,
                        modifier = Modifier.padding(top = 40.dp, bottom = 27.dp)
                    )

                    DrawRouletteSection(
                        items = items,
                        targetIndex = randomIndex,
                        modifier = Modifier.fillMaxWidth(),
                        onCurrentIndexChanged = { index ->
                            currentRestaurant = items[index]
                        },
                        onAnimationFinished = {
                            isDrawLocked = false
                            currentRestaurant = items[randomIndex]
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = displayRestaurant.restaurantCuisine,
                        style = KusTheme.typography.type14r,
                        color = KusTheme.colors.c_323232
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = displayRestaurant.restaurantName,
                        style = KusTheme.typography.type20b,
                        color = KusTheme.colors.c_323232
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        KusRatingBar(
                            rating = (displayRestaurant.restaurantScore ?: 0.0).toFloat(),
                            isEnabled = false,
                            starModifier = Modifier.size(16.dp)
                        )

                        Text(
                            text = "$score",
                            style = KusTheme.typography.type16r,
                            color = KusTheme.colors.c_666666
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text =
                            displayRestaurant.partnershipInfo
                                ?.takeIf { it.isNotBlank()}
                                ?: "제휴 정보 없음",
                        style = KusTheme.typography.type15m,
                        color = KusTheme.colors.c_838383,
                        textAlign = TextAlign.Center,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.width(240.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun DrawRouletteSection(
    items: List<DrawRestaurant>,
    targetIndex: Int,
    onCurrentIndexChanged: (Int) -> Unit = {},
    modifier: Modifier = Modifier,
    onAnimationFinished: () -> Unit = {},
) {
    val preloadedResources = items.map { item ->
        key(item.restaurantImgUrl) {
            asyncPainterResource(item.restaurantImgUrl)
        }
    }

    val allImagesLoaded = preloadedResources.all { it is Resource.Success }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(310.dp)
    ) {
        if (items.isEmpty()) return@BoxWithConstraints

        if (!allImagesLoaded) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                KusLoadingAnimation()
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
        val viewportWidthPx = with(density) { maxWidth.toPx() }

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

        LaunchedEffect(items, targetIndex) {
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
        modifier = modifier ,
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

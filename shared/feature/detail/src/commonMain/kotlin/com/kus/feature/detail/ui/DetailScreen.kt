package com.kus.feature.detail.ui

import UiState
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kus.designsystem.component.KusButton
import com.kus.designsystem.component.KusLoadingAnimation
import com.kus.designsystem.component.KusTopBar
import com.kus.designsystem.theme.KusTheme
import com.kus.designsystem.util.noRippleClickable
import com.kus.feature.detail.component.DetailCommentInputBar
import com.kus.feature.detail.component.DetailHeaderImage
import com.kus.feature.detail.component.DetailRestInfo
import com.kus.feature.detail.component.DetailTabSection
import com.kus.feature.detail.model.ReviewSort
import com.kus.shared.domain.model.detail.RestaurantDetail
import com.kus.shared.domain.model.detail.RestaurantReview
import kustaurant.shared.core.designsystem.generated.resources.Res
import kustaurant.shared.core.designsystem.generated.resources.ic_arrow_back
import kustaurant.shared.core.designsystem.generated.resources.ic_saved
import kustaurant.shared.core.designsystem.generated.resources.ic_unsaved
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DetailRoute(
    restaurantId: Long = 510L,
    navigateToEvaluate: (RestaurantDetail) -> Unit,
    navigateToUp: () -> Unit,
    shouldRefreshFromEvaluate: Boolean = false,
    clearEvaluateRefreshFlag: () -> Unit = {},
    viewModel: DetailViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(restaurantId) {
        viewModel.getRestaurantDetail(restaurantId)
    }

    LaunchedEffect(shouldRefreshFromEvaluate) {
        if (!shouldRefreshFromEvaluate) return@LaunchedEffect
        viewModel.refreshAfterEvaluation()
        clearEvaluateRefreshFlag()
    }

    when (val restaurantState = uiState.restaurant) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                KusLoadingAnimation(
                    modifier = Modifier.size(120.dp)
                )
            }
        }

        is UiState.Success -> {
            DetailSuccessScreen(
                restaurant = restaurantState.data,
                reviewsState = uiState.reviews,
                reviewSort = uiState.reviewSort,
                navigateToEvaluate = { navigateToEvaluate(restaurantState.data) },
                onBackClick = navigateToUp,
                onFavoriteClick = { viewModel.onFavoriteClick() },
                onSortSelected = { sort -> viewModel.getRestaurantReviews(sort) },
                onReviewTabSelected = { viewModel.getRestaurantReviewsIfNeeded() },
                onReviewLikeClick = { evalId -> viewModel.onReviewLikeClick(evalId) },
                onReviewDislikeClick = { evalId -> viewModel.onReviewDislikeClick(evalId) },
                onCommentLikeClick = { evalId, commentId -> viewModel.onCommentLikeClick(evalId, commentId) },
                onCommentDislikeClick = { evalId, commentId -> viewModel.onCommentDislikeClick(evalId, commentId) },
                onCommentSubmit = { evalId, body -> viewModel.postComment(evalId, body) },
                onCommentDeleteClick = { evalId, commentId -> viewModel.deleteComment(evalId, commentId) },
            )
        }

        is UiState.Failure -> {
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(color = KusTheme.colors.c_FFFFFF),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "서버 연결이 불안정합니다. 다시 시도해주세요.")
            }
        }

        is UiState.Idle -> {}
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DetailSuccessScreen(
    restaurant: RestaurantDetail,
    reviewsState: UiState<List<RestaurantReview>>,
    reviewSort: ReviewSort,
    navigateToEvaluate: () -> Unit,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onSortSelected: (ReviewSort) -> Unit,
    onReviewTabSelected: () -> Unit,
    onReviewLikeClick: (Int) -> Unit,
    onReviewDislikeClick: (Int) -> Unit,
    onCommentLikeClick: (Int, Int) -> Unit,
    onCommentDislikeClick: (Int, Int) -> Unit,
    onCommentSubmit: (Int, String) -> Unit,
    onCommentDeleteClick: (Int, Int) -> Unit,
) {
    var restInfoTopInWindow by remember { mutableFloatStateOf(Float.POSITIVE_INFINITY) }
    var topBarBottomInWindow by remember { mutableFloatStateOf(0f) }
    val useWhiteTopBar by remember {
        derivedStateOf { restInfoTopInWindow <= topBarBottomInWindow }
    }
    val topBarBackground = if (useWhiteTopBar) KusTheme.colors.c_FFFFFF else Color.Transparent
    val topBarIconTint = if (useWhiteTopBar) KusTheme.colors.c_000000 else KusTheme.colors.c_FFFFFF
    val evaluateButtonText = if (restaurant.isEvaluated) "다시 평가하기" else "맛집 평가하기"
    val favoriteIcon = if (restaurant.isFavorite) Res.drawable.ic_saved else Res.drawable.ic_unsaved
    val favoriteCountText = restaurant.favoriteCount.toString()
    var isCommentInputVisible by remember { mutableStateOf(false) }
    var selectedEvalId by remember { mutableStateOf<Int?>(null) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val bottomBarHeight = 76.dp

    Box(
        modifier = Modifier.fillMaxSize()
            .background(color = KusTheme.colors.c_FFFFFF)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = bottomBarHeight)
        ) {
            item {
                val imageHeight = 329.dp
                val overlap = 100.dp
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DetailHeaderImage(
                        imageUrl = restaurant.restaurantImgUrl,
                        imageHeight = imageHeight
                    )

                    DetailRestInfo(
                        situationList = restaurant.situationList,
                        mainTier = restaurant.mainTier,
                        isTempTier = restaurant.isTempTier,
                        restaurantCuisine = restaurant.restaurantCuisine,
                        restaurantCuisineImgUrl = restaurant.restaurantCuisineImgUrl,
                        restaurantPosition = restaurant.restaurantPosition,
                        restaurantName = restaurant.restaurantName,
                        restaurantAddress = restaurant.restaurantAddress,
                        naverMapUrl = restaurant.naverMapUrl,
                        partnershipInfo = restaurant.partnershipInfo,
                        rating = restaurant.restaurantScore,
                        evaluationCount = restaurant.evaluationCount,
                        modifier = Modifier
                            .padding(top = imageHeight - overlap)
                            .onGloballyPositioned { coordinates ->
                                restInfoTopInWindow = coordinates.positionInWindow().y
                            }
                    )
                }
            }

            item {
                val reviewList = when (reviewsState) {
                    is UiState.Success -> reviewsState.data
                    else -> emptyList()
                }
                DetailTabSection(
                    reviewCount = restaurant.evaluationCount,
                    menuList = restaurant.restaurantMenuList,
                    reviewList = reviewList,
                    selectedSort = reviewSort,
                    onSortSelected = onSortSelected,
                    onReviewTabSelected = onReviewTabSelected,
                    onReviewLikeClick = onReviewLikeClick,
                    onReviewDislikeClick = onReviewDislikeClick,
                    onCommentClick = { evalId ->
                        selectedEvalId = evalId
                        isCommentInputVisible = true
                    },
                    onCommentLikeClick = onCommentLikeClick,
                    onCommentDislikeClick = onCommentDislikeClick,
                    onCommentDeleteClick = onCommentDeleteClick,
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(topBarBackground)
                .statusBarsPadding()
                .align(Alignment.TopCenter)
                .onGloballyPositioned { coordinates ->
                    topBarBottomInWindow = coordinates.positionInWindow().y + coordinates.size.height
                }
        ) {
            KusTopBar(
                leftIcon = painterResource(Res.drawable.ic_arrow_back),
                onLeftClicked = onBackClick,
                leftIconModifier = Modifier.padding(all = 5.dp),
                iconTint = topBarIconTint,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(color = KusTheme.colors.c_FFFFFF)
        ) {
            HorizontalDivider(
                thickness = 1.dp,
                color = KusTheme.colors.c_E0E0E0
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                KusButton(
                    enabled = true,
                    buttonName = evaluateButtonText,
                    modifier = Modifier.weight(1f),
                    roundedCornerShape = RoundedCornerShape(50.dp),
                    onClick = navigateToEvaluate
                )

                Column(
                    modifier = Modifier
                        .padding(start = 18.dp, end = 12.dp)
                        .noRippleClickable { onFavoriteClick() },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(favoriteIcon),
                        contentDescription = null,
                        modifier = Modifier.size(28.dp)
                    )

                    Text(
                        text = favoriteCountText,
                        style = KusTheme.typography.type14r.copy(
                            color = KusTheme.colors.c_AAAAAA
                        )
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = isCommentInputVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        keyboardController?.hide()
                        isCommentInputVisible = false
                    }
            )
        }

        AnimatedVisibility(
            visible = isCommentInputVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            DetailCommentInputBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = KusTheme.colors.c_FFFFFF)
                    .imePadding(),
                hasFocus = true,
                onDismiss = {
                    isCommentInputVisible = false
                    selectedEvalId = null
                },
                onSend = { body ->
                    selectedEvalId?.let { evalId ->
                        onCommentSubmit(evalId, body)
                    }
                }
            )
        }
    }
}

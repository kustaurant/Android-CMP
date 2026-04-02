package com.kus.feature.evaluate.ui

import UiState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kus.designsystem.component.KusButton
import com.kus.designsystem.component.KusLoadingAnimation
import com.kus.designsystem.component.KusTopBar
import com.kus.designsystem.theme.KusTheme
import com.kus.feature.evaluate.component.EvaluationImage
import com.kus.feature.evaluate.component.EvaluationKeyword
import com.kus.feature.evaluate.component.EvaluationRestInfoCard
import com.kus.feature.evaluate.component.EvaluationReview
import com.kus.feature.evaluate.component.EvaluationStar
import com.kus.feature.evaluate.model.EvaluateRestaurant
import com.kus.feature.evaluate.model.Evaluation
import kustaurant.shared.core.designsystem.generated.resources.Res
import kustaurant.shared.core.designsystem.generated.resources.ic_arrow_back
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EvaluateRoute(
    restaurantId: Long,
    restaurant: EvaluateRestaurant,
    onShowMessage: (String) -> Unit,
    onBackClick: () -> Unit,
    onSubmitSuccess: () -> Unit,
    viewModel: EvaluateViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(restaurantId) {
        viewModel.initRestaurant(restaurant)
        viewModel.getPreviousEvaluation(restaurantId)
    }

    LaunchedEffect(uiState.submitState) {
        if (uiState.submitState is UiState.Success) {
            onShowMessage("평가를 완료했어요.")
            onSubmitSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = KusTheme.colors.c_FFFFFF)
    ) {
        EvaluateTopBar(onBackClick = onBackClick)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            when (val evaluationState = uiState.evaluation) {
                is UiState.Loading, UiState.Idle -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        KusLoadingAnimation()
                    }
                }

                is UiState.Success -> {
                    EvaluateSuccessScreen(
                        restaurant = uiState.restaurant,
                        evaluation = evaluationState.data,
                        submitState = uiState.submitState,
                        onScoreChanged = { viewModel.updateEvaluationScore(it) },
                        onSituationsChanged = { viewModel.updateEvaluationSituations(it) },
                        onCommentChanged = { viewModel.updateEvaluationComment(it) },
                        onImageSelected = { viewModel.updateImageBytes(it) },
                        onSubmitClick = { viewModel.submitEvaluation() },
                    )
                }

                is UiState.Failure -> {
                    EvaluateFailureScreen(
                        onRetryClick = { viewModel.getPreviousEvaluation(restaurantId) }
                    )
                }
            }
        }
    }
}

@Composable
private fun EvaluateTopBar(onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        KusTopBar(
            leftIcon = painterResource(Res.drawable.ic_arrow_back),
            onLeftClicked = onBackClick,
            leftIconModifier = Modifier.padding(all = 5.dp),
            iconTint = KusTheme.colors.c_000000,
            modifier = Modifier
                .fillMaxWidth()
                .background(KusTheme.colors.c_FFFFFF),
            content = {
                Text(
                    text = "평가하기",
                    style = KusTheme.typography.type17sb
                )
            }
        )

        HorizontalDivider(
            thickness = 1.dp,
            color = KusTheme.colors.c_EAEAEA
        )
    }
}

@Composable
private fun EvaluateFailureScreen(onRetryClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "서버 연결이 불안정합니다. 다시 시도해주세요.",
            style = KusTheme.typography.type16sb,
            color = KusTheme.colors.c_666666,
        )

        Box(
            modifier = Modifier
                .padding(top = 16.dp)
        ) {
            KusButton(
                enabled = true,
                buttonName = "다시 시도",
                roundedCornerShape = RoundedCornerShape(50.dp),
                onClick = onRetryClick,
            )
        }
    }
}

@Composable
private fun EvaluateSuccessScreen(
    restaurant: EvaluateRestaurant,
    evaluation: Evaluation,
    submitState: UiState<Unit>,
    onScoreChanged: (Double) -> Unit,
    onSituationsChanged: (List<Int>) -> Unit,
    onCommentChanged: (String) -> Unit,
    onImageSelected: (ByteArray) -> Unit,
    onSubmitClick: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val listState = rememberLazyListState()
    val density = LocalDensity.current

    val imeBottom = WindowInsets.ime.getBottom(density)
    val isImeVisible = imeBottom > 0
    val canDismissKeyboard = remember { mutableStateOf(true) }

    LaunchedEffect(isImeVisible) {
        if (isImeVisible) {
            canDismissKeyboard.value = false
            delay(500)
            canDismissKeyboard.value = true
        }
    }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (canDismissKeyboard.value && source == NestedScrollSource.UserInput && available.y != 0f) {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
                return Offset.Zero
            }
        }
    }

    val isRatingSelected = evaluation.evaluationScore != 0.0
    val isSubmitting = submitState is UiState.Loading
    val submitButtonColor =
        if (isRatingSelected) KusTheme.colors.c_43AB38 else KusTheme.colors.c_E0E0E0

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .background(color = KusTheme.colors.c_FFFFFF)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .nestedScroll(nestedScrollConnection)
        ) {
            item {
                EvaluationRestInfoCard(restaurant = restaurant)
            }

            item {
                EvaluationStar(
                    initialRating = evaluation.evaluationScore,
                    onRatingChanged = onScoreChanged,
                )
            }

            item {
                EvaluationKeyword(
                    selectedSituations = evaluation.evaluationSituations,
                    onSituationChanged = onSituationsChanged,
                )
            }

            item {
                EvaluationReview(
                    evaluationComment = evaluation.evaluationComment,
                    onCommentChange = onCommentChanged,
                )
            }

            item {
                EvaluationImage(
                    imageUrl = evaluation.evaluationImgUrl,
                    imageBytes = evaluation.imageBytes,
                    onImageSelected = onImageSelected,
                )
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                )
            }

            item {
                Box(
                    modifier = Modifier
                        .background(color = KusTheme.colors.c_FFFFFF)
                        .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom))
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                        .align(Alignment.BottomCenter),
                ) {
                    KusButton(
                        enabled = isRatingSelected && !isSubmitting,
                        buttonName = if (isSubmitting) "제출 중..." else "평가 제출하기",
                        roundedCornerShape = RoundedCornerShape(50.dp),
                        containerColor = submitButtonColor,
                        borderColor = submitButtonColor,
                        onClick = {
                            if (!isRatingSelected || isSubmitting) return@KusButton
                            onSubmitClick()
                        },
                    )
                }
            }
        }
    }
}

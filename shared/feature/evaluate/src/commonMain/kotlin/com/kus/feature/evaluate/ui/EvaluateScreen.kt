package com.kus.feature.evaluate.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kus.designsystem.component.KusTopBar
import com.kus.designsystem.theme.KusTheme
import com.kus.designsystem.util.noRippleClickable
import com.kus.feature.evaluate.component.EvaluationRestaurantInfoCard
import com.kus.feature.evaluate.component.EvaluationKeyword
import com.kus.feature.evaluate.component.EvaluationReview
import com.kus.feature.evaluate.component.EvaluationStar
import kustaurant.shared.core.designsystem.generated.resources.Res
import kustaurant.shared.core.designsystem.generated.resources.ic_left_arrow
import org.jetbrains.compose.resources.painterResource


@Composable
fun EvaluateScreen(
    viewModel: EvaluateViewModel = viewModel { EvaluateViewModel() }
) {
    val uiState by viewModel.uiState.collectAsState()
    val restaurant = uiState.restaurant
    val evaluation = uiState.evaluation
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                EvaluationRestaurantInfoCard(
                    restaurant = restaurant
                )
            }

            item {
                EvaluationStar(
                    initialRating = evaluation.evaluationScore,
                    onRatingChanged = { newRating ->
                        viewModel.updateEvaluationScore(newRating)
                    }
                )
            }

            item {
                EvaluationKeyword(
                    selectedSituations = evaluation.evaluationSituations,
                    onSituationChanged = { situation ->
                        viewModel.updateEvaluationSituations(situation)

                    }
                )
            }

            item {
                EvaluationReview(
                    evaluationComment = evaluation.evaluationComment,
                    onCommentChange = { comment ->
                        viewModel.updateEvaluationComment(comment)
                    }
                )
            }

        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        ) {
            KusTopBar(
                leftIcon = painterResource(Res.drawable.ic_left_arrow),
                leftIconModifier = Modifier.noRippleClickable {}
                    .padding(all = 5.dp),
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
}
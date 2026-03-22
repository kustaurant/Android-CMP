package com.kus.feature.draw.ui.result

import UiState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kus.designsystem.component.KusButton
import com.kus.designsystem.component.KusLoadingAnimation
import com.kus.designsystem.component.KusRatingBar
import com.kus.designsystem.component.KusTopBar
import com.kus.designsystem.theme.KusTheme
import com.kus.shared.domain.model.tier.filter.Cuisine
import com.kus.shared.domain.model.tier.filter.Location
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
    viewModel: DrawResultViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val restaurantState = uiState.restaurantState
    val items = (restaurantState as? UiState.Success)?.data.orEmpty()
    val randomIndex = uiState.randomIndex

    val isLoading = restaurantState is UiState.Loading
    val isDrawLocked = isLoading || !uiState.hasPlayedDrawAnimation

    var currentDisplayIndex by rememberSaveable {
        mutableStateOf<Int?>(null)
    }

    BackHandler { onBackClick() }

    LaunchedEffect(Unit) {
        viewModel.initialize(
            locations = initialLocations,
            cuisines = initialCuisines
        )
    }

    LaunchedEffect(randomIndex, uiState.hasPlayedDrawAnimation, items) {
        if (items.isEmpty() || randomIndex == null) return@LaunchedEffect

        currentDisplayIndex =
            if (uiState.hasPlayedDrawAnimation) {
                randomIndex
            } else {
                null
            }
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
            if (restaurantState is UiState.Success && randomIndex != null && items.isNotEmpty()) {
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
                                enabled = !isDrawLocked,
                                buttonName = "다시 뽑기",
                                textStyle = KusTheme.typography.type14sb,
                                modifier = Modifier.width(121.dp),
                                roundedCornerShape = RoundedCornerShape(50.dp),
                                borderColor = if (isDrawLocked) {
                                    KusTheme.colors.c_E0E0E0
                                } else {
                                    KusTheme.colors.c_43AB38
                                },
                                contentColor = KusTheme.colors.c_FFFFFF,
                                leftIcon = painterResource(Res.drawable.ic_retry),
                                onClick = {
                                    if (!isDrawLocked) {
                                        currentDisplayIndex = null
                                        viewModel.redraw()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        },
        containerColor = KusTheme.colors.c_FFFFFF,
    ) { innerPadding ->
        when (restaurantState) {
            UiState.Idle,
            UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    KusLoadingAnimation()
                }
            }

            is UiState.Failure -> {
                DrawNoResultScreen(innerPadding)
            }

            is UiState.Success -> {
                if (items.isEmpty() || randomIndex == null) {
                    DrawNoResultScreen(innerPadding)
                } else {
                    val displayIndex = currentDisplayIndex ?: randomIndex
                    val displayRestaurant = items[displayIndex]
                    val score =
                        ((displayRestaurant.restaurantScore ?: 0.0) * 2).roundToInt() / 2.0

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
                            shouldAnimate = !uiState.hasPlayedDrawAnimation,
                            modifier = Modifier.fillMaxWidth(),
                            onCurrentIndexChanged = { index ->
                                currentDisplayIndex = index
                            },
                            onAnimationFinished = {
                                currentDisplayIndex = randomIndex
                                viewModel.markDrawAnimationPlayed()
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
                            text = displayRestaurant.partnershipInfo
                                ?.takeIf { it.isNotBlank() }
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
}

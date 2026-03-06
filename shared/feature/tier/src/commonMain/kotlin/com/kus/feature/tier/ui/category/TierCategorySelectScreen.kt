package com.kus.feature.tier.ui.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kus.designsystem.component.KusButton
import com.kus.designsystem.component.KusChip
import com.kus.designsystem.component.KusTopBar
import com.kus.designsystem.theme.KusTheme
import com.kus.feature.tier.ui.TierFilterState
import com.kus.feature.tier.ui.config.TierCategoryUiConfig
import kustaurant.shared.core.designsystem.generated.resources.Res
import kustaurant.shared.core.designsystem.generated.resources.ic_arrow_back
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TierCategorySelectScreen(
    modifier: Modifier = Modifier,
    initial: TierFilterState = TierFilterState(),
    onBack: () -> Unit = {},
    onApply: (TierFilterState) -> Unit = {},
) {
    BackHandler { onBack() }

    val viewModel: TierCategorySelectViewModel = koinViewModel()

    LaunchedEffect(initial) {
        viewModel.initIfNeeded(initial)
    }

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KusTheme.colors.c_FFFFFF)
    ) {
        KusTopBar(
            leftIcon = painterResource(Res.drawable.ic_arrow_back),
            modifier = Modifier.height(63.dp),
            onLeftClicked = onBack
        ) {
            Text(
                text = "카테고리",
                style = KusTheme.typography.type16sb,
                color = KusTheme.colors.c_000000
            )
        }

        HorizontalDivider(color = KusTheme.colors.c_E0E0E0)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            CategorySection(
                title = "음식",
                items = TierCategoryUiConfig.cuisineItems,
                isSelected = { it in state.selectedCuisines },
                labelOf = { it.toLabel() },
                onToggle = { item -> viewModel.toggleCuisine(item) }
            )

            Spacer(Modifier.height(22.dp))

            CategorySection(
                title = "상황",
                items = TierCategoryUiConfig.situationItems,
                isSelected = { it in state.selectedSituations },
                labelOf = { it.toLabel() },
                onToggle = { item -> viewModel.toggleSituation(item) }
            )

            Spacer(Modifier.height(22.dp))

            CategorySection(
                title = "위치",
                items = TierCategoryUiConfig.locationItems,
                isSelected = { it in state.selectedLocations },
                labelOf = { it.toLabel() },
                onToggle = { item -> viewModel.toggleLocation(item) }
            )

            Spacer(Modifier.height(28.dp))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            val container =
                if (state.applyEnabled)
                    KusTheme.colors.c_43AB38
                else
                    KusTheme.colors.c_E0E0E0

            KusButton(
                enabled = state.applyEnabled,
                buttonName = "적용하기",
                roundedCornerShape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth() ,
                containerColor = container,
                borderColor = container,
                onClick = { onApply(viewModel.buildResult()) }
            )
        }
    }
}


@Composable
private fun <T> CategorySection(
    title: String,
    items: List<T>,
    isSelected: (T) -> Boolean,
    labelOf: (T) -> String,
    onToggle: (T) -> Unit,
) {
    Text(
        text = title,
        style = KusTheme.typography.type17sb,
        color = KusTheme.colors.c_323232
    )

    Spacer(Modifier.height(13.dp))

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items.forEach { item ->
            KusChip(
                chipName = labelOf(item),
                isSelected = isSelected(item),
                onClick = { onToggle(item) },
                modifier = Modifier.height(29.dp)
            )
        }
    }
}



package com.kus.feature.draw.ui.select

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kus.designsystem.component.KusButton
import com.kus.designsystem.component.KusCategoryImageButton
import com.kus.designsystem.component.KusChip
import com.kus.designsystem.component.KusFadingEdgeLazyRow
import com.kus.designsystem.component.KusTopBar
import com.kus.designsystem.theme.KusTheme
import com.kus.feature.draw.mapper.toImage
import com.kus.shared.domain.model.tier.filter.Cuisine
import com.kus.shared.domain.model.tier.filter.Location
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawSelectScreen(
    onApplyClick: (Set<Location>, Set<Cuisine>) -> Unit,
    onBackClick: () -> Unit,
    viewModel: DrawSelectViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler { onBackClick() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column(
                Modifier.background(KusTheme.colors.c_FFFFFF)
            ) {
                KusTopBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding(horizontal = 8.dp),
                ) {
                    Text(
                        text = "랜덤 맛집 뽑기",
                        style = KusTheme.typography.type17sb,
                        color = KusTheme.colors.c_323232
                    )
                }
            }
        },
        bottomBar = {
            KusButton(
                enabled = true,
                buttonName = "랜덤 뽑기",
                modifier = Modifier
                    .fillMaxWidth()
                    .width(52.dp)
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 20.dp),
                roundedCornerShape = RoundedCornerShape(50.dp),
                onClick = {
                    onApplyClick(
                        uiState.selectedLocations,
                        uiState.selectedCuisines
                    )
                }
            )
        }
    ) { inner ->
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(KusTheme.colors.c_FFFFFF)
                .padding(inner)
        ) {
            item {
                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "건국대 주변 맛집을 랜덤으로 뽑아보세요!",
                    style = KusTheme.typography.type13r,
                    color = KusTheme.colors.c_666666,
                )
            }

            item {
                Spacer(modifier = Modifier.height(26.dp))

                LocationCategoryRow(
                    modifier = Modifier.fillMaxWidth(),
                    allLocations = Location.entries.toList(),
                    selectedLocations = uiState.selectedLocations,
                    onChipClick = viewModel::onLocationChipClick,
                )
            }

            item {
                Spacer(modifier = Modifier.height(26.dp))
            }

            items(
                items = Cuisine.entries.chunked(4),
                key = { rowItems -> rowItems.joinToString(separator = "_") { it.name } }
            ) { rowItems ->
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp),
                ) {
                    rowItems.forEach { cuisine ->
                        KusCategoryImageButton(
                            categoryName = cuisine.toLabel(),
                            categoryImage = painterResource(cuisine.toImage()),
                            isSelected = cuisine in uiState.selectedCuisines,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.onCuisineClick(cuisine) },
                        )
                    }

                    repeat(4 - rowItems.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

        }
    }
}

@Composable
fun LocationCategoryRow(
    allLocations: List<Location>,
    selectedLocations: Set<Location>,
    onChipClick: (Location) -> Unit,
    modifier: Modifier = Modifier,
    fadeWidth: Dp = 16.dp,
) {
    val listState = rememberLazyListState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Spacer(modifier = Modifier.width(16.dp))

        KusFadingEdgeLazyRow(
            state = listState,
            fadeWidth = fadeWidth,
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(end = 0.dp),
        ) {
            items(allLocations, key = { it.name }) { location ->
                KusChip(
                    chipName = location.toLabel(),
                    isSelected = location in selectedLocations,
                    onClick = { onChipClick(location) },
                    modifier = Modifier.height(32.dp)
                )
            }
        }
    }
}
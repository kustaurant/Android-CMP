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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kus.designsystem.component.KusButton
import com.kus.designsystem.component.KusChip
import com.kus.designsystem.component.KusTopBar
import com.kus.designsystem.theme.KusTheme
import com.kus.feature.tier.ui.TierFilterState
import com.kus.shared.domain.model.tier.filter.Cuisine
import com.kus.shared.domain.model.tier.filter.Location
import com.kus.shared.domain.model.tier.filter.Situation
import com.kus.shared.domain.tier.rule.TierFilterRule
import kustaurant.shared.core.designsystem.generated.resources.Res
import kustaurant.shared.core.designsystem.generated.resources.ic_arrow_back
import org.jetbrains.compose.resources.painterResource

@Composable
fun TierCategorySelectScreen(
    modifier: Modifier = Modifier,
    initial: TierFilterState = TierFilterState(),
    onBack: () -> Unit = {},
    onApply: (TierFilterState) -> Unit = {},
) {
    val cuisineAll = Cuisine.ALL
    val cuisinePartnership = Cuisine.PARTNERSHIP
    val cuisineItems = remember {
        listOf(
            Cuisine.ALL,
            Cuisine.KOREAN,
            Cuisine.JAPANESE,
            Cuisine.CHINESE,
            Cuisine.WESTERN,
            Cuisine.ASIAN,
            Cuisine.MEAT,
            Cuisine.CHICKEN,
            Cuisine.SEAFOOD,
            Cuisine.BURGER_PIZZA,
            Cuisine.BUNSIK,
            Cuisine.PUB,
            Cuisine.CAFE_DESSERT,
            Cuisine.BAKERY,
            Cuisine.SALAD,
            Cuisine.PARTNERSHIP,
        )
    }

    val situationAll = Situation.ALL
    val situationItems = remember {
        listOf(
            Situation.ALL,
            Situation.SOLO,
            Situation.TWO_TO_FOUR,
            Situation.FIVE_OR_MORE,
            Situation.COMPANY_DINNER,
            Situation.DELIVERY,
            Situation.LATE_NIGHT,
            Situation.INVITE_FRIENDS,
            Situation.DATE,
            Situation.BLIND_DATE
        )
    }

    val locationAll = Location.ALL
    val locationItems = remember {
        listOf(
            Location.ALL,
            Location.KONKUK_TO_JUNGMUN,
            Location.JUNGMUN_TO_EODAE,
            Location.BACK_GATE,
            Location.FRONT_GATE,
            Location.GUUI_STATION
        )
    }

    val initialNormalized = remember(initial) { initial.normalized() }

    var selectedCuisines by rememberSaveable {
        mutableStateOf(initialNormalized.cuisines.ifEmpty { setOf(cuisineAll) })
    }
    var selectedSituations by rememberSaveable {
        mutableStateOf(initialNormalized.situations.ifEmpty { setOf(situationAll) })
    }
    var selectedLocations by rememberSaveable {
        mutableStateOf(initialNormalized.locations.ifEmpty { setOf(locationAll) })
    }

    val currentFilter = remember(selectedCuisines, selectedSituations, selectedLocations) {
        TierFilterState(
            cuisines = selectedCuisines,
            situations = selectedSituations,
            locations = selectedLocations,
        ).normalized()
    }

    val hasChanges = remember(currentFilter, initialNormalized) {
        currentFilter != initialNormalized
    }

    val isAllGroupsSelected = remember(selectedCuisines, selectedSituations, selectedLocations) {
        selectedCuisines.isNotEmpty() && selectedSituations.isNotEmpty() && selectedLocations.isNotEmpty()
    }

    val applyEnabled = hasChanges && isAllGroupsSelected

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
                items = cuisineItems,
                isSelected = { it in selectedCuisines },
                labelOf = { it.toLabel() },
                onToggle = { item ->
                    selectedCuisines = TierFilterRule.toggleWithAllAndExclusive(
                        current = selectedCuisines,
                        clicked = item,
                        all = cuisineAll,
                        exclusive = setOf(cuisinePartnership),
                    )
                }
            )

            Spacer(Modifier.height(22.dp))

            CategorySection(
                title = "상황",
                items = situationItems,
                isSelected = { it in selectedSituations },
                labelOf = { it.toLabel() },
                onToggle = { item ->
                    selectedSituations = TierFilterRule.toggleWithAll(
                        current = selectedSituations,
                        clicked = item,
                        all = situationAll
                    )
                }
            )

            Spacer(Modifier.height(22.dp))

            CategorySection(
                title = "위치",
                items = locationItems,
                isSelected = { it in selectedLocations },
                labelOf = { it.toLabel() },
                onToggle = { item ->
                    selectedLocations = TierFilterRule.toggleWithAll(
                        current = selectedLocations,
                        clicked = item,
                        all = locationAll
                    )
                }
            )

            Spacer(Modifier.height(28.dp))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            val activeColor = KusTheme.colors.c_43AB38
            val inactiveColor = KusTheme.colors.c_E0E0E0

            val container = if (applyEnabled) activeColor else inactiveColor
            val border = if (applyEnabled) activeColor else inactiveColor

            KusButton(
                enabled = applyEnabled,
                buttonName = "적용하기",
                roundedCornerShape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                containerColor = container,
                borderColor = border,
                onClick = { onApply(currentFilter) }
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



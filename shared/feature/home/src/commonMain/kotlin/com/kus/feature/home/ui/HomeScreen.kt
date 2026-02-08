package com.kus.feature.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kus.designsystem.component.KusCategoryImageButton
import com.kus.designsystem.theme.KusTheme
import com.kus.designsystem.util.noRippleClickable
import com.kus.feature.home.component.HomeBannerPager
import com.kus.feature.home.component.HomeRestaurants
import com.kus.feature.home.component.KusSearchBoxWithNoAction
import com.kus.feature.home.model.Category
import kustaurant.shared.feature.home.generated.resources.Res
import kustaurant.shared.feature.home.generated.resources.ic_alarm
import kustaurant.shared.feature.home.generated.resources.img_home_logo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HomeRoute(
    onSearchNavigate: () -> Unit,
    onAlertNavigate: () -> Unit,
    onTierNavigate: (String) -> Unit,
    onRestaurantDetailNavigate: (Long) -> Unit,
    viewModel: HomeViewModel = viewModel(),
) {

    HomeScreen(
        onSearchBoxClick = onSearchNavigate,
        onAlertButtonClick = onAlertNavigate,
        onCategoryClick = onTierNavigate,
        onRestaurantClick = onRestaurantDetailNavigate,
    )
}

@Composable
private fun HomeScreen(
    onSearchBoxClick: () -> Unit,
    onAlertButtonClick: () -> Unit,
    onCategoryClick: (String) -> Unit,
    onRestaurantClick: (Long) -> Unit,
) {
    val paddedModifier = Modifier.padding(horizontal = 20.dp)

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(KusTheme.colors.c_FFFFFF).padding(top = 16.dp),
    ) {
        stickyHeader() {
            Column(
                modifier = Modifier
                    .background(KusTheme.colors.c_FFFFFF)
                    .then(paddedModifier),
            ) {
                HomeTitleSection(
                    onLogoClick = {},
                    onAlertButtonClick = onAlertButtonClick,
                )

                Spacer(Modifier.height(8.dp))

                KusSearchBoxWithNoAction(
                    onSearchBoxClick = onSearchBoxClick,
                )

                Spacer(Modifier.height(18.dp))
            }
        }

        item {
            HomeBannerPager(
                imageUrls = listOf("", "", ""),
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = "맛집 탐색 카테고리",
                style = KusTheme.typography.type20b,
                modifier = paddedModifier,
            )

            Spacer(Modifier.height(8.dp))
        }

        Category.entries.chunked(4).forEach { rowItems ->
            item {
                Row(
                    modifier = paddedModifier,
                ) {
                    rowItems.forEach { category ->
                        KusCategoryImageButton(
                            categoryName = category.title,
                            categoryImage = painterResource(category.image),
                            modifier = Modifier.weight(1f),
                            onClick = { onCategoryClick(category.id) },
                        )
                    }
                }
            }
        }

        item {

            Spacer(Modifier.height(40.dp))

            HomeRestaurants(
                title = "인증된 건대 TOP 맛집",
                subtitle = "가장 높은 평가를 받은 맛집을 알려드립니다.",
                restaurants = listOf(1,2,3,4,5),
                onRestaurantClick = onRestaurantClick,
            )

            Spacer(Modifier.height(40.dp))
        }

        item {
            HomeRestaurants(
                title = "나를 위한 건대 맛집",
                subtitle = "즐겨찾기를 바탕으로 다른 맛집을 추천해 드립니다",
                restaurants = listOf(1,2,3,4,5),
                onRestaurantClick = onRestaurantClick,
            )

            Spacer(Modifier.height(40.dp))
        }
    }
}

@Composable
fun HomeTitleSection(
    modifier: Modifier = Modifier,
    onLogoClick: () -> Unit,
    onAlertButtonClick: () -> Unit,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(Res.drawable.img_home_logo),
                contentDescription = "앱 로고",
                modifier = Modifier.height(30.dp).noRippleClickable(onLogoClick),
            )
        }

        Icon(
            painter = painterResource(Res.drawable.ic_alarm),
            contentDescription = "알림 버튼",
            modifier = Modifier.size(20.dp).noRippleClickable(onAlertButtonClick)
        )
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    KusTheme {
        HomeScreen(
            onSearchBoxClick = {},
            onAlertButtonClick = {},
            onCategoryClick = {},
            onRestaurantClick = {},
        )
    }
}

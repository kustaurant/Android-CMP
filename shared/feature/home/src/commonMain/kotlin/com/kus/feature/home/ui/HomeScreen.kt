package com.kus.feature.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kus.designsystem.component.KusCategoryImageButton
import com.kus.designsystem.theme.KusTheme
import com.kus.designsystem.util.noRippleClickable
import com.kus.feature.home.component.KusSearchBoxWithNoAction
import com.kus.feature.home.model.Category
import kustaurant.shared.feature.home.generated.resources.Res
import kustaurant.shared.feature.home.generated.resources.ic_alarm
import kustaurant.shared.feature.home.generated.resources.img_home_logo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HomeRoute() {
    HomeScreen()
}

@Composable
private fun HomeScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(KusTheme.colors.c_FFFFFF)
            .padding(20.dp, 16.dp, 20.dp, 0.dp),
    ) {
        stickyHeader() {
            HomeTitleSection(
                onLogoClick = {},
                onAlertButtonClick = {},
            )

            Spacer(Modifier.height(8.dp))

            KusSearchBoxWithNoAction(
                onSearchBoxClick = {},
            )
        }

        item {
            Spacer(Modifier.height(8.dp))

            Box(
                modifier = Modifier.fillMaxWidth().height(100.dp)
                    .background(KusTheme.colors.c_F5F5F5)
            )

            Spacer(Modifier.height(14.dp))

            Text(
                text= "맛집 탐색 카테고리",
                style = KusTheme.typography.type20b
            )

            Spacer(Modifier.height(8.dp))
        }

        Category.entries
            .chunked(4)
            .forEach { rowItems ->
                item {
                    Row() {
                        rowItems.forEach { category ->
                            KusCategoryImageButton(
                                categoryName = category.title,
                                categoryImage = painterResource(category.image),
                                modifier = Modifier.weight(1f),
                                onClick = {},
                            )
                        }
                    }
                }
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
        modifier = modifier
            .fillMaxWidth(),
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
                modifier = Modifier
                    .height(30.dp)
                    .noRippleClickable(onLogoClick),
            )
        }

        Icon(
            painter = painterResource(Res.drawable.ic_alarm),
            contentDescription = "알림 버튼",
            modifier = Modifier.noRippleClickable(onAlertButtonClick)
        )
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    KusTheme {
        HomeScreen()
    }
}
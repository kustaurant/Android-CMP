package com.kus.feature.evaluate.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme
import com.kus.feature.evaluate.ui.EvaluateRestaurant
import kustaurant.shared.core.designsystem.generated.resources.Res
import kustaurant.shared.core.designsystem.generated.resources.ic_location
import org.jetbrains.compose.resources.painterResource

@Composable
fun EvaluationRestInfoCard(
    restaurant: EvaluateRestaurant,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(
        bottomStart = 16.dp,
        bottomEnd = 16.dp
    )
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 22.dp),
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = KusTheme.colors.c_FFFFFF)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .background(
                    color = KusTheme.colors.c_FFFFFF,
                    shape = shape
                )
                .padding(top = 68.dp, bottom = 20.dp)
                .padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                        .padding(end = 25.dp)
                ) {
                    EvaluationRestInfoTag(
                        situationList = restaurant.situationList
                    )

                    Text(
                        text = restaurant.restaurantName,
                        modifier = Modifier.padding(top = 4.dp),
                        style = KusTheme.typography.type18sb.copy(
                            color = KusTheme.colors.c_000000
                        )
                    )

                }

                EvaluationRestInfoRank(
                    mainTier = restaurant.mainTier
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 7.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_location),
                        modifier = Modifier.size(16.dp)
                            .padding(top = 1.dp),
                        contentDescription = null,
                    )

                    Column(
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(
                            text = restaurant.restaurantPosition,
                            style = KusTheme.typography.type14b.copy(
                                color = KusTheme.colors.c_000000
                            )
                        )

                        Text(
                            text = restaurant.restaurantAddress,
                            style = KusTheme.typography.type14m.copy(color = KusTheme.colors.c_666666),
                            modifier = Modifier.padding(top = 4.dp),
                        )
                    }
                }
            }
        }
    }
}

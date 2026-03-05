package com.kus.feature.detail.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme
import com.kus.feature.detail.ui.DetailRestaurantMenu
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kustaurant.shared.feature.detail.generated.resources.Res
import kustaurant.shared.feature.detail.generated.resources.img_rest_example
import org.jetbrains.compose.resources.painterResource

@Composable
fun DetailMenuItem(
    menu: DetailRestaurantMenu,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(KusTheme.colors.c_F5F5F5)
        ) {
            if (menu.menuImgUrl.isBlank() || !menu.menuImgUrl.startsWith("https")) {
                Image(
                    painter = painterResource(Res.drawable.img_rest_example),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(64.dp)
                )
            } else {
                KamelImage(
                    resource = asyncPainterResource(menu.menuImgUrl),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(64.dp),
                    onFailure = {
                        Image(
                            painter = painterResource(Res.drawable.img_rest_example),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(64.dp)
                        )
                    }
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(start = 10.dp)
                .weight(1f),
        ) {
            Text(
                text = menu.menuName,
                style = KusTheme.typography.type16sb.copy(
                    color = KusTheme.colors.c_000000
                )
            )
            Text(
                text = menu.menuPrice,
                style = KusTheme.typography.type14r.copy(
                    color = KusTheme.colors.c_000000
                ),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

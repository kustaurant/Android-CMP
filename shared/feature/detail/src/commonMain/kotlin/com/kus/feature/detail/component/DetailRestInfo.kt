package com.kus.feature.detail.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme
import com.kus.designsystem.util.noRippleClickable
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kustaurant.shared.feature.detail.generated.resources.Res as DetailRes
import kustaurant.shared.feature.detail.generated.resources.Res
import kustaurant.shared.feature.detail.generated.resources.ic_review_star
import kustaurant.shared.feature.detail.generated.resources.img_rest_example
import org.jetbrains.compose.resources.painterResource

@Composable
fun DetailRestInfo(
    modifier: Modifier = Modifier,
    situationList: ArrayList<String> = arrayListOf(),
    mainTier: Int = 1,
    restaurantCuisine: String = "",
    restaurantCuisineImgUrl: String = "",
    restaurantPosition: String = "",
    restaurantName: String = "",
    restaurantAddress: String = "",
    naverMapUrl: String = "",
    partnershipInfo: String = "",
    rating: Double = 0.0,
    evaluationCount: Int = 0,
) {
    val uriHandler = LocalUriHandler.current
    val hasNaverMapUrl = naverMapUrl.isNotBlank()

    Column(
        modifier = modifier.fillMaxWidth()
            .background(
                color = KusTheme.colors.c_FFFFFF,
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp
                )
            )
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f)
                    .padding(end = 25.dp)
            ) {
                DetailRestInfoTag(
                    situationList = situationList
                )

                Text(
                    text = restaurantName,
                    modifier = Modifier.padding(top = 7.dp),
                    style = KusTheme.typography.type18sb.copy(
                        color = KusTheme.colors.c_000000
                    )
                )

                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(DetailRes.drawable.ic_review_star),
                        contentDescription = "평점 아이콘",
                        modifier = Modifier.size(16.dp)
                    )

                    Text(
                        text = rating.toString(),
                        modifier = Modifier.padding(start = 4.dp),
                        style = KusTheme.typography.type14b.copy(
                            color = KusTheme.colors.c_000000
                        )
                    )

                    Text(
                        text = "(평가수 ${if (evaluationCount > 9999) "9999+" else evaluationCount}개)",
                        modifier = Modifier.padding(start = 4.dp),
                        style = KusTheme.typography.type14r.copy(
                            color = KusTheme.colors.c_AAAAAA
                        )
                    )
                }

            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                DetailRestInfoRank(
                    mainTier = mainTier
                )

                Row(
                    modifier = Modifier.padding(top = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (restaurantCuisineImgUrl.startsWith("http")) {
                        KamelImage(
                            resource = { asyncPainterResource(restaurantCuisineImgUrl) },
                            contentDescription = "음식 카테고리 이미지",
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Image(
                            painter = painterResource(Res.drawable.img_rest_example),
                            modifier = Modifier.size(20.dp),
                            contentDescription = "음식 카테고리 이미지"
                        )
                    }

                    Text(
                        text = restaurantCuisine,
                        style = KusTheme.typography.type14r.copy(
                            color = KusTheme.colors.c_666666
                        )
                    )
                }
            }
        }

        Column {
            DetailRestInfoMore(
                title = restaurantPosition,
                content = restaurantAddress,
                modifier = Modifier.padding(top = 20.dp)
            )

            DetailRestInfoMore(
                title = "제휴정보",
                content = partnershipInfo,
                enableSeeMore = true,
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        Text(
            text = "네이버 지도로 이동하기",
            style = KusTheme.typography.type14m.copy(
                color = KusTheme.colors.c_43AB38
            ),
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.padding(top = 17.dp, bottom = 16.dp)
                .noRippleClickable {
                    if (hasNaverMapUrl) {
                        uriHandler.openUri(naverMapUrl)
                    }
                }
        )
    }
}

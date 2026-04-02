package com.kus.feature.detail.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kus.designsystem.theme.KusTheme
import com.kus.designsystem.util.noRippleClickable
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kustaurant.shared.core.designsystem.generated.resources.ic_check
import kustaurant.shared.feature.detail.generated.resources.Res
import kustaurant.shared.feature.detail.generated.resources.ic_review_star
import kustaurant.shared.feature.detail.generated.resources.img_rest_example
import org.jetbrains.compose.resources.painterResource
import kotlin.math.roundToInt
import kustaurant.shared.core.designsystem.generated.resources.Res as CoreRes
import kustaurant.shared.feature.detail.generated.resources.Res as DetailRes

@Composable
fun DetailRestInfo(
    modifier: Modifier = Modifier,
    situationList: List<String> = listOf(),
    mainTier: Int = 1,
    isEvaluated: Boolean = false,
    isTempTier: Boolean = false,
    restaurantCuisine: String = "",
    restaurantCuisineImgUrl: String = "",
    restaurantPosition: String = "",
    restaurantName: String = "",
    restaurantAddress: String = "",
    naverMapUrl: String = "",
    partnershipInfo: String = "",
    rating: Double? = 0.0,
    evaluationCount: Int = 0,
) {
    val uriHandler = LocalUriHandler.current
    val hasNaverMapUrl = naverMapUrl.isNotBlank()
    val roundedRating = rating?.let { (it * 1000).roundToInt() / 1000.0 } ?: 0.0

    Column(
        modifier = modifier.fillMaxWidth()
            .background(
                color = KusTheme.colors.c_FFFFFF,
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp
                )
            )
            .padding(horizontal = 20.dp)
            .padding(top = 6.dp, bottom = 20.dp)
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

                DetailRestInfoName(
                    restaurantName = restaurantName,
                    isEvaluated = isEvaluated,
                    modifier = Modifier.padding(top = 7.dp),
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
                        text = roundedRating.toString(),
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
                if (isTempTier) {
                    Text(
                        text = "AI 임시 티어",
                        style = KusTheme.typography.type10sb.copy(
                            color = KusTheme.colors.c_AAAAAA
                        )
                    )
                }

                DetailRestInfoRank(
                    mainTier = mainTier,
                    isTempTier = isTempTier
                )

                Row(
                    modifier = Modifier.padding(top = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
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

//            DetailRestInfoMore(
//                title = "제휴정보",
//                content = partnershipInfo,
//                enableSeeMore = true,
//                modifier = Modifier.padding(top = 12.dp)
//            )
        }

        Text(
            text = "네이버 지도로 이동하기",
            style = KusTheme.typography.type14m.copy(
                color = KusTheme.colors.c_43AB38
            ),
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.padding(top = 17.dp, bottom = 16.dp)
                .noRippleClickable {
                    val isValidUrl =
                        naverMapUrl.startsWith("https://") || naverMapUrl.startsWith("http://")
                    if (hasNaverMapUrl && isValidUrl) {
                        runCatching { uriHandler.openUri(naverMapUrl) }
                    }
                }
        )
    }
}

@Composable
private fun DetailRestInfoName(
    restaurantName: String,
    isEvaluated: Boolean,
    modifier: Modifier = Modifier,
) {
    val textStyle = KusTheme.typography.type18sb.copy(
        color = KusTheme.colors.c_000000
    )

    if (!isEvaluated) {
        Text(
            text = restaurantName,
            modifier = modifier,
            style = textStyle
        )
        return
    }

    val displayText = buildAnnotatedString {
        append(restaurantName.trimEnd())
        append('\u00A0')
        appendInlineContent(
            id = "evaluated_icon",
            alternateText = "평가완료"
        )
    }

    val inlineContent = mapOf(
        "evaluated_icon" to InlineTextContent(
            placeholder = Placeholder(
                width = 14.sp,
                height = 14.sp,
                placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
            )
        ) {
            Icon(
                painter = painterResource(CoreRes.drawable.ic_check),
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = Color.Unspecified
            )
        }
    )

    Text(
        text = displayText,
        modifier = modifier,
        style = textStyle,
        inlineContent = inlineContent
    )
}

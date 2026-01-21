package com.kus.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme
import kustaurant.shared.core.designsystem.generated.resources.Res
//import kustaurant.core.designsystem.generated.resources.ic_check
//import kustaurant.core.designsystem.generated.resources.ic_kus_blank
//import kustaurant.core.designsystem.generated.resources.ic_location
//import kustaurant.core.designsystem.generated.resources.ic_saved
//import kustaurant.core.designsystem.generated.resources.ic_unsaved
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun KusRestThumbnail(
    modifier: Modifier = Modifier,
    tier: Int? = null,
    restName: String,
    restThumbnail: String? = null,
    restAlliance: String? = null,
    categories: ArrayList<String>? = null,
    location: String? = null,
    isSaved: Boolean,
    isEvaluated: Boolean,
    onClick: () -> Unit = {},
) {
    val locationText = location ?: "위치정보 없음"
    val allianceText = restAlliance ?: "해당사항 없음"
    val tierText = tier?.toString() ?: "-"
    val tierColor = when (tier) {
        1 -> KusTheme.colors.c_0093FF
        2 -> KusTheme.colors.c_01BAA6
        3 -> KusTheme.colors.c_FFB900
        4 -> KusTheme.colors.c_9BA5B0
        else -> KusTheme.colors.c_AAAAAA
    }
    val savedModel = if (isSaved) Res.drawable.ic_saved else Res.drawable.ic_unsaved

    Row(
        modifier = modifier
            .background(
                color = KusTheme.colors.c_FFFFFF,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(11.dp))
        ) {
            if (restThumbnail.isNullOrBlank()) {
                Image(
                    imageVector = vectorResource(Res.drawable.ic_kus_blank),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(80.dp)
                )
            } else {
                KamelImage(
                    resource = asyncPainterResource(restThumbnail),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(80.dp),
                    onFailure = {
                        Image(
                            imageVector = vectorResource(Res.drawable.ic_kus_blank),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(80.dp)
                        )
                    }
                )
            }

            Icon(
                imageVector = vectorResource(savedModel),
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .padding(top = 2.dp),
                tint = Color.Unspecified
            )
        }

        Column(
            modifier = Modifier
                .padding(start = 17.dp, end = 8.dp)
                .weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = restName,
                    style = KusTheme.typography.type16b.copy(
                        color = KusTheme.colors.c_000000
                    )
                )

                if (isEvaluated) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_check),
                        contentDescription = null,
                        modifier = Modifier.padding(start = 6.dp),
                        tint = Color.Unspecified
                    )
                }
            }

            Row(
                modifier = Modifier.padding(top = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_location),
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = KusTheme.colors.c_AAAAAA
                )

                Text(
                    text = locationText,
                    style = KusTheme.typography.type12m.copy(
                        color = KusTheme.colors.c_AAAAAA
                    ),
                    modifier = Modifier.padding(start = 4.dp)
                )
            }


            if (categories != null) {
                val categoryItems = categories.filter { it.isNotBlank() }
                LazyRow(
                    modifier = Modifier
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(categoryItems) { tag ->
                        Text(
                            text = "#$tag",
                            style = KusTheme.typography.type12r.copy(
                                color = KusTheme.colors.c_AAAAAA
                            )
                        )
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    text = "제휴",
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = KusTheme.colors.c_AAAAAA,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .background(
                            color = KusTheme.colors.c_FFFFFF
                        )
                        .padding(horizontal = 4.dp, vertical = 1.dp),
                    style = KusTheme.typography.type10r.copy(
                        color = KusTheme.colors.c_AAAAAA
                    )
                )

                Text(
                    text = allianceText,
                    modifier = Modifier.padding(start = 4.dp),
                    style = KusTheme.typography.type10r.copy(
                        color = KusTheme.colors.c_AAAAAA
                    ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )

            }

        }

        Box(
            modifier = Modifier
                .align(Alignment.Bottom)
                .size(20.dp)
                .border(
                    width = 1.dp,
                    color = tierColor,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(2.dp)
                .background(tierColor, RoundedCornerShape(100.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = tierText,
                style = KusTheme.typography.type16sb.copy(
                    color = KusTheme.colors.c_FFFFFF,
                    lineHeight = androidx.compose.ui.unit.TextUnit.Unspecified
                ),
                textAlign = TextAlign.Center
            )
        }


    }
}

//@Preview
//@Composable
//fun KusRestThumbnailPreview() {
//    KusTheme {
//        KusRestThumbnail(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp),
//            tier = 3,
//            restName = "꾸아 건대점",
//            restAlliance = "어디대 학생증 제시하면 10% 할인이 된다네요 대박 개쩔어",
//            categories = arrayListOf("한식", "분식", "가성비"),
//            location = "서울 성동구",
//            isSaved = true,
//            isEvaluated = true,
//        )
//    }
//}

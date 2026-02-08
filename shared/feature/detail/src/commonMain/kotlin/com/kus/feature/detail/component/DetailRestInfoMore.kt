package com.kus.feature.detail.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme
import kustaurant.shared.core.designsystem.generated.resources.ic_location
import kustaurant.shared.feature.detail.generated.resources.ic_file_check
import org.jetbrains.compose.resources.painterResource
import kustaurant.shared.core.designsystem.generated.resources.Res as CoreRes
import kustaurant.shared.feature.detail.generated.resources.Res as DetailRes

@Composable
fun DetailRestInfoMore(
    title: String,
    content: String,
    enableSeeMore: Boolean = false,
    modifier: Modifier = Modifier,
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val textMeasurer = rememberTextMeasurer()
    val isPartnerInfo = title == "제휴정보"
    val iconRes = if (isPartnerInfo) {
        DetailRes.drawable.ic_file_check
    } else {
        CoreRes.drawable.ic_location
    }
    val titleStyle =
        if (isPartnerInfo) KusTheme.typography.type14m.copy(color = KusTheme.colors.c_AAAAAA)
        else KusTheme.typography.type14m.copy(color = KusTheme.colors.c_666666)
    val seeMoreStyle = KusTheme.typography.type14b.copy(
        color = KusTheme.colors.c_AAAAAA,
        textDecoration = TextDecoration.Underline
    )
    var displayText by remember { mutableStateOf(AnnotatedString(content)) }
    var showSeeMore by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(iconRes),
            modifier = Modifier.size(16.dp)
                .padding(top = 1.dp),
            contentDescription = null,
        )

        Column(
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(
                text = title,
                style = KusTheme.typography.type14b.copy(
                    color = KusTheme.colors.c_000000
                )
            )

            BoxWithConstraints {
                val density = LocalDensity.current
                val maxWidthPx = with(density) { maxWidth.toPx() }.toInt()

                LaunchedEffect(content, maxWidthPx, isExpanded, enableSeeMore) {
                    if (!enableSeeMore || isExpanded || maxWidthPx <= 0) {
                        displayText = AnnotatedString(content)
                        showSeeMore = false
                        return@LaunchedEffect
                    }

                    val baseResult = textMeasurer.measure(
                        text = AnnotatedString(content),
                        style = titleStyle,
                        constraints = Constraints(maxWidth = maxWidthPx),
                        maxLines = 2
                    )

                    if (!baseResult.hasVisualOverflow) {
                        displayText = AnnotatedString(content)
                        showSeeMore = false
                        return@LaunchedEffect
                    }

                    val ellipsis = "… "
                    val moreLabel = "더보기"
                    var cutIndex = content.length

                    while (cutIndex > 0) {
                        val candidateText = content.take(cutIndex).trimEnd()
                        val annotated = buildAnnotatedString {
                            append(candidateText)
                            append(ellipsis)
                            pushStringAnnotation(tag = "more", annotation = "more")
                            withStyle(
                                SpanStyle(
                                    color = seeMoreStyle.color,
                                    textDecoration = TextDecoration.Underline
                                )
                            ) {
                                append(moreLabel)
                            }
                            pop()
                        }
                        val result = textMeasurer.measure(
                            text = annotated,
                            style = titleStyle,
                            constraints = Constraints(maxWidth = maxWidthPx),
                            maxLines = 2
                        )
                        if (!result.hasVisualOverflow) {
                            displayText = annotated
                            showSeeMore = true
                            return@LaunchedEffect
                        }
                        cutIndex -= 1
                    }

                    displayText = buildAnnotatedString {
                        append(ellipsis)
                        pushStringAnnotation(tag = "more", annotation = "more")
                        withStyle(
                            SpanStyle(
                                color = seeMoreStyle.color,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append(moreLabel)
                        }
                        pop()
                    }
                    showSeeMore = true
                }

                if (enableSeeMore && showSeeMore && !isExpanded) {
                    ClickableText(
                        text = displayText,
                        modifier = Modifier.padding(top = 2.dp),
                        style = titleStyle,
                    ) { offset ->
                        val annotations = displayText.getStringAnnotations("more", offset, offset)
                        if (annotations.isNotEmpty()) {
                            isExpanded = true
                        }
                    }
                } else {
                    Text(
                        text = content,
                        style = titleStyle,
                        maxLines = if (enableSeeMore && !isExpanded) 2 else Int.MAX_VALUE,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 2.dp),
                    )
                }
            }
        }
    }
}

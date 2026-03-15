package com.kus.feature.my.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.kus.designsystem.component.KusChip
import com.kus.designsystem.component.KusRatingBar
import com.kus.designsystem.theme.KusTheme
import com.kus.designsystem.util.noRippleClickable
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kustaurant.shared.core.designsystem.generated.resources.Res
import kustaurant.shared.core.designsystem.generated.resources.ic_kus_disable
import org.jetbrains.compose.resources.painterResource

private const val MORE_TAG = "more"

@Composable
internal fun MyReviewItem(
    restaurantName: String,
    restaurantImgURL: String,
    evaluationScore: Float,
    evaluationBody: String,
    evaluationItemScores: List<String>,
    imgUrl: String? = null,
    onItemClick: () -> Unit,
) {
    var isBodyExpanded by remember(evaluationBody) { mutableStateOf(false) }

    val bodyTextStyle = KusTheme.typography.type14r.copy(
        color = KusTheme.colors.c_000000
    )
    val bodyMoreTextStyle = SpanStyle(
        color = KusTheme.colors.c_000000,
        fontFamily = KusTheme.typography.type15sb.fontFamily,
        textDecoration = TextDecoration.Underline
    )

    Column() {

        Row(
            modifier = Modifier.fillMaxWidth().noRippleClickable(onItemClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            KamelImage(
                resource = { asyncPainterResource(restaurantImgURL) },
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(40.dp)
                    .clip(RoundedCornerShape(10.dp)),
                onFailure = {
                    Image(
                        painter = painterResource(Res.drawable.ic_kus_disable),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(40.dp)
                            .clip(RoundedCornerShape(10.dp)),
                    )
                }
            )

            Column(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .weight(1f),
            ) {
                Text(
                    text = restaurantName,
                    style = KusTheme.typography.type14m,
                    color = KusTheme.colors.c_000000,
                )

                Row(
                    modifier = Modifier.padding(top = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    KusRatingBar(
                        rating = evaluationScore,
                        isEnabled = false,
                        starModifier = Modifier.size(24.dp),
                    )
                }
            }
        }

        if (imgUrl != null && imgUrl.startsWith("http")) {
            KamelImage(
                resource = { asyncPainterResource(imgUrl) },
                contentDescription = "리뷰에 첨부된 이미지",
                modifier = Modifier.size(128.dp)
                    .clip(shape = RoundedCornerShape(10.dp))
                    .padding(top = 10.dp),
            )
        }

        ExpandableSeeMoreText(
            text = evaluationBody,
            textStyle = bodyTextStyle,
            moreTextStyle = bodyMoreTextStyle,
            isExpanded = isBodyExpanded,
            maxCollapsedLines = 4,
            onExpandedChange = { isBodyExpanded = it },
            modifier = Modifier.padding(top = 10.dp),
        )

        Spacer(Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            evaluationItemScores.forEach { item ->
                KusChip(
                    chipName = item,
                    isSelected = false,
                    onClick = { /* 클릭 기능 없음 */ },
                )
            }
        }
    }
}

@Composable
internal fun ExpandableSeeMoreText(
    text: String,
    textStyle: TextStyle,
    moreTextStyle: SpanStyle,
    isExpanded: Boolean,
    enableSeeMore: Boolean = true,
    maxCollapsedLines: Int = 2,
    ellipsis: String = "...",
    moreLabel: String = "더보기",
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val textMeasurer = rememberTextMeasurer()
    var displayText by remember(text, ellipsis, moreLabel) { mutableStateOf(AnnotatedString(text)) }
    var showSeeMore by remember(text, ellipsis, moreLabel) { mutableStateOf(false) }

    BoxWithConstraints(modifier = modifier) {
        val density = LocalDensity.current
        val maxWidthPx = with(density) { maxWidth.toPx() }.toInt()

        LaunchedEffect(
            text,
            textStyle,
            moreTextStyle,
            maxWidthPx,
            isExpanded,
            enableSeeMore,
            maxCollapsedLines,
            ellipsis,
            moreLabel
        ) {
            if (!enableSeeMore || isExpanded || maxWidthPx <= 0) {
                displayText = AnnotatedString(text)
                showSeeMore = false
                return@LaunchedEffect
            }

            val baseResult = textMeasurer.measure(
                text = AnnotatedString(text),
                style = textStyle,
                constraints = Constraints(maxWidth = maxWidthPx),
                maxLines = maxCollapsedLines
            )

            if (!baseResult.hasVisualOverflow) {
                displayText = AnnotatedString(text)
                showSeeMore = false
                return@LaunchedEffect
            }

            var cutIndex = text.length
            while (cutIndex > 0) {
                val candidateText = text.take(cutIndex).trimEnd()
                val annotated = buildAnnotatedString {
                    append(candidateText)
                    append(ellipsis)
                    pushStringAnnotation(tag = MORE_TAG, annotation = MORE_TAG)
                    withStyle(moreTextStyle) {
                        append(moreLabel)
                    }
                    pop()
                }

                val result = textMeasurer.measure(
                    text = annotated,
                    style = textStyle,
                    constraints = Constraints(maxWidth = maxWidthPx),
                    maxLines = maxCollapsedLines
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
                pushStringAnnotation(tag = MORE_TAG, annotation = MORE_TAG)
                withStyle(moreTextStyle) {
                    append(moreLabel)
                }
                pop()
            }
            showSeeMore = true
        }

        if (enableSeeMore && showSeeMore && !isExpanded) {
            ClickableText(
                text = displayText,
                style = textStyle,
            ) { offset ->
                val annotations = displayText.getStringAnnotations(MORE_TAG, offset, offset)
                if (annotations.isNotEmpty()) {
                    onExpandedChange(true)
                }
            }
        } else {
            Text(
                text = text,
                style = textStyle,
                maxLines = if (enableSeeMore && !isExpanded) maxCollapsedLines else Int.MAX_VALUE,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

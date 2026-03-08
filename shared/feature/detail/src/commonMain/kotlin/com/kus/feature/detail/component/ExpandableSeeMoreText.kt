package com.kus.feature.detail.component

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Constraints

private const val MORE_TAG = "more"

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

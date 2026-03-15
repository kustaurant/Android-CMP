package com.kus.feature.search.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import com.kus.shared.domain.model.search.HighlightsItem

fun String.applyHighlight(
    highlights: List<HighlightsItem>,
    highlightColor: Color
): AnnotatedString {

    return buildAnnotatedString {
        append(this@applyHighlight)

        highlights.forEach {
            addStyle(
                style = SpanStyle(color = highlightColor),
                start = it.start,
                end = it.end
            )
        }
    }
}

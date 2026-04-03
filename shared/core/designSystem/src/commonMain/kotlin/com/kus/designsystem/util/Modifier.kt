package com.kus.designsystem.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier =
    composed {
        var lastClickTime by remember { mutableStateOf<TimeMark?>(null) }

        clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
        ) {
            val now = TimeSource.Monotonic.markNow()
            val last = lastClickTime
            if (last == null || last.elapsedNow() >= 300L.milliseconds) {
                lastClickTime = now
                onClick()
            }
        }
    }

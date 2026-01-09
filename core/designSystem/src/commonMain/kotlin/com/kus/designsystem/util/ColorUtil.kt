package com.kus.designsystem.util

import androidx.compose.ui.graphics.Color

/**
 * 컬러의 투명도를 퍼센트로 설정하는 확장함수
 * @param percent 투명도 퍼센트 (0-100)
 * @return 해당 퍼센트의 투명도를 가진 Color
 */
fun Color.withOpacityPercent(percent: Int): Color {
    val clampedPercent = percent.coerceIn(0, 100)
    return this.copy(alpha = clampedPercent / 100f)
}

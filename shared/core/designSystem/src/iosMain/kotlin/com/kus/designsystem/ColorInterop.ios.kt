package com.kus.designsystem

import androidx.compose.ui.graphics.Color
import platform.UIKit.UIColor

fun Color.toUIColor(): UIColor =
    UIColor.colorWithRed(
        red = this.red.toDouble(),
        green = this.green.toDouble(),
        blue = this.blue.toDouble(),
        alpha = this.alpha.toDouble(),
    )

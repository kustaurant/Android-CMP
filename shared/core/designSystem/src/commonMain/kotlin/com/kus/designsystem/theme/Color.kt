package com.kus.designsystem.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

val C_43AB38 = Color(0xFF43AB38)
val C_43AB38_20 = Color(0xFFE6F3EA)
val C_046B40 = Color(0xFF046B40)
val C_098C62 = Color(0xFF098C62)
val C_FFFFFF = Color(0xFFFFFFFF)
val C_F5F5F5 = Color(0xFFF5F5F5)
val C_F3F3F3 = Color(0xFFF3F3F3)
val C_EAEAEA = Color(0xFFEAEAEA)
val C_E0E0E0 = Color(0xFFE0E0E0)
val C_AAAAAA = Color(0xFFAAAAAA)
val C_666666 = Color(0xFF666666)
val C_323232 = Color(0xFF323232)
val C_353535 = Color(0xFF353535)
val C_000000 = Color(0xFF000000)
val C_0093FF = Color(0xFF0093FF)
val C_01BAA6 = Color(0xFF01BAA6)
val C_FFB900 = Color(0xFFFFB900)
val C_9BA5B0 = Color(0xFF9BA5B0)
val C_FF0000 = Color(0xFFFF0000)


@Stable
class KusColors(
    c_43AB38: Color,
    c_43AB38_20: Color,
    c_046B40: Color,
    c_098C62: Color,
    c_FFFFFF: Color,
    c_F5F5F5: Color,
    c_F3F3F3: Color,
    c_EAEAEA: Color,
    c_E0E0E0: Color,
    c_AAAAAA: Color,
    c_666666: Color,
    c_323232: Color,
    c_353535: Color,
    c_000000: Color,
    c_0093FF: Color,
    c_01BAA6: Color,
    c_FFB900: Color,
    c_9BA5B0: Color,
    c_FF0000: Color,
) {
    var c_43AB38 by mutableStateOf(c_43AB38)
        private set
    var c_43AB38_20 by mutableStateOf(c_43AB38_20)
        private set
    var c_046B40 by mutableStateOf(c_046B40)
        private set
    var c_098C62 by mutableStateOf(c_098C62)
        private set
    var c_FFFFFF by mutableStateOf(c_FFFFFF)
        private set
    var c_F5F5F5 by mutableStateOf(c_F5F5F5)
        private set
    var c_F3F3F3 by mutableStateOf(c_F3F3F3)
        private set
    var c_EAEAEA by mutableStateOf(c_EAEAEA)
        private set
    var c_E0E0E0 by mutableStateOf(c_E0E0E0)
        private set
    var c_AAAAAA by mutableStateOf(c_AAAAAA)
        private set
    var c_666666 by mutableStateOf(c_666666)
        private set
    var c_323232 by mutableStateOf(c_323232)
        private set
    var c_353535 by mutableStateOf(c_353535)
        private set
    var c_000000 by mutableStateOf(c_000000)
        private set
    var c_0093FF by mutableStateOf(c_0093FF)
        private set
    var c_01BAA6 by mutableStateOf(c_01BAA6)
        private set
    var c_FFB900 by mutableStateOf(c_FFB900)
        private set
    var c_9BA5B0 by mutableStateOf(c_9BA5B0)
        private set
    var c_FF0000 by mutableStateOf(c_FF0000)
        private set

    fun copy(): KusColors = KusColors(
        c_43AB38,
        c_43AB38_20,
        c_046B40,
        c_098C62,
        c_FFFFFF,
        c_F5F5F5,
        c_F3F3F3,
        c_EAEAEA,
        c_E0E0E0,
        c_AAAAAA,
        c_666666,
        c_323232,
        c_353535,
        c_000000,
        c_0093FF,
        c_01BAA6,
        c_FFB900,
        c_9BA5B0,
        c_FF0000,
    )

    fun update(other: KusColors) {
        c_43AB38 = other.c_43AB38
        c_43AB38_20 = other.c_43AB38_20
        c_046B40 = other.c_046B40
        c_098C62 = other.c_098C62
        c_FFFFFF = other.c_FFFFFF
        c_F5F5F5 = other.c_F5F5F5
        c_F3F3F3 = other.c_F3F3F3
        c_EAEAEA = other.c_EAEAEA
        c_E0E0E0 = other.c_E0E0E0
        c_AAAAAA = other.c_AAAAAA
        c_666666 = other.c_666666
        c_323232 = other.c_323232
        c_353535 = other.c_353535
        c_000000 = other.c_000000
        c_0093FF = other.c_0093FF
        c_01BAA6 = other.c_01BAA6
        c_FFB900 = other.c_FFB900
        c_9BA5B0 = other.c_9BA5B0
        c_FF0000 = other.c_FF0000
    }
}

fun KusLightColors(
    c_43AB38: Color = C_43AB38,
    c_43AB38_20: Color = C_43AB38_20,
    c_046B40: Color = C_046B40,
    c_098C62: Color = C_098C62,
    c_FFFFFF: Color = C_FFFFFF,
    c_F5F5F5: Color = C_F5F5F5,
    c_F3F3F3: Color = C_F3F3F3,
    c_EAEAEA: Color = C_EAEAEA,
    c_E0E0E0: Color = C_E0E0E0,
    c_AAAAAA: Color = C_AAAAAA,
    c_666666: Color = C_666666,
    c_323232: Color = C_323232,
    c_353535: Color = C_353535,
    c_000000: Color = C_000000,
    c_0093FF: Color = C_0093FF,
    c_01BAA6: Color = C_01BAA6,
    c_FFB900: Color = C_FFB900,
    c_9BA5B0: Color = C_9BA5B0,
    c_FF0000: Color = C_FF0000,
) = KusColors(
    c_43AB38,
    c_43AB38_20,
    c_046B40,
    c_098C62,
    c_FFFFFF,
    c_F5F5F5,
    c_F3F3F3,
    c_EAEAEA,
    c_E0E0E0,
    c_AAAAAA,
    c_666666,
    c_323232,
    c_353535,
    c_000000,
    c_0093FF,
    c_01BAA6,
    c_FFB900,
    c_9BA5B0,
    c_FF0000,
)

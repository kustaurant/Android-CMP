package com.kus.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import kustaurant.shared.core.designsystem.generated.resources.Res
import kustaurant.shared.core.designsystem.generated.resources.pretendard_bold
import kustaurant.shared.core.designsystem.generated.resources.pretendard_medium
import kustaurant.shared.core.designsystem.generated.resources.pretendard_regular
import kustaurant.shared.core.designsystem.generated.resources.pretendard_semibold
import org.jetbrains.compose.resources.Font

private val PretendardRegular: FontFamily
    @Composable
    get() =
        FontFamily(Font(Res.font.pretendard_regular, FontWeight.Normal))

private val PretendardMedium: FontFamily
    @Composable
    get() =
        FontFamily(Font(Res.font.pretendard_medium, FontWeight.Medium))

private val PretendardSemiBold: FontFamily
    @Composable
    get() =
        FontFamily(Font(Res.font.pretendard_semibold, FontWeight.SemiBold))

private val PretendardBold: FontFamily
    @Composable
    get() =
        FontFamily(Font(Res.font.pretendard_bold, FontWeight.Bold))

private fun KusTextStyle(
    fontFamily: FontFamily,
    fontSize: TextUnit,
    lineHeight: TextUnit = 1.28.em,
    letterSpacing: TextUnit = 0.02.em,
): TextStyle = TextStyle(
    fontFamily = fontFamily,
    fontSize = fontSize,
    lineHeight = lineHeight,
    letterSpacing = letterSpacing,
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    ),
)

@Immutable
class KusTypography(
    val type20b: TextStyle,
    val type18b: TextStyle,
    val type16b: TextStyle,
    val type14b: TextStyle,
    val type13b: TextStyle,
    val type26sb: TextStyle,
    val type18sb: TextStyle,
    val type17sb: TextStyle,
    val type16sb: TextStyle,
    val type15sb: TextStyle,
    val type14sb: TextStyle,
    val type20m: TextStyle,
    val type18m: TextStyle,
    val type16m: TextStyle,
    val type15m: TextStyle,
    val type14m: TextStyle,
    val type13m: TextStyle,
    val type12m: TextStyle,
    val type11m: TextStyle,
    val type16r: TextStyle,
    val type15r: TextStyle,
    val type14r: TextStyle,
    val type13r: TextStyle,
    val type12r: TextStyle,
    val type11r: TextStyle,
    val type10r: TextStyle,
)

@Composable
fun createKusTypography() = KusTypography(
    type20b = KusTextStyle(
        fontFamily = PretendardBold,
        fontSize = 20.sp,
    ),
    type18b = KusTextStyle(
        fontFamily = PretendardBold,
        fontSize = 18.sp,
    ),
    type16b = KusTextStyle(
        fontFamily = PretendardBold,
        fontSize = 16.sp,
    ),
    type14b = KusTextStyle(
        fontFamily = PretendardBold,
        fontSize = 14.sp
    ),
    type13b = KusTextStyle(
        fontFamily = PretendardBold,
        fontSize = 13.sp,
    ),
    type26sb = KusTextStyle(
        fontFamily = PretendardSemiBold,
        fontSize = 26.sp
    ),
    type18sb = KusTextStyle(
        fontFamily = PretendardSemiBold,
        fontSize = 18.sp,
    ),
    type17sb = KusTextStyle(
        fontFamily = PretendardSemiBold,
        fontSize = 17.sp,
    ),
    type16sb = KusTextStyle(
        fontFamily = PretendardSemiBold,
        fontSize = 16.sp,
    ),
    type15sb = KusTextStyle(
        fontFamily = PretendardSemiBold,
        fontSize = 15.sp,
    ),
    type14sb = KusTextStyle(
        fontFamily = PretendardSemiBold,
        fontSize = 14.sp,
    ),
    type20m = KusTextStyle(
        fontFamily = PretendardMedium,
        fontSize = 20.sp,
    ),
    type18m = KusTextStyle(
        fontFamily = PretendardMedium,
        fontSize = 18.sp,
    ),
    type16m = KusTextStyle(
        fontFamily = PretendardMedium,
        fontSize = 16.sp,
    ),
    type15m = KusTextStyle(
        fontFamily = PretendardMedium,
        fontSize = 15.sp,
    ),
    type14m = KusTextStyle(
        fontFamily = PretendardMedium,
        fontSize = 14.sp
    ),
    type12m = KusTextStyle(
        fontFamily = PretendardMedium,
        fontSize = 12.sp,
    ),
    type11m = KusTextStyle(
        fontFamily = PretendardMedium,
        fontSize = 11.sp,
    ),
    type16r = KusTextStyle(
        fontFamily = PretendardRegular,
        fontSize = 16.sp,
    ),
    type15r = KusTextStyle(
        fontFamily = PretendardRegular,
        fontSize = 15.sp,
    ),
    type14r = KusTextStyle(
        fontFamily = PretendardRegular,
        fontSize = 14.sp,
    ),
    type13r = KusTextStyle(
        fontFamily = PretendardRegular,
        fontSize = 13.sp,
    ),
    type13m = KusTextStyle(
        fontFamily = PretendardMedium,
        fontSize = 13.sp,
    ),
    type12r = KusTextStyle(
        fontFamily = PretendardRegular,
        fontSize = 12.sp,
    ),
    type11r = KusTextStyle(
        fontFamily = PretendardRegular,
        fontSize = 11.sp,
    ),
    type10r = KusTextStyle(
        fontFamily = PretendardRegular,
        fontSize = 10.sp,
    ),
)

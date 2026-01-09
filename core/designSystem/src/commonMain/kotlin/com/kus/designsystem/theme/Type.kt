package com.kus.designsystem.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import kustaurant.core.designsystem.generated.resources.Res
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.Font

val Pretendard = FontFamily(
    Font(
        resource = Res.font.pretendard_regular,
        weight = FontWeight.Normal,
    ),
    Font(
        resource = Res.font.pretendard_medium,
        weight = FontWeight.Medium
    ),
    Font(
        resource = Res.font.pretendard_semibold,
        weight = FontWeight.SemiBold
    ),
    Font(
        resource = Res.font.pretendard_bold,
        weight = FontWeight.Bold
    )
)
@Immutable
class KusTypography(
    val type20b: TextStyle,
    val type16b: TextStyle,
    val type13b: TextStyle,
    val type18sb: TextStyle,
    val type17sb: TextStyle,
    val type16sb: TextStyle,
    val type15sb: TextStyle,
    val type18m: TextStyle,
    val type16m: TextStyle,
    val type15m: TextStyle,
    val type12m: TextStyle,
    val type11m: TextStyle,
    val type14r: TextStyle,
    val type13r: TextStyle,
    val type12r: TextStyle,
    val type11r: TextStyle,
    val type10r: TextStyle,
)

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

fun KusTypography() = KusTypography(
    type20b = KusTextStyle(
        fontFamily = PretendardBold,
        fontSize = 20.sp,
    ),
    type16b = KusTextStyle(
        fontFamily = PretendardBold,
        fontSize = 16.sp,
    ),
    type13b = KusTextStyle(
        fontFamily = PretendardBold,
        fontSize = 13.sp,
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
    type12m = KusTextStyle(
        fontFamily = PretendardMedium,
        fontSize = 12.sp,
    ),
    type11m = KusTextStyle(
        fontFamily = PretendardMedium,
        fontSize = 11.sp,
    ),
    type14r = KusTextStyle(
        fontFamily = PretendardRegular,
        fontSize = 14.sp,
    ),
    type13r = KusTextStyle(
        fontFamily = PretendardRegular,
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

@Preview(showBackground = true)
@Composable
fun KusTypographyPreview() {
    KusTheme {
        Column {
            Text("type20b - KusTheme", style = KusTheme.typography.type20b)
            Text("type13b - KusTheme", style = KusTheme.typography.type13b)
            Text("type18sb - KusTheme", style = KusTheme.typography.type18sb)
            Text("type17sb - KusTheme", style = KusTheme.typography.type17sb)
            Text("type18m - KusTheme", style = KusTheme.typography.type18m)
            Text("type16m - KusTheme", style = KusTheme.typography.type16m)
            Text("type15m - KusTheme", style = KusTheme.typography.type15m)
            Text("type12m - KusTheme", style = KusTheme.typography.type12m)
            Text("type11m - KusTheme", style = KusTheme.typography.type11m)
            Text("type14r - KusTheme", style = KusTheme.typography.type14r)
            Text("type13r - KusTheme", style = KusTheme.typography.type13r)
            Text("type12r - KusTheme", style = KusTheme.typography.type12r)
        }
    }
}

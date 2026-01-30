package com.kus.feature.tier.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kus.designsystem.theme.KusTheme
import kustaurant.shared.feature.tier.generated.resources.Res
import kustaurant.shared.feature.tier.generated.resources.ic_crown
import org.jetbrains.compose.resources.painterResource

@Composable
fun TierInfoPopup(
    onDismiss: () -> Unit,
) {
    val accent = KusTheme.colors.c_43AB38

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .widthIn(max = 250.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
                    .background(Color.White, RoundedCornerShape(13.dp))
                    .padding(horizontal = 15.dp, vertical = 15.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "티어란?",
                        style = KusTheme.typography.type17sb.copy(
                            color = KusTheme.colors.c_323232
                        )
                    )

                    HorizontalDivider(thickness = 1.dp, color = KusTheme.colors.c_323232, modifier = Modifier.width(68.dp))

                    Spacer(Modifier.height(24.dp))

                    Text(
                        text = buildAnnotatedString {
                            append("식당의 티어는 여러분이 부여한 소중한 ")
                            withStyle(SpanStyle(color = accent, fontWeight = FontWeight.SemiBold)) {
                                append("평가 점수")
                            }
                            append("가 반영되어 결정됩니다!\n\n")

                            append("평가는 각 식당의 상세 페이지에서 할 수 있으며, 점수는 ")
                            withStyle(SpanStyle(color = accent, fontWeight = FontWeight.SemiBold)) {
                                append("5점 만점")
                            }
                            append("입니다.\n\n")

                            append("식당에 대한 여러 ")
                            withStyle(SpanStyle(color = accent, fontWeight = FontWeight.SemiBold)) {
                                append("평가의 평균 점수")
                            }
                            append("가 가장 높은 ")
                            withStyle(SpanStyle(color = accent, fontWeight = FontWeight.SemiBold)) {
                                append("식당 순")
                            }
                            append("으로 티어가 산출되어 우선적으로 노출됩니다!")
                        },
                        style = KusTheme.typography.type14r.copy(
                            color = KusTheme.colors.c_666666,
                            lineHeight = 18.sp,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.padding(bottom = 15.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(61.dp)
                    .align(Alignment.TopCenter)
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.ic_crown),
                    contentDescription = null,
                    modifier = Modifier.size(39.dp)
                )
            }
        }
    }
}

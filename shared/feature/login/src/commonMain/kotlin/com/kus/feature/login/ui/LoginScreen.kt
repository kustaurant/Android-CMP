package com.kus.feature.login.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kustaurant.shared.core.designsystem.generated.resources.ic_kus_logo
import org.jetbrains.compose.resources.painterResource
import kustaurant.shared.core.designsystem.generated.resources.Res as CoreRes

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onKakaoClick: () -> Unit,
    onNaverClick: () -> Unit,
    onSkipClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(24.dp))

            Image(
                painter = painterResource(CoreRes.drawable.ic_kus_logo),
                contentDescription = "logo",
                modifier = Modifier
                    .size(74.dp)
                    .clip(CircleShape)
                    .border(0.dp, Color.Transparent, CircleShape)
            )

            Spacer(Modifier.height(18.dp))

            Image(
                painter = painterResource(CoreRes.drawable.ic_logo_title),
                contentDescription = "title",
                modifier = Modifier
                    .height(64.dp)
                    .wrapContentWidth()
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = "건대생을 위한 맛집 리스트",
                color = Color(0xFF43AB38),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(34.dp))

            LoginDivider(
                text = "간편 로그인",
                modifier = Modifier.fillMaxWidth(),
                lineColor = Color(0xFF3A3A3A),
                textColor = Color(0xFF7A7A7A),
            )

            Spacer(Modifier.height(18.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(22.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SocialCircleButton(
                    iconRes = CoreRes.drawable.ic_naver_logo,
                    contentDescription = "naver",
                    backgroundColor = Color(0xFF03C75A),
                    onClick = onNaverClick
                )
            }

            Spacer(Modifier.height(22.dp))

            Text(
                text = "건너뛰기",
                color = Color(0xFF8A8A8A),
                fontSize = 13.sp,
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onSkipClick() }
                    .padding(vertical = 8.dp)
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun LoginDivider(
    text: String,
    modifier: Modifier = Modifier,
    lineColor: Color,
    textColor: Color,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(lineColor)
        )
        Text(
            text = text,
            color = textColor,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(lineColor)
        )
    }
}

@Composable
private fun SocialCircleButton(
    iconRes: Int,
    contentDescription: String,
    backgroundColor: Color,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(resource = iconRes),
            contentDescription = contentDescription,
            modifier = Modifier.size(28.dp)
        )
    }
}

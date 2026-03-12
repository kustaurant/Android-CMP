package com.kus.feature.login.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme
import com.kus.designsystem.util.noRippleClickable
import kustaurant.shared.core.designsystem.generated.resources.ic_kus_logo
import kustaurant.shared.core.designsystem.generated.resources.ic_logo_title
import kustaurant.shared.core.designsystem.generated.resources.ic_naver_logo
import kustaurant.shared.feature.login.generated.resources.Res
import kustaurant.shared.feature.login.generated.resources.bg_login
import org.jetbrains.compose.resources.painterResource
import kustaurant.shared.core.designsystem.generated.resources.Res as CoreRes

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onNaverClick: () -> Unit,
    onSkipLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.bg_login),
            contentDescription = "쿠스토랑 로그인 배경화면입니다.",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(
                modifier = Modifier.weight(0.65f)
            )

            Image(
                painter = painterResource(CoreRes.drawable.ic_kus_logo),
                contentDescription = "쿠스토랑 로고입니다.",
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .border(0.dp, Color.Transparent, CircleShape)
            )

            Spacer(
                Modifier
                .height(20.dp)
            )

            Image(
                painter = painterResource(CoreRes.drawable.ic_logo_title),
                contentDescription = "쿠스토랑 타이틀입니다.",
                modifier = Modifier
                    .height(64.dp)
                    .wrapContentWidth()
            )

            Spacer(Modifier.height(14.dp))

            Text(
                text = "건대생을 위한 맛집 리스트",
                style = KusTheme.typography.type20m,
                color = KusTheme.colors.c_43AB38,
            )

            Spacer(Modifier.height(30.dp))

            LoginDivider(
                text = "간편 로그인",
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(22.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SocialCircleButton(
                    iconPainter = painterResource(CoreRes.drawable.ic_naver_logo),
                    contentDescription = "네이버 로그인 버튼입니다.",
                    onClick = onNaverClick
                )
            }

            Spacer(Modifier.height(22.dp))

            Text(
                text = "건너뛰기",
                style = KusTheme.typography.type12m,
                color = KusTheme.colors.c_AAAAAA,
                modifier = Modifier
                    .noRippleClickable{
                        onSkipLogin()
                        onNavigateToHome()
                    }
                    .padding(vertical = 8.dp)
            )

            Spacer(Modifier.height(30.dp))
        }
    }
}

@Composable
private fun LoginDivider(
    text: String,
    modifier: Modifier = Modifier,
) {
    val color = KusTheme.colors.c_666666
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(2.dp)
                .padding(start = 32.dp)
                .background(color)
        )
        Text(
            text = text,
            color = color,
            style = KusTheme.typography.type12m,
            modifier = Modifier.padding(horizontal = 13.dp)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(2.dp)
                .padding(end = 32.dp)
                .background(color)
        )
    }
}

@Composable
private fun SocialCircleButton(
    iconPainter: Painter,
    contentDescription: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(58.dp)
            .clip(CircleShape)
            .noRippleClickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = iconPainter,
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize()
        )
    }
}

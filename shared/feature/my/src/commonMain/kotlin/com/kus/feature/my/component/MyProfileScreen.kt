package com.kus.feature.my.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme
import com.kus.designsystem.util.noRippleClickable
import com.kus.feature.my.ui.type.AccountMenu
import com.kus.feature.my.ui.type.ServiceMenu
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
internal fun MyProfileScreen(
    userName: String,
    userImgUrl: String,
    modifier: Modifier = Modifier,
    isGuest: Boolean = false,
    onEditProfileClick: () -> Unit,
    onNoticeClick: () -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onFeedbackClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(KusTheme.colors.c_F3F3F3),
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .background(KusTheme.colors.c_FFFFFF)
                    .padding(horizontal = 30.dp, vertical = 20.dp),
            ) {
                KamelImage(
                    resource = { asyncPainterResource(userImgUrl) },
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(50.dp)),
                    onFailure = {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(50.dp))
                                .background(KusTheme.colors.c_F5F5F5),
                            contentAlignment = Alignment.Center,
                        ) {}
                    }
                )

                Spacer(Modifier.width(12.dp))

                if (isGuest) {
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                    ) {
                        Text(
                            text = "로그인이 필요한 서비스입니다.",
                            textDecoration = TextDecoration.Underline,
                            style = KusTheme.typography.type12r,
                            color = KusTheme.colors.c_AAAAAA,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.noRippleClickable(onEditProfileClick),
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = "안녕하세요,\n",
                            style = KusTheme.typography.type14r,
                            color = KusTheme.colors.c_666666,
                        )

                        Text(
                            text = "$userName 님",
                            style = KusTheme.typography.type20b,
                            color = KusTheme.colors.c_046B40,
                        )

                        Text(
                            text = "프로필 편집",
                            textDecoration = TextDecoration.Underline,
                            style = KusTheme.typography.type12r,
                            color = KusTheme.colors.c_AAAAAA,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.noRippleClickable(onEditProfileClick),
                        )
                    }
                }
            }
        }

        item {
            MenuList(
                title = "서비스",
                items = ServiceMenu.entries,
                onClick = { menu ->
                    when (menu) {
                        ServiceMenu.NOTICE -> onNoticeClick()
                        ServiceMenu.TERMS_OF_SERVICE -> onTermsClick()
                        ServiceMenu.PRIVACY_POLICY -> onPrivacyPolicyClick()
                        ServiceMenu.SEND_FEEDBACK -> onFeedbackClick()
                    }
                }
            )
        }

        item {
            MenuList(
                title = "계정",
                items = AccountMenu.entries,
                onClick = { menu ->
                    when (menu) {
                        AccountMenu.LOGOUT -> onLogoutClick()
                        AccountMenu.DELETE_ACCOUNT -> onDeleteAccountClick()
                    }
                }
            )
        }
    }
}

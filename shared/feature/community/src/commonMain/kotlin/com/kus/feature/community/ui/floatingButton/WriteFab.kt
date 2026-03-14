package com.kus.feature.community.ui.floatingButton

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme
import kustaurant.shared.core.designsystem.generated.resources.ic_pencil
import org.jetbrains.compose.resources.painterResource
import kustaurant.shared.core.designsystem.generated.resources.Res as CoreRes

@Composable
fun WriteFab(onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(999.dp),
        color = KusTheme.colors.c_43AB38,
        shadowElevation = 6.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 22.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("글쓰기", color = Color.White, style = KusTheme.typography.type15sb)
            Spacer(Modifier.width(6.dp))
            Image(
                painter = painterResource(CoreRes.drawable.ic_pencil),
                contentDescription = "글 쓰기 아이콘입니다."
            )
        }
    }
}
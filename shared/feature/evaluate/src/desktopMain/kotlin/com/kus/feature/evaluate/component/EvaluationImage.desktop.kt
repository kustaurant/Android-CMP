package com.kus.feature.evaluate.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kustaurant.shared.feature.evaluate.generated.resources.Res
import kustaurant.shared.feature.evaluate.generated.resources.ic_image_add
import org.jetbrains.compose.resources.painterResource

@Composable
actual fun EvaluationImage(
    imageUrl: String,
    imageBytes: ByteArray?,
    onImageSelected: (ByteArray) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 30.dp)
            .height(150.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(
                shape = RoundedCornerShape(16.dp),
                width = 1.dp,
                color = KusTheme.colors.c_AAAAAA
            )
    ) {
        when {
            imageUrl.isNotEmpty() -> {
                KamelImage(
                    resource = asyncPainterResource(imageUrl),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            else -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(Res.drawable.ic_image_add),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )

                    Text(
                        text = "사진 추가하기",
                        style = KusTheme.typography.type13r.copy(
                            color = KusTheme.colors.c_AAAAAA
                        ),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

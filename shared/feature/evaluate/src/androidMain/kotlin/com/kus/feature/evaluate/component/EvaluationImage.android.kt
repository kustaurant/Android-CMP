package com.kus.feature.evaluate.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme
import com.kus.designsystem.util.noRippleClickable
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import com.preat.peekaboo.image.picker.toImageBitmap
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kustaurant.shared.feature.evaluate.generated.resources.Res
import kustaurant.shared.feature.evaluate.generated.resources.ic_edit
import kustaurant.shared.feature.evaluate.generated.resources.ic_image_add
import org.jetbrains.compose.resources.painterResource

@Composable
actual fun EvaluationImage(
    imageUrl: String,
    imageBytes: ByteArray?,
    onImageSelected: (ByteArray) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val imagePicker = rememberImagePickerLauncher(
        selectionMode = SelectionMode.Single,
        scope = scope,
        onResult = { byteArrays ->
            byteArrays.firstOrNull()?.let { onImageSelected(it) }
        }
    )

    when {
        imageBytes != null -> {
            EvaluationImageFrame(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                onClick = { imagePicker.launch() }
            ) {
                Image(
                    bitmap = imageBytes.toImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                EditIconBadge()
            }
        }

        imageUrl.isNotEmpty() -> {
            EvaluationImageFrame(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                onClick = { imagePicker.launch() }
            ) {
                KamelImage(
                    resource = { asyncPainterResource(imageUrl) },
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                EditIconBadge()
            }
        }

        else -> {
            EvaluationImageFrame(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                onClick = { imagePicker.launch() }
            ) {
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

@Composable
private fun EvaluationImageFrame(
    modifier: Modifier,
    onClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .padding(top = 30.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(
                shape = RoundedCornerShape(16.dp),
                width = 1.dp,
                color = KusTheme.colors.c_AAAAAA
            )
            .noRippleClickable(onClick),
        content = content
    )
}

@Composable
private fun BoxScope.EditIconBadge() {
    Box(
        modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(top = 14.dp, end = 14.dp)
            .background(
                color = KusTheme.colors.c_323232.copy(alpha = 0.8f),
                shape = RoundedCornerShape(100.dp)
            )
            .border(
                width = 1.dp,
                color = KusTheme.colors.c_FFFFFF,
                shape = RoundedCornerShape(100.dp)
            )
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_edit),
            contentDescription = null,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.Center)
        )
    }
}

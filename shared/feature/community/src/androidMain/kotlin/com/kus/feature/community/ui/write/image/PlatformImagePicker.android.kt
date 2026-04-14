package com.kus.feature.community.ui.write.image

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.preat.peekaboo.image.picker.ResizeOptions
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher

@Composable
actual fun rememberImagePicker(
    onImagePicked: (ByteArray) -> Unit
): ImagePicker {
    val scope = rememberCoroutineScope()

    val launcher = rememberImagePickerLauncher(
        selectionMode = SelectionMode.Single,
        scope = scope,
        resizeOptions = ResizeOptions(
            width = 1024,
            height = 1024,
            resizeThresholdBytes = 1 * 1024 * 1024L,
            compressionQuality = 0.8
        ),
        onResult = { list ->
            list.firstOrNull()?.let(onImagePicked)
        }
    )

    return remember {
        object : ImagePicker {
            override fun launch() {
                launcher.launch()
            }
        }
    }
}
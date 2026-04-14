package com.kus.feature.community.ui.write.image

import androidx.compose.runtime.Composable

interface ImagePicker {
    fun launch()
}

@Composable
expect fun rememberImagePicker(
    onImagePicked: (ByteArray) -> Unit
): ImagePicker
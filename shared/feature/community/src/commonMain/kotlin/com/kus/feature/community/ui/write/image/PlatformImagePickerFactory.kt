package com.kus.feature.community.ui.write.image

import androidx.compose.runtime.Composable

interface PlatformImagePickerFactory {
    @Composable
    fun rememberPicker(): PlatformImagePicker
}
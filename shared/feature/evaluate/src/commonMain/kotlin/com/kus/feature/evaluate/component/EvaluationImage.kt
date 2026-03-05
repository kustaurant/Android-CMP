package com.kus.feature.evaluate.component

import androidx.compose.runtime.Composable

@Composable
expect fun EvaluationImage(
    imageUrl: String,
    imageBytes: ByteArray?,
    onImageSelected: (ByteArray) -> Unit,
)

package com.kus.data.community.model

data class UploadImageData(
    val bytes: ByteArray,
    val fileName: String,
    val mimeType: String,
)


package com.kus.data.community.model

data class ResolvedImage(
    val bytes: ByteArray,
    val mimeType: String,
    val fileName: String,
)
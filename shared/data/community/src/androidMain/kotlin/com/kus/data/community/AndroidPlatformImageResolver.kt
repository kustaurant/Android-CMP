package com.kus.data.community

import android.content.Context
import android.provider.OpenableColumns
import com.kus.data.community.model.ResolvedImage
import androidx.core.net.toUri
import com.kus.domain.community.model.UploadImageException

class AndroidPlatformImageResolver(
    private val context: Context,
    private val maxBytes: Long = 1_048_576L,
) : PlatformImageResolver {

    override suspend fun resolve(imagePath: String): ResolvedImage {
        val uri = imagePath.toUri()
        val resolver = context.contentResolver

        val mime = resolver.getType(uri) ?: "image/*"
        val bytes = resolver.openInputStream(uri)?.use { it.readBytes() }
            ?: throw UploadImageException.ReadFailed()

        if (bytes.size.toLong() > maxBytes) {
            throw UploadImageException.TooLarge(maxBytes)
        }

        val fileName: String = resolver
            .query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
            ?.use { cursor ->
                if (cursor.moveToFirst()) {
                    cursor.getString(
                        cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
                    )
                } else null
            }
            ?.takeIf { it.isNotBlank() }
            ?: "image_${System.currentTimeMillis()}.jpg"

        return ResolvedImage(bytes = bytes, mimeType = mime, fileName = fileName)
    }
}
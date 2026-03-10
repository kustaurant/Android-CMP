package com.kus.appkit.community.util

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class)
fun NSData.toByteArray(): ByteArray {
    val size = length.toInt()
    val byteArray = ByteArray(size)

    memScoped {
        byteArray.usePinned { pinned ->
            memcpy(
                pinned.addressOf(0),
                this@toByteArray.bytes,
                size.convert()
            )
        }
    }

    return byteArray
}

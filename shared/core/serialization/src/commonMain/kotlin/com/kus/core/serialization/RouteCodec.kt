package com.kus.core.serialization

import kotlin.io.encoding.Base64

object RouteCodec {
    fun encode(json: String): String =
        Base64.UrlSafe.encode(json.encodeToByteArray())

    fun decode(encoded: String): String =
        Base64.UrlSafe.decode(encoded).decodeToString()
}
package com.kus.core.serialization

import kotlinx.serialization.json.Json

object KusJson {
    val json: Json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        explicitNulls = false
        isLenient = true
    }
}

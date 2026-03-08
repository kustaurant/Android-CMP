package com.kus.kustaurant.evaluate.api

import com.kus.kustaurant.evaluate.remote.request.EvaluationRequest
import com.kus.kustaurant.evaluate.remote.response.EvaluationResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class EvaluateApi(
    private val client: HttpClient,
    private val json: Json,
) {
    suspend fun getEvaluation(restaurantId: Long): EvaluationResponse {
        return client.get("/api/v2/auth/restaurants/$restaurantId/evaluation").body()
    }

    suspend fun postEvaluation(
        restaurantId: Long,
        request: EvaluationRequest,
        imageBytes: ByteArray?,
    ) {
        if (imageBytes != null) {
            client.post("/api/v2/auth/restaurants/$restaurantId/evaluation") {
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append(
                                key = "evaluationDTO",
                                value = json.encodeToString(request),
                                headers = Headers.build {
                                    append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                                }
                            )
                            append(
                                key = "newImage",
                                value = imageBytes,
                                headers = Headers.build {
                                    append(HttpHeaders.ContentType, "image/jpeg")
                                    append(HttpHeaders.ContentDisposition, "filename=evaluation.jpg")
                                }
                            )
                        }
                    )
                )
            }
        } else {
            client.post("/api/v2/auth/restaurants/$restaurantId/evaluation") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }
    }
}

package com.kus.kustaurant.evaluate.api

import com.kus.data.network.ApiClientProvider
import com.kus.kustaurant.evaluate.remote.request.EvaluationRequest
import com.kus.kustaurant.evaluate.remote.response.EvaluationResponse
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

class EvaluateApi(
    private val apiClientProvider: ApiClientProvider,
) {
    private val client get() = apiClientProvider.client

    suspend fun getEvaluation(restaurantId: Long): EvaluationResponse {
        return client.get("/api/v2/auth/restaurants/$restaurantId/evaluation").body()
    }

    suspend fun postEvaluation(
        restaurantId: Long,
        request: EvaluationRequest,
        imageBytes: ByteArray?,
    ) {
        client.post("/api/v2/auth/restaurants/$restaurantId/evaluation") {
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("evaluationScore", request.evaluationScore.toString())

                        request.evaluationSituations?.forEach { situation ->
                            append("evaluationSituations", situation.toString())
                        }

                        request.evaluationComment?.let {
                            append("evaluationComment", it)
                        }

                        imageBytes?.let {
                            append(
                                key = "newImage",
                                value = it,
                                headers = Headers.build {
                                    append(HttpHeaders.ContentType, "image/jpeg")
                                    append(HttpHeaders.ContentDisposition, "filename=evaluation.jpg")
                                }
                            )
                        }
                    }
                )
            )
        }
    }
}

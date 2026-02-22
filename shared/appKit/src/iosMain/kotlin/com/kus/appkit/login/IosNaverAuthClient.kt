@file:OptIn(ExperimentalForeignApi::class)

package com.kus.appkit.login

import cocoapods.NaverBridge.NaverLoginBridge
import com.kus.feature.login.SocialAuthClient
import com.kus.feature.login.model.NaverAuthPayload
import com.kus.feature.login.model.NaverAuthResult
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class IosNaverAuthClient : SocialAuthClient {
    override suspend fun login(): NaverAuthResult =
        suspendCancellableCoroutine { cont ->
            val bridge = NaverLoginBridge.shared()

            bridge.loginOnSuccess(
                onSuccess = { providerId: String?, accessToken: String? ->
                    val pid = providerId?.trim().orEmpty()
                    val token = accessToken?.trim().orEmpty()

                    if (pid.isEmpty() || token.isEmpty()) {
                        cont.resume(NaverAuthResult.Failure("INVALID_DATA", "providerId/accessToken is null or empty"))
                    } else {
                        cont.resume(
                            NaverAuthResult.Success(
                                NaverAuthPayload(providerId = pid, accessToken = token)
                            )
                        )
                    }
                },
                onCancel = { reason: String? ->
                    cont.resume(NaverAuthResult.Cancelled(reason))
                },
                onFailure = { code: String?, message: String? ->
                    cont.resume(NaverAuthResult.Failure(code ?: "LOGIN_ERROR", message))
                },
            )
        }
}

package com.kus.feature.login

import android.app.Activity
import com.kus.feature.login.model.SocialLoginResult
import com.navercorp.nid.NidOAuth
import com.navercorp.nid.oauth.util.NidOAuthCallback

class AndroidNaverLoginPlatform(
    private val activity: Activity,
) : NaverLoginPlatform {

    override suspend fun login(): SocialLoginResult =
        kotlinx.coroutines.suspendCancellableCoroutine { cont ->

            val callback = object : NidOAuthCallback {
                override fun onSuccess() {
                    val token = NidOAuth.getAccessToken()
                    if (token.isNullOrBlank()) {
                        cont.resume(
                            SocialLoginResult.Failure(
                                code = "EMPTY_TOKEN",
                                message = "NidOAuth.getAccessToken() returned null/blank"
                            ),
                            onCancellation = {}
                        )
                    } else {
                        cont.resume(SocialLoginResult.Success(token), onCancellation = {})
                    }
                }

                override fun onFailure(errorCode: String, errorDesc: String) {
                    cont.resume(
                        SocialLoginResult.Failure(code = errorCode, message = errorDesc),
                        onCancellation = {}
                    )
                }
            }

            NidOAuth.requestLogin(activity, callback)
        }

    override suspend fun logout(): Result<Unit> =
        kotlinx.coroutines.suspendCancellableCoroutine { cont ->
            NidOAuth.logout(object : NidOAuthCallback {
                override fun onSuccess() { cont.resume(Result.success(Unit), onCancellation = {}) }
                override fun onFailure(errorCode: String, errorDesc: String) {
                    cont.resume(Result.failure(IllegalStateException("$errorCode: $errorDesc")), onCancellation = {})
                }
            })
        }

    override suspend fun disconnect(): Result<Unit> =
        kotlinx.coroutines.suspendCancellableCoroutine { cont ->
            NidOAuth.disconnect(object : NidOAuthCallback {
                override fun onSuccess() { cont.resume(Result.success(Unit), onCancellation = {}) }
                override fun onFailure(errorCode: String, errorDesc: String) {
                    cont.resume(Result.failure(IllegalStateException("$errorCode: $errorDesc")), onCancellation = {})
                }
            })
        }
}

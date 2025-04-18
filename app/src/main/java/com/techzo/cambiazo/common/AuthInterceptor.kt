package com.techzo.cambiazo.common

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenProvider: () -> String) : Interceptor {
    // Añade aquí los paths que NO requieren autenticación
    private val unauthenticatedPaths = listOf(
        "authentication/sign-in",
        "authentication/sign-up",
        "users/username"
    )

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        if (shouldAuthenticate(originalRequest.url.encodedPath)) {
            val token = tokenProvider()
            if (token.isNotEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
        }

        return chain.proceed(requestBuilder.build())
    }

    private fun shouldAuthenticate(path: String): Boolean {
        return unauthenticatedPaths.none { path.startsWith(it) }
    }
}
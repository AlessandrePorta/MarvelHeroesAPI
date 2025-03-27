package com.marvelapi.application

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val apiKey: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestUrl = request.url

        val newUrl = requestUrl.newBuilder()
            .addQueryParameter(QUERY_PARAMETER_API_KEY, apiKey)
            .build()

        return chain.proceed(
            request.newBuilder()
                .url(newUrl)
                .build()
        )
    }

    companion object {
        private const val QUERY_PARAMETER_API_KEY = "api_key"
    }
}
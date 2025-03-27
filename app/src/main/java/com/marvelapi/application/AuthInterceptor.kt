package com.marvelapi.application

import okhttp3.Interceptor
import okhttp3.Response
import java.math.BigInteger
import java.security.MessageDigest
import java.util.Calendar

class AuthInterceptor(
    private val publicKey: String,
    private val privateKey: String,
    private val calendar: Calendar
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestUrl = request.url

        val timeStamp = (calendar.timeInMillis / 1000L).toString()
        val hash = "$timeStamp$privateKey$publicKey"
        val hashMD5 = digestMD5(hash)

        val newUrl = requestUrl.newBuilder()
            .addQueryParameter(MARVEL_PUBLIC_API_KEY, publicKey)
            .addQueryParameter(TIME_STAMP, timeStamp)
            .addQueryParameter(MARVEL_HASH_KEY, hashMD5)
            .build()

        return chain.proceed(
            request.newBuilder()
                .url(newUrl)
                .build()
        )
    }

    private fun digestMD5(hash : String) : String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1,md.digest(hash.toByteArray())).toString(16).padStart(32,'0')
    }

    companion object {
        private const val TIME_STAMP = "ts"
        private const val MARVEL_PUBLIC_API_KEY = "apikey"
        private const val MARVEL_HASH_KEY = "hash"
    }
}
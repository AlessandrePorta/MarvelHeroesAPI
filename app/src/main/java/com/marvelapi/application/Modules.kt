package com.marvelapi.application

import com.marvelapi.BuildConfig
import com.marvelapi.services.MarvelCharactersService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val HTTP_CLIENT_BASE = "HTTP_CLIENT_BASE"

private const val CONNECT_TIMEOUT = 5L

private const val READ_TIMEOUT = 30L
private const val WRITE_TIMEOUT = 30L

private const val HTTP_CLIENT_MARVEL = "HTTP_CLIENT_MARVEL"


val retrofitModule = module {

    factory<AuthInterceptor> {
        AuthInterceptor(
            apiKey = BuildConfig.API_KEY
        )
    }

    factory<HttpLoggingInterceptor> {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single<OkHttpClient>(named(HTTP_CLIENT_BASE)) {
        OkHttpClient
            .Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS).build()
    }

    factory<OkHttpClient>(named(HTTP_CLIENT_MARVEL)) {
        get<OkHttpClient>(named(HTTP_CLIENT_BASE))
            .newBuilder()
            .addInterceptor(get<AuthInterceptor>())
            .build()
    }

    factory<Converter.Factory> { GsonConverterFactory.create() }

    factory<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(get(named(HTTP_CLIENT_MARVEL)))
            .addConverterFactory(get())
            .build()
    }

    factory { get<Retrofit>().create(MarvelCharactersService::class.java) }

}

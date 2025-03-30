package com.marvelapi.services

import com.marvelapi.services.response.CharactersResponse
import com.marvelapi.services.response.WrapperResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface MarvelCharactersService {

    @GET("v1/public/characters")
    suspend fun getCharacters(@QueryMap queries: Map<String, String>): WrapperResponse<CharactersResponse>
}
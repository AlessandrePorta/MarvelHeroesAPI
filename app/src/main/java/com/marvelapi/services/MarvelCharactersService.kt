package com.marvelapi.services

import com.marvelapi.services.response.CharactersResponse
import com.marvelapi.services.response.WrapperResponse
import retrofit2.http.GET

interface MarvelCharactersService {

    @GET("v1/public/characters")
    suspend fun getCharacters(query: String?): WrapperResponse<CharactersResponse>
}
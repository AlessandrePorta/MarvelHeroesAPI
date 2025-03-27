package com.marvelapi.repository

import androidx.paging.PagingSource
import com.marvelapi.services.response.CharactersResponse

interface MarvelRepository {

    fun getCharacters(query : String?) : PagingSource<Int, CharactersResponse>
}
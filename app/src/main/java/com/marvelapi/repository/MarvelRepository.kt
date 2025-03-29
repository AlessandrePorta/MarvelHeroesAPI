package com.marvelapi.repository

import androidx.paging.PagingData
import com.marvelapi.database.CharacterEntity
import com.marvelapi.services.response.CharactersResponse
import com.marvelapi.services.response.WrapperResponse
import kotlinx.coroutines.flow.Flow

interface MarvelRepository {
    fun getCharacters(query: String?): Flow<PagingData<CharacterEntity>>
}
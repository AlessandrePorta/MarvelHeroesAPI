package com.marvelapi.repository

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.marvelapi.database.CharacterEntity
import kotlinx.coroutines.flow.Flow

interface MarvelRepository {
    fun getCharacters(query : String, pagingConfig: PagingConfig): Flow<PagingData<CharacterEntity>>
    fun getFavorites(): Flow<PagingData<CharacterEntity>>
}
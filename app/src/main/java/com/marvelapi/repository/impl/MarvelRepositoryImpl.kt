package com.marvelapi.repository.impl

import com.marvelapi.paging.CharactersPagingSource
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.marvelapi.database.CharacterDao
import com.marvelapi.database.CharacterEntity
import com.marvelapi.database.DatabaseProvider
import com.marvelapi.repository.MarvelRepository
import com.marvelapi.services.MarvelCharactersService
import kotlinx.coroutines.flow.Flow

class MarvelRepositoryImpl(
    private val marvelCharactersService: MarvelCharactersService,
    private val databaseProvider: DatabaseProvider
) : MarvelRepository {

    private val characterDao: CharacterDao
        get() = databaseProvider.getDatabase().characterDao()

    override fun getCharacters(query: String?): Flow<PagingData<CharacterEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = CharactersPagingSource.PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                CharactersPagingSource(
                    fetchCharacters = { marvelCharactersService.getCharacters(query) },
                    characterDao = characterDao
                )
            }
        ).flow
    }
}



package com.marvelapi.repository.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.marvelapi.database.CharacterDao
import com.marvelapi.database.CharacterEntity
import com.marvelapi.database.DatabaseProvider
import com.marvelapi.paging.CharactersPagingSource
import com.marvelapi.repository.MarvelRepository
import com.marvelapi.services.MarvelCharactersService
import kotlinx.coroutines.flow.Flow

class MarvelRepositoryImpl(
    private val marvelCharactersService: MarvelCharactersService,
    private val databaseProvider: DatabaseProvider
) : MarvelRepository {

    private val characterDao: CharacterDao
        get() = databaseProvider.getDatabase().characterDao()

    override fun getCharacters(
        query: String,
        pagingConfig: PagingConfig
    ): Flow<PagingData<CharacterEntity>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                CharactersPagingSource(
                    marvelCharactersService,
                    characterDao = characterDao
                )
            }
        ).flow
    }

    override fun getFavorites(): Flow<PagingData<CharacterEntity>> = Pager(
        config = PagingConfig(pageSize = 20)){
        characterDao.getFavorites(true)
    }.flow
}



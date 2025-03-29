package com.marvelapi.usecase

import android.util.Log
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.marvelapi.model.CharacterVO
import com.marvelapi.repository.MarvelRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CharactersUseCaseImpl(private val marvelRepository: MarvelRepository) : CharactersUseCase {

    override suspend fun invoke(
        query: String,
        pagingConfig: PagingConfig
    ): Flow<PagingData<CharacterVO>> {
        return try {
            marvelRepository.getCharacters(query, pagingConfig).map {
                it.map { entity ->
                    Log.d("Entity", entity.toString())
                    entity.toModel()
                }
            }
        } catch (e: Exception) {
            Log.d("Error", e.message.toString())
            throw e
        }
    }

    override suspend fun invoke(pagingConfig: PagingConfig): Flow<PagingData<CharacterVO>> {
        return marvelRepository.getFavorites().map { it.map { entity -> entity.toModel() } }
    }

}


package com.marvelapi.usecase

import androidx.paging.PagingData
import com.marvelapi.database.CharacterEntity
import com.marvelapi.repository.MarvelRepository
import kotlinx.coroutines.flow.Flow

class CharactersUseCaseImpl(private val marvelRepository: MarvelRepository) : CharactersUseCase {
    override fun invoke(
        query: String?
    ): Flow<PagingData<CharacterEntity>> {
        return marvelRepository.getCharacters(query)
    }

}



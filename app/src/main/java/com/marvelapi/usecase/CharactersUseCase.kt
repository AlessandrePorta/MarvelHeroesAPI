package com.marvelapi.usecase

import androidx.paging.PagingData
import com.marvelapi.database.CharacterEntity
import kotlinx.coroutines.flow.Flow

interface CharactersUseCase {
    operator fun invoke(query: String?): Flow<PagingData<CharacterEntity>>
}
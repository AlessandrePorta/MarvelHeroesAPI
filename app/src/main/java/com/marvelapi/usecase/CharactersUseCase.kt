package com.marvelapi.usecase

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.marvelapi.model.CharacterVO
import kotlinx.coroutines.flow.Flow

interface CharactersUseCase {
    suspend operator fun invoke(
        query: String,
        pagingConfig: PagingConfig
    ): Flow<PagingData<CharacterVO>>

    suspend operator fun invoke(pagingConfig: PagingConfig): Flow<PagingData<CharacterVO>>
}
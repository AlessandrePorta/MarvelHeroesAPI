package com.marvelapi.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.marvelapi.repository.MarvelRepository
import com.marvelapi.services.response.CharactersResponse
import kotlinx.coroutines.flow.Flow

internal class GetCharactersPagingUseCaseImpl(
    private val repository: MarvelRepository
) : CharactersUseCase {

    override fun invoke(
        query: String,
        pagingConfig: PagingConfig
    ): Flow<PagingData<CharactersResponse>> {
        return Pager(config = pagingConfig) {
            repository.getCharacters(query)
        }.flow
    }
}
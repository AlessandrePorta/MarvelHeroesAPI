package com.marvelapi.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.marvelapi.model.Character
import com.marvelapi.repository.MarvelRepository
import kotlinx.coroutines.flow.Flow

internal class GetCharactersPagingUseCaseImpl(
    private val repository: MarvelRepository
) : CharactersUseCase {

    override fun invoke(
        query: String,
        pagingConfig: PagingConfig
    ): Flow<PagingData<Character>> {
        return if (query.isNotEmpty()) {
            Pager(config = pagingConfig) {
                repository.getCharacters()
            }.flow
        } else {
            Pager(config = pagingConfig) {
                repository.getCharacters()
            }.flow
        }
    }
}
package com.marvelapi.usecase

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.marvelapi.model.Character
import com.marvelapi.services.response.CharactersResponse
import kotlinx.coroutines.flow.Flow

interface CharactersUseCase {

    operator fun invoke(query: String, pagingConfig: PagingConfig): Flow<PagingData<CharactersResponse>>
}
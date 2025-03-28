package com.marvelapi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.marvelapi.database.CharacterEntity
import com.marvelapi.usecase.CharactersUseCase
import kotlinx.coroutines.flow.Flow

class MarvelViewModel(
    private val charactersUseCase: CharactersUseCase
) : ViewModel() {

    fun getCharacters(query: String?): Flow<PagingData<CharacterEntity>> {
        return charactersUseCase.invoke(query).cachedIn(viewModelScope)
    }
}






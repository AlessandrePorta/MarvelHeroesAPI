package com.marvelapi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.marvelapi.usecase.CharactersUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class MarvelViewModel(
    private val getCharactersPagingUseCase: CharactersUseCase
) : ViewModel() {
    private val _query = MutableStateFlow("")

    val pagingDataFlow = _query.debounce(300)
        .flatMapLatest { query ->
            getCharactersPagingUseCase(
                query,
                pagingConfig = PagingConfig(1)
            )
        }.cachedIn(viewModelScope)

}

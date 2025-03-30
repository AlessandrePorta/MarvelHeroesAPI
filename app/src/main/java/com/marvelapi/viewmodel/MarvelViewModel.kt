package com.marvelapi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.filter
import com.marvelapi.database.CharacterDao
import com.marvelapi.database.toEntity
import com.marvelapi.model.CharacterVO
import com.marvelapi.usecase.CharactersUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val DELAY_SEARCH_TIME = 500L

private const val PAGE_SIZE = 20

private const val DEBOUNCE_TIME = 300L

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class MarvelViewModel(
    private val getCharactersPagingUseCase: CharactersUseCase,
    private val dao: CharacterDao
) : ViewModel() {

    private val _query = MutableStateFlow("")
    private var favoriteMode = false
    private var coroutineJob: Job? = null

    val pagingDataFlow = _query.flatMapLatest {
        _query.debounce(DEBOUNCE_TIME)
            .flatMapLatest { query ->
                if (favoriteMode) {
                    getCharactersPagingUseCase(pagingConfig = PagingConfig(PAGE_SIZE))
                } else {
                    getCharactersPagingUseCase(
                        query = query,
                        pagingConfig = PagingConfig(PAGE_SIZE, enablePlaceholders = false)
                    )
                }
            }
    }.cachedIn(viewModelScope).combine(_query) { pagingData, query ->
        pagingData.filter { it.name!!.startsWith(query) }
    }

    fun onFavoriteClick(character: CharacterVO) {
        viewModelScope.launch {
            character.id?.let {
                dao.insertFavorite(character.toEntity())
            }
        }
    }

    fun search(newText: String?) {
        coroutineJob?.cancel()
        coroutineJob = viewModelScope.launch {
            delay(DELAY_SEARCH_TIME)
            _query.update {
                newText.orEmpty()
            }
        }
    }

}




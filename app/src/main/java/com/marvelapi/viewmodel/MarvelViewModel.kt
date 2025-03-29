package com.marvelapi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.filter
import com.marvelapi.database.CharacterDao
import com.marvelapi.usecase.CharactersUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest

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
                    getCharactersPagingUseCase(query)
                } else {
                    getCharactersPagingUseCase(query)
                }
            }
            .cachedIn(viewModelScope).combine(_query) { pagingData, query ->
                pagingData.filter { it.name.startsWith(query) }
            }

    }


//
//    fun search(newText: String?) {
//        coroutineJob?.cancel()
//        coroutineJob = viewModelScope.launch {
//            delay(DELAY_SEARCH_TIME)
//            _query.update {
//                newText.orEmpty()
//            }
//        }
//    }
//
//    fun onFavoriteClick(character: Character) {
//        viewModelScope.launch {
//            character.id?.let {
//                dao.insert(character.toEntity())
//            }
//        }
//    }
//
//    fun setMode(favoriteMode: Boolean?) {
//        this.favoriteMode = favoriteMode ?: false
//    }

}


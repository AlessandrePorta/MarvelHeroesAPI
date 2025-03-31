package com.marvelapi

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.marvelapi.database.CharacterDao
import com.marvelapi.database.DatabaseProvider
import com.marvelapi.repository.MarvelRepository
import com.marvelapi.repository.impl.MarvelRepositoryImpl
import com.marvelapi.services.MarvelCharactersService
import com.marvelapi.usecase.CharactersUseCaseImpl
import com.marvelapi.viewmodel.MarvelViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertTrue
import org.junit.Before
import kotlin.test.Test

@ExperimentalCoroutinesApi
@FlowPreview
class MarvelRepositoryTest {

        @MockK
        lateinit var marvelCharactersService: MarvelCharactersService // Mocked service

        @MockK
        lateinit var marvelRepository: MarvelRepository // Mocked repository

        @MockK
        lateinit var characterDao: CharacterDao // Mocked DAO

        private lateinit var marvelViewModel: MarvelViewModel
        private val testDispatcher = StandardTestDispatcher()

        @Before
        fun setup() {
            Dispatchers.setMain(testDispatcher)

            // Initialize Mockk before use
            MockKAnnotations.init(this)

            // Set up mocks for repository behavior
//            coEvery { marvelCharactersService.getCharacters(any()) } returns flowOf(PagingData.from(listOf()))
            coEvery { marvelRepository.getFavorites() } returns flowOf(PagingData.from(listOf()))

            val charactersUseCase = CharactersUseCaseImpl(marvelRepository)

            // Initialize ViewModel with mock dependencies
            marvelViewModel = MarvelViewModel(charactersUseCase, characterDao)
        }

    @Test
    fun `test getFavorites returns correct PagingData`() = runTest {
        val result = marvelRepository.getFavorites().toList()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `test getCharacters returns paginated data`() = runTest {
        val result = marvelRepository.getCharacters("Spider-Man", PagingConfig(20)).toList()

        assertTrue(result.isNotEmpty())
    }
}
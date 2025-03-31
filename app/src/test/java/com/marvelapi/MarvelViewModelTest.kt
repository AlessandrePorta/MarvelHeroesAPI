package com.marvelapi

import androidx.paging.PagingData
import androidx.paging.map
import com.marvelapi.database.CharacterDao
import com.marvelapi.model.CharacterVO
import com.marvelapi.usecase.CharactersUseCaseImpl
import com.marvelapi.viewmodel.MarvelViewModel
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import kotlin.test.Test

@ExperimentalCoroutinesApi
@FlowPreview
class MarvelViewModelTest {

    @MockK
    private val marvelUseCaseMock: CharactersUseCaseImpl = mockk(relaxed = true)

    @MockK
    private val characterDaoMock: CharacterDao = mockk()

    private val marvelViewModel = MarvelViewModel(marvelUseCaseMock, characterDaoMock)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

    }

    private suspend fun collectPagingDataToList(flow: Flow<PagingData<CharacterVO>>): List<CharacterVO> {
        val result = mutableListOf<CharacterVO>()
        flow.collect { pagingData ->
            pagingData.map { entity ->
                result.add(entity)
            }
        }
        return result
    }

    @Test
    fun `test getFavorites with non-empty result`() = runTest {
        val characters = flowOf(
            PagingData.from(
                listOf(
                    CharacterVO(id = 1, name = "Spider-Man", description = "Hero", thumbnail = "path", isFavorite = true),
                )
            )
        )

        val list = characters.toList()

        assertTrue(list.isNotEmpty())
        assertEquals(1, list.size)
        assertEquals("Spider-Man", list.first())
    }

    @Test
    fun `test getFavorites with empty result`() = runTest {
        val characters = flowOf(
            PagingData.from(
                listOf()
            )
        )
        val list = characters.toList()

        assertTrue(list.isEmpty())
    }

    @Test
    fun `test getCharacters with non-empty result`() = runTest {
        val characters = flowOf(
            PagingData.from(
                listOf(
                    CharacterVO(id = 1, name = "Spider-Man", description = "Hero", thumbnail = "path", isFavorite = true),
                    CharacterVO(id = 2, name = "Iron Man", description = "Hero", thumbnail = "path", isFavorite = false)
                )
            )
        )

        val list = collectPagingDataToList(characters)

        assertTrue(list.isNotEmpty())
        assertEquals(1, list.size)
        assertEquals("Spider-Man", list.first().name)
    }
}
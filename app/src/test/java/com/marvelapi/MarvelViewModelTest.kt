import androidx.paging.PagingData
import androidx.paging.map
import com.marvelapi.database.CharacterDao
import com.marvelapi.database.CharacterEntity
import com.marvelapi.model.CharacterVO
import com.marvelapi.repository.MarvelRepository
import com.marvelapi.services.MarvelCharactersService
import com.marvelapi.usecase.CharactersUseCase
import com.marvelapi.usecase.CharactersUseCaseImpl
import com.marvelapi.viewmodel.MarvelViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
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
    lateinit var marvelRepository: MarvelRepository // Mocked repository

    @MockK
    lateinit var characterDao: CharacterDao // Mocked DAO

    private lateinit var marvelViewModel: MarvelViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        coEvery { marvelRepository.getCharacters(any(), any()) } returns flowOf(PagingData.from(listOf()))
        coEvery { marvelRepository.getFavorites() } returns flowOf(PagingData.from(listOf()))

        val charactersUseCase = CharactersUseCaseImpl(marvelRepository)

        marvelViewModel = MarvelViewModel(charactersUseCase, characterDao)
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
                    CharacterEntity(id = 1, name = "Spider-Man", description = "Hero", thumbnail = "path", isFavorite = true)
                )
            )
        )
        coEvery { marvelRepository.getFavorites() } returns characters

        marvelViewModel.getFavorites()

        val list = collectPagingDataToList(marvelViewModel.pagingDataFlow)

        assertTrue(list.isNotEmpty())
        assertEquals(1, list.size)
        assertEquals("Spider-Man", list.first().name)
    }

    @Test
    fun `test getFavorites with empty result`() = runTest {
        val emptyPagingData = flowOf(PagingData.from(emptyList<CharacterEntity>()))
        coEvery { marvelRepository.getFavorites() } returns emptyPagingData


        marvelViewModel.getFavorites()

        val list = collectPagingDataToList(marvelViewModel.pagingDataFlow)

        assertTrue(list.isEmpty())
    }

    @Test
    fun `test getCharacters with non-empty result`() = runTest {
        val characters = flowOf(
            PagingData.from(
                listOf(
                    CharacterEntity(id = 1, name = "Spider-Man", description = "Hero", thumbnail = "path", isFavorite = true),
                    CharacterEntity(id = 2, name = "Iron Man", description = "Hero", thumbnail = "path", isFavorite = false)
                )
            )
        )
        coEvery { marvelRepository.getCharacters(any(), any()) } returns characters

        marvelViewModel.search("Spider-Man")

        val list = collectPagingDataToList(marvelViewModel.pagingDataFlow)

        assertTrue(list.isNotEmpty())
        assertEquals(1, list.size)
        assertEquals("Spider-Man", list.first().name) 
    }
}
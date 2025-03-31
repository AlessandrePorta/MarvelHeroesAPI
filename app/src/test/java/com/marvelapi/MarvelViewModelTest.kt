package com.marvelapi

import com.marvelapi.database.CharacterDao
import com.marvelapi.database.toEntity
import com.marvelapi.model.CharacterVO
import com.marvelapi.usecase.CharactersUseCaseImpl
import com.marvelapi.viewmodel.MarvelViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlinx.coroutines.test.resetMain

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class MarvelViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @MockK
    private lateinit var marvelUseCaseMock: CharactersUseCaseImpl

    @MockK
    private lateinit var characterDaoMock: CharacterDao

    private lateinit var marvelViewModel: MarvelViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)

        marvelViewModel = MarvelViewModel(marvelUseCaseMock, characterDaoMock)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `when character is passed to onFavoriteClick, it should be saved`() = runTest {
        coEvery { characterDaoMock.insertFavorite(any()) } returns Unit

        val character = CharacterVO(1, "Test", "Test", "Test", "Test", false)

        marvelViewModel.onFavoriteClick(character)

        coVerify { characterDaoMock.insertFavorite(character.toEntity()) }
    }
}
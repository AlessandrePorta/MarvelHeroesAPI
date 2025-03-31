package com.marvelapi

import com.marvelapi.database.CharacterDao
import com.marvelapi.database.toEntity
import com.marvelapi.model.CharacterVO
import com.marvelapi.usecase.CharactersUseCaseImpl
import com.marvelapi.viewmodel.MarvelViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [30])
class MarvelViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    private var marvelUseCaseMock: CharactersUseCaseImpl = mockk(relaxed = true)

    private var characterDaoMock: CharacterDao = mockk(relaxed = true)

    private lateinit var marvelViewModel: MarvelViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        println(CharacterDao::class.qualifiedName)
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
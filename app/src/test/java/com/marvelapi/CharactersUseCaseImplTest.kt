package com.marvelapi

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.marvelapi.database.CharacterDao
import com.marvelapi.paging.CharactersPagingSource
import com.marvelapi.repository.impl.MarvelRepositoryImpl
import com.marvelapi.usecase.CharactersUseCaseImpl
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CharactersUseCaseImplTest {

    private val repoMock: MarvelRepositoryImpl = mockk()
    private val useCase = CharactersUseCaseImpl(repoMock)
    private val pagingSourceMock: CharactersPagingSource = mockk(relaxed = true)
    private val daoMock: CharacterDao = mockk(relaxed = true)

    @Test
    fun `when invoke useCase with query, getCharacters should be called`() = runTest {
        val config = PagingConfig(20)
        coEvery { repoMock.getCharacters(any(), any()) } returns Pager(
            config = config,
            pagingSourceFactory = {
                pagingSourceMock
            }
        ).flow

        useCase.invoke(
            query = "",
            pagingConfig = PagingConfig(20)
        ).first()

        verify(exactly = 1) { repoMock.getCharacters("", any()) }
    }

    @Test
    fun `when invoke useCase without query, getFavorites should be called`() = runTest {
        val config = PagingConfig(20)
        coEvery { repoMock.getFavorites() } returns Pager(
            config = config
        ) { daoMock.getFavorites(true) }
            .flow

        useCase.invoke(
            pagingConfig = PagingConfig(20)
        ).first()

        verify(exactly = 1) { repoMock.getFavorites() }
    }
}


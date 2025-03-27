package com.marvelapi.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.marvelapi.model.Character
import com.marvelapi.services.response.DataContainerResponse
import com.marvelapi.services.response.toModel

class CharactersPagingSource(
    private val fetchCharacters: suspend (Int) -> DataContainerResponse,
) : PagingSource<Int, Character>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        return try {
            val offset = params.key ?: 1

            val response = fetchCharacters(offset)
            val responseOffset = response.offset
            val total = response.total

            LoadResult.Page(
                data = response.results.map { it.toModel() },
                prevKey = null,
                nextKey = if (responseOffset < total) {
                    responseOffset + LIMIT
                } else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(LIMIT) ?: anchorPage?.nextKey?.minus(LIMIT)
        }
    }

    companion object {
        private const val LIMIT = 1
    }
}
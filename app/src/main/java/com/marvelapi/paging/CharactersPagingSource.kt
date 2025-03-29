package com.marvelapi.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.marvelapi.database.CharacterDao
import com.marvelapi.database.CharacterEntity
import com.marvelapi.services.response.CharactersResponse
import com.marvelapi.services.response.WrapperResponse

class CharactersPagingSource(
    val fetchCharacters: suspend () -> WrapperResponse<CharactersResponse>,
    private val characterDao: CharacterDao
) : PagingSource<Int, CharacterEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterEntity> {
        return try {
            val page = params.key ?: 0
            val offset = page * PAGE_SIZE

            val response = fetchCharacters()

            val characters = response.dataContainer.results.map {
                CharacterEntity(
                    id = it.id!!,
                    name = it.name!!,
                    description = it.description ?: "",
                    thumbnail = it.thumbnail?.path + it.thumbnail?.extension
                )
            }
            Log.d("PagingSource", "Fetched ${response.status} characters")

            if (characters.isNotEmpty()) {
                characterDao.insert(characters)
            }

            val nextKey = if (offset + PAGE_SIZE >= response.dataContainer.total) null else page + 1
            LoadResult.Page(
                data = characters,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CharacterEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}








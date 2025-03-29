package com.marvelapi.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.marvelapi.database.CharacterDao
import com.marvelapi.database.CharacterEntity
import com.marvelapi.services.MarvelCharactersService
import com.marvelapi.services.response.CharactersResponse
import com.marvelapi.services.response.toEntity
import com.marvelapi.services.response.toModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CharactersPagingSource(
    private val marvelCharactersService: MarvelCharactersService,
    private val characterDao: CharacterDao
) : PagingSource<Int, CharacterEntity>() {
    var query = ""

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterEntity> {
        val page = params.key ?: 1
        val offset = (page - 1) * 20
        return try {
            val queries = createQuery(offset)
            val response = marvelCharactersService.getCharacters(queries)

            val characters = response.dataContainer.results.map {
                CharacterEntity(
                    id = it.id!!,
                    name = it.name!!,
                    description = it.description ?: "",
                    thumbnail = it.thumbnail?.path + "." + it.thumbnail?.extension
                )
            }
            Log.d("Mapped Characters", characters.toString())

            if (characters.isNotEmpty()) {
                withContext(Dispatchers.IO) {
                    try {
                        characterDao.insert(characters)
                        Log.d("Inserted Data", "Data inserted successfully.")
                    } catch (e: Exception) {
                        Log.e("Database Error", "Error inserting data", e)
                    }
                }
                Log.d("Inserted Data", "Data inserted successfully")
            }

            val nextKey = if (characters.size < 20) null else page + 1
            Log.d("PagingSource", "NextKey: $nextKey, PrevKey: null, DataSize: ${characters.size}")
            LoadResult.Page(
                data = characters,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            Log.e("PagingSource", "Error during data loading", e)
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
        const val START_VALUE = 20
        private const val LIMIT = 40
        private const val OFFSET = "offset"
        private const val NAME_STARTS_WITH = "nameStartsWith"
    }

    private fun createQuery(offset : Int) = buildMap {
        put(OFFSET, offset.toString())
        if (query.isNotEmpty()) put(NAME_STARTS_WITH, query)
    }
}








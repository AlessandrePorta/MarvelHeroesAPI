import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.marvelapi.services.response.CharactersResponse
import com.marvelapi.services.response.WrapperResponse

class CharactersPagingSource(
    val fetchCharacters: suspend () -> WrapperResponse<CharactersResponse>,
) : PagingSource<Int, CharactersResponse>() {
    override fun getRefreshKey(
        state: PagingState<Int, CharactersResponse>
    ): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(
                anchorPosition
            )
            anchorPage?.prevKey?.plus(1) ?:
            anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, CharactersResponse> {
        return try {
            val page = params.key ?: 0
            val offset = page * PAGE_SIZE
            val response = fetchCharacters()
            val nextKey =
                if (offset >= response.dataContainer.total) null
                else page + 1
            return LoadResult.Page(
                data = response.dataContainer.results,
                prevKey = null, // Only paging forward.
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}


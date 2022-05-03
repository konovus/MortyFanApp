package com.konovus.simplemortyr.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.konovus.simplemortyr.api.ListCharactersResponse
import com.konovus.simplemortyr.api.MortyApi
import com.konovus.simplemortyr.entity.Character
import com.konovus.simplemortyr.ui.mainScreen.TAG
import com.konovus.simplemortyr.util.Filter
import retrofit2.HttpException
import java.io.IOException

const val STARTING_PAGE_INDEX = 1

class MainPagingSource(
    private val api: MortyApi,
    private val query: String,
    private val filter: Filter
) : PagingSource<Int, Character>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        val pageNumber = params.key ?: STARTING_PAGE_INDEX
        return try{
            val response = searchCharacters(pageNumber, filter, query, api)

            Log.i(TAG, "Paging Source - load: ${response.results.size}")
            LoadResult.Page(
                data = response.results,
                prevKey = if(pageNumber == STARTING_PAGE_INDEX) null else pageNumber - 1,
                nextKey = if(response.info.next == null) null else pageNumber + 1
            )
        } catch (e: IOException){
            LoadResult.Error(e)
        } catch (e: HttpException){
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
    private suspend fun searchCharacters(pageNumber: Int, filter: Filter, query: String, api: MortyApi
    ): ListCharactersResponse =
        when(filter){
            Filter.NAME -> api.filterCharacters(pageNumber, query, null, null, null)
            Filter.STATUS_ALIVE -> api.filterCharacters(pageNumber,null, query, null, null)
            Filter.STATUS_DEAD -> api.filterCharacters(pageNumber,null, query, null, null)
            Filter.STATUS_UNKNOWN -> api.filterCharacters(pageNumber,null, query, null, null)
            Filter.SPECIES_HUMAN -> api.filterCharacters(pageNumber,null, null, query, null)
            Filter.SPECIES_ALIEN -> api.filterCharacters(pageNumber,null, null, query, null)
            Filter.SPECIES_HUMANOID -> api.filterCharacters(pageNumber,null, null, query, null)
            Filter.SPECIES_MYTHOLOGICAL -> api.filterCharacters(pageNumber,null, null, query, null)
            Filter.SPECIES_DISEASE -> api.filterCharacters(pageNumber,null, null, query, null)
            Filter.GENDER_MALE -> api.filterCharacters(pageNumber,null, null, null, query)
            Filter.GENDER_FEMALE -> api.filterCharacters(pageNumber,null, null, null, query)
            Filter.GENDER_GENDERLESS -> api.filterCharacters(pageNumber,null, null, null, query)
            Filter.GENDER_UNKNOWN -> api.filterCharacters(pageNumber,null, null, null, query)
        }
}
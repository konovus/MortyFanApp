package com.konovus.simplemortyr.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.konovus.simplemortyr.api.ListCharactersResponse
import com.konovus.simplemortyr.api.MortyApi
import com.konovus.simplemortyr.entity.Character
import com.konovus.simplemortyr.ui.mainScreen.TAG
import com.konovus.simplemortyr.util.Filter
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit


@ExperimentalPagingApi
class MainRemoteMediator(
    private val db: MortyDatabase,
    private val api: MortyApi,
    private val query: String,
    private val filter: Filter
) : RemoteMediator<Int, Character>() {

    companion object{
        var pageNr = 1
        var lastUpdated = 0L
        var lastQuery = ""
    }
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Character>
    ): MediatorResult {
        Log.i(TAG, "mediator loadType: ${loadType.name}")
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> STARTING_PAGE_INDEX
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> pageNr
            }

            pageNr = if (lastQuery != query) 1 else pageNr
            lastQuery = query

            val searchQuery = if (query.isEmpty()) null else query
            val response = searchCharacters(pageNr, filter, searchQuery, api)

            Log.i(TAG, "mediator response: ${response.results}")
            db.withTransaction {
                if (loadType == LoadType.REFRESH)
                    db.mortyDao().deleteAll()

                val characters = response.results.map {
                    val saveCharacter = it.copy(searchQuery = " " + it.name + " "
                            + it.gender + " " + it.origin.name + " " + it.species + " " + it.status)
                    saveCharacter

                }

                val nextPage = if (response.info.next != null) (page + 1) else null
                pageNr = nextPage ?: 1
                lastUpdated = state.firstItemOrNull()?.updatedAt ?: System.currentTimeMillis()
                db.mortyDao().insertAll(characters)
            }


            MediatorResult.Success(endOfPaginationReached = response.info.next == null)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.HOURS.convert(1000, TimeUnit.MILLISECONDS)
        return if (System.currentTimeMillis() - lastUpdated >= cacheTimeout)
            InitializeAction.SKIP_INITIAL_REFRESH
        else
            InitializeAction.LAUNCH_INITIAL_REFRESH

    }

    private suspend fun searchCharacters(pageNumber: Int, filter: Filter, query: String?, api: MortyApi
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
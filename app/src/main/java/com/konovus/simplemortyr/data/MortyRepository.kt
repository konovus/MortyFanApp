package com.konovus.simplemortyr.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.konovus.simplemortyr.api.MortyApi
import com.konovus.simplemortyr.entity.Episode
import com.konovus.simplemortyr.util.Filter
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MortyRepository @Inject constructor(
    private val api: MortyApi,
    private val dao: MortyDao,
    private val db: MortyDatabase
    ) {

    @ExperimentalPagingApi
    fun getAllCharactersRemoteMediator(query: String, filter: Filter) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false,
            ),
            remoteMediator = MainRemoteMediator(db, api, query, filter),
            pagingSourceFactory = { dao.getPagedCharactersByQuery(query) }
        ).flow


    fun getAllCharactersPaged(query: String, filter: Filter) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            pagingSourceFactory = { MainPagingSource(api, query, filter) }
        ).flow

    suspend fun getAllEpisodes(ids: String) =
        if(ids.contains(","))
            api.getEpisodes(ids)
        else listOf(api.getEpisodeById(ids))

    suspend fun getAllEpisodesSafe(ids: String): List<Episode> {
        var requestList: SafeResponse<List<Episode>>? = null
        var requestSingle: SafeResponse<Episode>? = null
        if(ids.contains(","))
            requestList = safeApiCall { api.getEpisodesSafe(ids) }
        else requestSingle = safeApiCall { api.getEpisodeByIdSafe(ids) }

        if(requestList?.failed == true || requestSingle?.failed == true)
            return emptyList()

        if (requestList?.isSuccessful == false || requestSingle?.isSuccessful == false)
            return emptyList()

        return requestList?.body ?: listOf(requestSingle?.body!!)
    }


    suspend fun getAllCharacters(ids: String) =
        if(ids.contains(","))
            api.getCharactersById(ids)
        else listOf(api.getCharacterById(ids))

    private inline fun <T> safeApiCall(apicall: () -> Response<T>): SafeResponse<T> {
        return try {
            SafeResponse.success(apicall.invoke())
        } catch (e: Exception){
            SafeResponse.failure(e)
        }
    }

}
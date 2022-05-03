package com.konovus.simplemortyr.api

import com.konovus.simplemortyr.entity.Character
import com.konovus.simplemortyr.entity.Episode
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MortyApi {

    companion object{
        const val BASE_URL = "https://rickandmortyapi.com/api/"
    }

    @GET("character/{id}")
    suspend fun getCharacterById(@Path("id") id: String): Character

    @GET("character/{ids}")
    suspend fun getCharactersById(@Path("ids") ids: String): List<Character>

    @GET("character")
    suspend fun getAllCharacters(
        @Query("page") page: Int
    ): ListCharactersResponse

    @GET("character/?")
    suspend fun filterCharacters(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("status") status: String?,
        @Query("species") species: String?,
        @Query("gender") gender: String?,
    ): ListCharactersResponse

    @GET("episode/{ids}")
    suspend fun getEpisodes(
        @Path("ids") ids: String
    ): List<Episode>

    @GET("episode/{ids}")
    suspend fun getEpisodesSafe(
        @Path("ids") ids: String
    ): Response<List<Episode>>

    @GET("episode/{id}")
    suspend fun getEpisodeById(
        @Path("id") id: String
    ): Episode

    @GET("episode/{id}")
    suspend fun getEpisodeByIdSafe(
        @Path("id") id: String
    ): Response<Episode>

}
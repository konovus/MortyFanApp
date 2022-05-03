package com.konovus.simplemortyr.data

import androidx.paging.PagingSource
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.konovus.simplemortyr.entity.Character
import kotlinx.coroutines.flow.Flow

@Dao
interface MortyDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(character: Character)

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(characters: List<Character>)

    @Update(onConflict = REPLACE)
    suspend fun update(character: Character)

    @Delete
    suspend fun delete(character: Character)

    @Query("delete from characters where searchQuery = :query")
    suspend fun deleteAllByQuery(query: String)

    @Query("delete from characters ")
    suspend fun deleteAll()

    @Query("select * from characters")
    fun getAllCharactersFlow(): Flow<List<Character>>

    @Query("select * from characters")
    suspend fun getAllCharacters(): List<Character>

    @Query("select * from characters")
    fun getPagedCharacters(): PagingSource<Int, Character>

    @Query("select * from characters where searchQuery LIKE '% ' || :query || '%' ")
    fun getPagedCharactersByQuery(query: String): PagingSource<Int, Character>

}
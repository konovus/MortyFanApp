package com.konovus.simplemortyr.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.konovus.simplemortyr.entity.RemoteKey

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKey: RemoteKey)

    @Query("SELECT * FROM remote_keys where searchQuery = :query ")
    suspend fun remoteKeyByQuery(query: String): RemoteKey

    @Query("DELETE FROM remote_keys where searchQuery = :query")
    suspend fun deleteById(query: String)

    @Query("DELETE FROM remote_keys where searchQuery = :query")
    suspend fun deleteAll(query: String)
}
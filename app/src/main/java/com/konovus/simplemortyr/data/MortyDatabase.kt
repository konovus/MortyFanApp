package com.konovus.simplemortyr.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.konovus.simplemortyr.entity.Character
import com.konovus.simplemortyr.entity.Converters
import com.konovus.simplemortyr.entity.RemoteKey

@Database(entities = [Character::class, RemoteKey::class], version = 13, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MortyDatabase : RoomDatabase(){

    abstract fun mortyDao(): MortyDao
    abstract fun remoteKeyDao(): RemoteKeyDao

}
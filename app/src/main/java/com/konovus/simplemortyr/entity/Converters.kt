package com.konovus.simplemortyr.entity

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class Converters  {

    private val moshi: Moshi =  Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

//    @TypeConverter
//    fun fromEpisodeUrls(data: EpisodeUrls): String {
//        return moshi.adapter(EpisodeUrls::class.java).toJson(data)
//    }
//
//    @TypeConverter
//    fun toEpisodeUrls(json: String): EpisodeUrls {
//        return moshi.adapter(EpisodeUrls::class.java).fromJson(json)!!
//    }

    @TypeConverter
    fun fromStringList(data: List<String>): String{
        val type = Types.newParameterizedType(List::class.java, String::class.java)
        val adapter = moshi.adapter<List<String>>(type)
        return adapter.toJson(data)
    }
    @TypeConverter
    fun toStringList(json: String): List<String> {
        val type = Types.newParameterizedType(List::class.java, String::class.java)
        val adapter = moshi.adapter<List<String>>(type)
        return adapter.fromJson(json)!!
    }

    @TypeConverter
    fun fromCharacter(data: Character): String {
        return moshi.adapter(Character::class.java).toJson(data)
    }

    @TypeConverter
    fun toCharacter(json: String): Character {
        return moshi.adapter(Character::class.java).fromJson(json)!!
    }


    @TypeConverter
    fun fromLocation(data: Location): String {
        return moshi.adapter(Location::class.java).toJson(data)
    }

    @TypeConverter
    fun toLocation(json: String): Location {
        return moshi.adapter(Location::class.java).fromJson(json)!!
    }


    @TypeConverter
    fun fromOrigin(data: Origin): String {
        return moshi.adapter(Origin::class.java).toJson(data)
    }

    @TypeConverter
    fun toOrigin(json: String): Origin {
        return moshi.adapter(Origin::class.java).fromJson(json)!!
    }


}

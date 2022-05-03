package com.konovus.simplemortyr.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
@Parcelize
@Entity(tableName = "characters")
data class Character(
    val name: String = "",
    var searchQuery: String = "",
    val updatedAt: Long = System.currentTimeMillis(),
    var fav: Boolean = false,
    val episode: List<String> = emptyList(),
    val gender: String = "",
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1,
    val image: String = "",
    val origin: Origin = Origin(),
    val species: String = "",
    val status: String = "",
) : Parcelable {

    fun getEpisodesList()  = episode.joinToString(",") {
        it.split("/").last()
    }
}

package com.konovus.simplemortyr.entity

import android.os.Parcelable
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class Episode(
    @PrimaryKey
    val id: Int,
    val name: String,
    val air_date: String,
    val episode: String,
    val characters: List<String>
): Parcelable {

    fun getFormattedEpisodeName(): String {
        val seasonNr = if (episode.substring(1,2) == "0") episode.substring(2,3) else episode.substring(1,3)
        val episodeNr = if (episode.substring(4, 5) == "0") episode.substring(5) else episode.substring(4)
        return "Season $seasonNr Episode $episodeNr"
    }

    fun getCharactersIds() = characters.joinToString(",") {
        it.split("/").last()
    }
}
package com.konovus.simplemortyr.entity

import android.os.Parcelable
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class Origin(
    @PrimaryKey
    val name: String = "",
    val url: String = ""
) : Parcelable
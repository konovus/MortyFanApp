package com.konovus.simplemortyr.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKey(
  @PrimaryKey
  val searchQuery: String,
  val next: Int?,
  val prev: Int?
)
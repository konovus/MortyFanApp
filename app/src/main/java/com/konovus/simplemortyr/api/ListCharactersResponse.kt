package com.konovus.simplemortyr.api

import com.konovus.simplemortyr.entity.Character

data class ListCharactersResponse(
    val info: Info,
    val results: List<Character>
){
    data class Info(
        val count: Int,
        val pages: Int,
        val next: String?,
        val prev: String?
    )
}
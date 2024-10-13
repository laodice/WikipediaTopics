package com.example.wikipediatopics.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WikipediaArticle(
    @Json(name = "*") val htmlDump: String,
)

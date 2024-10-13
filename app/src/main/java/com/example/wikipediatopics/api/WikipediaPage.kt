package com.example.wikipediatopics.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WikipediaPage(
    val title: String,
    @Json(name = "pageid") val pageId: Int,
    val text: WikipediaArticle,
)

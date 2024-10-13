package com.example.wikipediatopics.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WikipediaResult(
    val parse: WikipediaPage,
)

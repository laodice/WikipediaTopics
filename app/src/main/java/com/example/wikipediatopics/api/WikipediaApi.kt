package com.example.wikipediatopics.api

import retrofit2.http.GET
import retrofit2.http.Query

interface WikipediaApi {
    @GET("api.php?action=parse&section=0&prop=text&format=json&")
    suspend fun getPage(
        @Query("page") topic: String,
    ): WikipediaResult
}

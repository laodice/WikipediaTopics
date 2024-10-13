package com.example.wikipediatopics.page

import com.example.wikipediatopics.api.WikipediaApi
import com.example.wikipediatopics.api.WikipediaPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PageRepository(
    private val api: WikipediaApi,
) {
    suspend fun fetchPageForTopic(topic: String): Result<WikipediaPage> =
        withContext(Dispatchers.IO) {
            try {
                val result = api.getPage(topic).parse
                Result.success(result)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}

package com.example.wikipediatopics.page

import com.example.wikipediatopics.api.ApiResult
import com.example.wikipediatopics.api.Failure
import com.example.wikipediatopics.api.Success
import com.example.wikipediatopics.api.WikipediaApi
import com.example.wikipediatopics.api.WikipediaPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PageRepository(
    private val api: WikipediaApi,
) {
    suspend fun fetchPageForTopic(topic: String): ApiResult<WikipediaPage> =
        withContext(Dispatchers.IO) {
            try {
                val result = api.getPage(topic).parse
                Success(result)
            } catch (e: Exception) {
                Failure(e)
            }
        }
}

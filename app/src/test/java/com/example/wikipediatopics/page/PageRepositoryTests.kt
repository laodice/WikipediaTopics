package com.example.wikipediatopics.page

import android.net.http.HttpException
import com.example.wikipediatopics.api.Failure
import com.example.wikipediatopics.api.Success
import com.example.wikipediatopics.api.WikipediaApi
import com.example.wikipediatopics.api.WikipediaPage
import com.example.wikipediatopics.api.WikipediaResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.isA
import org.hamcrest.core.IsInstanceOf
import org.junit.Test

class PageRepositoryTests {
    private val api = mockk<WikipediaApi>()

    @Test
    fun `Should return the WikipediaPage model wrapped in successful result`() =
        runBlocking {
            coEvery { api.getPage(any()) } returns mockk<WikipediaResult>(relaxed = true)

            val result = PageRepository(api).fetchPageForTopic("topic")
            assertThat(result, IsInstanceOf(Success::class.java))
            assertThat((result as Success).value, isA(WikipediaPage::class.java))
        }

    @Test
    fun `Should return a failure`() =
        runBlocking {
            coEvery { api.getPage(any()) } throws HttpException("http exception", null)

            val result = PageRepository(api).fetchPageForTopic("topic")
            assertThat(result, IsInstanceOf(Failure::class.java))
        }
}

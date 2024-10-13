package com.example.wikipediatopics.page

import com.example.wikipediatopics.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Rule
import org.junit.Test

class PageViewModelTests {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val repository = mockk<PageRepository>()
    private val viewModel = PageViewModel(repository)

    @Test
    fun `Should update the query value when calling onQueryChange`() =
        runBlocking {
            viewModel.onQueryChange("new value")
            assertThat(viewModel.query.first(), `is`("new value"))
        }

    @Test
    fun `Should not update the query value when search is active`() =
        runBlocking {
            viewModel.onQueryChange("value")

            viewModel.onToggleSearch(true)

            assertThat(viewModel.query.first(), `is`("value"))
            assertThat(viewModel.isSearching.first(), `is`(true))
        }

    @Test
    fun `Should update the query value when search is inactive`() =
        runBlocking {
            viewModel.onQueryChange("value")
            viewModel.onToggleSearch(true)

            viewModel.onToggleSearch(false)

            assertThat(viewModel.query.first(), `is`(""))
            assertThat(viewModel.isSearching.first(), `is`(false))
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Should search for the topic when search is triggered`() =
        runTest {
            coEvery { repository.fetchPageForTopic(any()) } returns Result.success(mockk(relaxed = true))

            viewModel.onSearch("topic")

            advanceUntilIdle()
            assertThat(viewModel.query.first(), `is`("topic"))
            coVerify(exactly = 1) { repository.fetchPageForTopic(any()) }
        }
}

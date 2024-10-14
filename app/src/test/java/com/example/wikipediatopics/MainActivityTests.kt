package com.example.wikipediatopics

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performImeAction
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.wikipediatopics.api.WikipediaArticle
import com.example.wikipediatopics.api.WikipediaPage
import com.example.wikipediatopics.page.PageViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Config.OLDEST_SDK])
class MainActivityTests : KoinTest {
    @get:Rule
    val composeRule = createComposeRule()

    private val viewModel = mockk<PageViewModel>(relaxed = true)
    private val module =
        module {
            viewModel { viewModel }
        }

    @Before
    fun setup() {
        loadKoinModules(module)
    }

    @After
    fun tearDown() {
        unloadKoinModules(module)
    }

    @Test
    fun `Should display a search bar`() {
        every { viewModel.query } returns MutableStateFlow("value").asStateFlow()
        every { viewModel.isSearching } returns MutableStateFlow(false).asStateFlow()

        with(composeRule) {
            setContent { App() }

            onAllNodesWithTag(TEST_TAG_SEARCH_BAR).apply {
                assertCountEquals(1)
                onFirst().assertIsDisplayed()
                onFirst().onChild().assertTextEquals("value")
            }
        }
    }

    @Test
    fun `Entering a topic and clicking enter should start a search`() {
        every { viewModel.query } returns MutableStateFlow("Query").asStateFlow()
        every { viewModel.isSearching } returns MutableStateFlow(true).asStateFlow()
        every { viewModel.wikiPage } returns MutableStateFlow(null).asStateFlow()

        with(composeRule) {
            setContent { App() }

            onNodeWithTag(TEST_TAG_SEARCH_BAR).onChild().apply {
                performImeAction()
            }

            verify(exactly = 1) { viewModel.onSearch("Query") }
        }
    }

    @Test
    fun `Should display wikipedia page and number of occurrences when searching for a topic`() {
        every { viewModel.query } returns MutableStateFlow("value").asStateFlow()
        every { viewModel.isSearching } returns MutableStateFlow(true).asStateFlow()
        every { viewModel.wikiPage } returns
            MutableStateFlow(
                WikipediaPage(
                    title = "title",
                    pageId = 1234,
                    text = WikipediaArticle("<h1>html dump</h1>"),
                ),
            ).asStateFlow()

        with(composeRule) {
            setContent { App() }

            onAllNodesWithTag(TEST_TAG_QUERY_OCCURRENCES).apply {
                assertCountEquals(1)
                onFirst().assertIsDisplayed()
            }
            onAllNodesWithTag(TEST_TAG_WEB_PAGE).apply {
                assertCountEquals(1)
                onFirst().assertExists()
            }
        }
    }

    @Test
    fun `Should find the matching word in the text regardless of letter case`() {
        val text = "AQuerylongquerytextcontaiQueryningQuerywordsqueryIwanttocountQuery"
        val word = "Query"

        assertThat(text.countOccurrencesOf(word), `is`(6))
    }

    @Test
    fun `Should return 0 if there's no instances of the word`() {
        val text = "AlongtextcontainstardewingwordsIwanttocount"
        val word = "Query"

        assertThat(text.countOccurrencesOf(word), `is`(0))
    }
}

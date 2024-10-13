package com.example.wikipediatopics

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onFirst
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.wikipediatopics.page.PageViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
}

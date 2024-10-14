package com.example.wikipediatopics

import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.wikipediatopics.page.PageViewModel
import com.example.wikipediatopics.ui.theme.WikipediaTopicsTheme
import org.koin.androidx.compose.koinViewModel

const val TEST_TAG_SEARCH_BAR = "TEST_TAG_SEARCH_BAR"
const val TEST_TAG_SEARCH_BAR_PLACEHOLDER = "TEST_TAG_SEARCH_BAR_PLACEHOLDER"
const val TEST_TAG_QUERY_OCCURRENCES = "TEST_TAG_QUERY_OCCURRENCES"
const val TEST_TAG_WEB_PAGE = "TEST_TAG_WEB_PAGE"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WikipediaTopicsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    App()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(viewModel: PageViewModel = koinViewModel()) {
    val query by viewModel.query.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val wikiPage by viewModel.wikiPage.collectAsState()

    val queryOccurrences by derivedStateOf {
        wikiPage?.text?.htmlDump?.countOccurrencesOf(query) ?: 0
    }

    SearchBar(
        query = query,
        onQueryChange = viewModel::onQueryChange,
        onSearch = viewModel::onSearch,
        active = isSearching,
        onActiveChange = viewModel::onToggleSearch,
        placeholder = { SearchPlaceholder() },
        modifier =
            Modifier
                .fillMaxWidth()
                .testTag(TEST_TAG_SEARCH_BAR),
    ) {
        wikiPage?.let { page ->
            Text(
                stringResource(id = R.string.wiki_page_query_occurrences, page.title, queryOccurrences),
                modifier = Modifier.testTag(TEST_TAG_QUERY_OCCURRENCES),
            )
            AndroidView(
                modifier = Modifier.testTag(TEST_TAG_WEB_PAGE),
                factory = { viewContext ->
                    WebView(viewContext).apply {
                        layoutParams =
                            ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                            )
                    }
                },
                update = { webView ->
                    webView.loadDataWithBaseURL(null, page.text.htmlDump, "text/html", "utf-8", null)
                },
            )
        }
    }
}

@Composable
fun SearchPlaceholder() {
    Text(
        text = stringResource(id = R.string.wiki_search_bar_placeholder),
        textDecoration = TextDecoration.Underline,
        modifier = Modifier.testTag(TEST_TAG_SEARCH_BAR_PLACEHOLDER),
    )
}

@VisibleForTesting
fun String.countOccurrencesOf(word: String): Int = word.toRegex(RegexOption.IGNORE_CASE).findAll(this, 0).count()

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WikipediaTopicsTheme {
        App()
    }
}

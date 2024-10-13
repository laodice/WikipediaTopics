package com.example.wikipediatopics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.wikipediatopics.page.PageViewModel
import com.example.wikipediatopics.ui.theme.WikipediaTopicsTheme
import org.koin.androidx.compose.koinViewModel

const val TEST_TAG_SEARCH_BAR = "TEST_TAG_SEARCH_BAR"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WikipediaTopicsTheme {
                App()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(viewModel: PageViewModel = koinViewModel()) {
    val query by viewModel.query.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()

    SearchBar(
        query = query,
        onQueryChange = viewModel::onQueryChange,
        onSearch = viewModel::onSearch,
        active = isSearching,
        onActiveChange = viewModel::onToggleSearch,
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag(TEST_TAG_SEARCH_BAR),
    ) {
        Text(text = query)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WikipediaTopicsTheme {
        App()
    }
}

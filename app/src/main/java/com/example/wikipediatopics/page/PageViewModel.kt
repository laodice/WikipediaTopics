package com.example.wikipediatopics.page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wikipediatopics.api.Failure
import com.example.wikipediatopics.api.Success
import com.example.wikipediatopics.api.WikipediaPage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PageViewModel(
    private val repository: PageRepository,
) : ViewModel() {
    private val _isSearching = MutableStateFlow(true)
    val isSearching = _isSearching.asStateFlow()

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _wikiPage: MutableStateFlow<WikipediaPage?> = MutableStateFlow(null)
    val wikiPage = _wikiPage.asStateFlow()

    fun onQueryChange(text: String) {
        _query.value = text
    }

    fun onToggleSearch(isSearching: Boolean) {
        _isSearching.value = isSearching
        if (!_isSearching.value) {
            onQueryChange("")
        }
    }

    fun onSearch(text: String) {
        onQueryChange(text)
        searchTopic(text)
    }

    private fun searchTopic(newTopic: String) {
        if (newTopic.isBlank()) return
        viewModelScope.launch {
            when (val result = repository.fetchPageForTopic(newTopic)) {
                is Success -> _wikiPage.value = result.value
                is Failure -> println("There's been an issue that should be handled: ${result.error} | ${result.msg}")
            }
        }
    }
}

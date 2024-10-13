package com.example.wikipediatopics.page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wikipediatopics.api.WikipediaPage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PageViewModel(
    private val repository: PageRepository,
) : ViewModel() {
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

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
        viewModelScope.launch {
            repository.fetchPageForTopic(newTopic).fold(
                onSuccess = { page: WikipediaPage -> println("LAO10: got page: ${page.title}") },
                onFailure = { exception -> println("LAO10: There's been an issue that should be handled: $exception") },
            )
        }
    }
}

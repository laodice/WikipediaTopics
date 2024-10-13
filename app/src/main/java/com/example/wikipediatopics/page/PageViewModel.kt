package com.example.wikipediatopics.page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wikipediatopics.api.WikipediaPage
import kotlinx.coroutines.launch

class PageViewModel(
    private val repository: PageRepository,
) : ViewModel() {
    fun searchTopic(newTopic: String) {
        viewModelScope.launch {
            repository.fetchPageForTopic(newTopic).fold(
                onSuccess = { page: WikipediaPage -> println("LAO10: got page: ${page.title}") },
                onFailure = { exception -> println("LAO10: There's been an issue that should be handled: $exception") },
            )
        }
    }
}

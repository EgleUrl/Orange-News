package com.example.orangenews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// Custom ViewModelFactory to allow ViewModel instantiation with constructor arguments
class ViewModelFactory(private val repository: ArticleRepository) : ViewModelProvider.Factory {
    // Called when a ViewModel is requested
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            // If the requested ViewModel is BookmarkViewModel, return
            // a new instance of it with the repository injected
            modelClass.isAssignableFrom(BookmarkViewModel::class.java) -> {
                BookmarkViewModel(repository) as T
            }
            // If the requested ViewModel class is not recognized, throw an error
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

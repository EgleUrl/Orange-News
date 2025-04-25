package com.example.orangenews

// This ViewModel:
// Interacts with the local Room database (through ArticleRepository).
// Exposes a real-time flow of bookmarked articles for Compose to observe.
// Provides simple methods to add or remove bookmarks asynchronously (using Kotlin coroutines).
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

// ViewModel responsible for managing the list of bookmarked articles
class BookmarkViewModel(private val repository: ArticleRepository) : ViewModel() {

    // Exposes a flow of all bookmarked articles to the UI
    val bookmarkedArticles: Flow<List<ArticleEntity>> = repository.allBookmarkedArticles

    /**
     * Adds a new article to the bookmarks database.
     *
     * @param article The article to be bookmarked.
     */
    fun addBookmark(article: ArticleEntity) {
        viewModelScope.launch {
            repository.insertArticle(article) // Insert article into the Room database
        }
    }

    /**
     * Removes an article from the bookmarks database.
     *
     * @param article The article to be removed from bookmarks.
     */
    fun removeBookmark(article: ArticleEntity) {
        viewModelScope.launch {
            repository.deleteArticle(article)
        }
    }
}

package com.example.orangenews

// This ArticleRepository:
// Connects the ViewModel to the DAO (database layer).
// Encapsulates database operations like inserting and deleting bookmarked articles.
// Exposes a Flow for observing real-time changes to bookmarked articles.
import kotlinx.coroutines.flow.Flow

/**
 * Repository class that abstracts access to the database.
 * Acts as a single source of truth for bookmarked articles.
 */
class ArticleRepository(private val articleDao: ArticleDao) {

    // Flow that emits the list of all bookmarked articles from the database
    val allBookmarkedArticles: Flow<List<ArticleEntity>> = articleDao.getAllBookmarkedArticles()

    /**
     * Inserts a new article into the bookmarks database.
     *
     * @param article The article to insert.
     */
    suspend fun insertArticle(article: ArticleEntity) {
        articleDao.insertArticle(article)
    }

    /**
     * Deletes an article from the bookmarks database.
     *
     * @param article The article to remove.
     */
    suspend fun deleteArticle(article: ArticleEntity) {
        articleDao.deleteArticle(article)
    }
}

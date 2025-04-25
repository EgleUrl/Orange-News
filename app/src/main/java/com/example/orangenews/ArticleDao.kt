package com.example.orangenews

// This DAO:
//Defines insert, delete, and query operations for bookmarked articles.
//Uses Kotlin Flow to allow the app to reactively listen for database changes.
//Makes the database asynchronous and efficient using Room and coroutines.
import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) interface for interacting with the bookmarked_articles table.
 * Defines all the database operations related to bookmarked news articles.
 */
@Dao
interface ArticleDao {
    /**
     * Inserts a new article into the database.
     * If an article with the same URL (primary key) already exists, it will be replaced.
     *
     * @param article The article to insert or update.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: ArticleEntity)

    /**
     * Deletes an existing article from the database.
     *
     * @param article The article to remove.
     */
    @Delete
    suspend fun deleteArticle(article: ArticleEntity)

    /**
     * Retrieves all bookmarked articles from the database as a Flow.
     * The Flow automatically updates if the data changes.
     *
     * @return A flow emitting the list of all bookmarked articles.
     */
    @Query("SELECT * FROM bookmarked_articles")
    fun getAllBookmarkedArticles(): Flow<List<ArticleEntity>>
}

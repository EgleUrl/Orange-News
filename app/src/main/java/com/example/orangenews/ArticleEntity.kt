package com.example.orangenews

// This ArticleEntity class defines the schema for the bookmarked_articles Room database table.
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity class that represents a bookmarked article stored in the local Room database.
 *
 * Each instance corresponds to a row in the "bookmarked_articles" table.
 */
@Entity(tableName = "bookmarked_articles")
data class ArticleEntity(
    @PrimaryKey val url: String, // Unique identifier for the article (using URL to avoid duplicates)
    val title: String, // Title of the article
    val description: String?, // Short description or summary text of the article
    val imageUrl: String?, // URL pointing to the article's image (can be null)
    val publishedAt: String, // Publish date in ISO format
    val sourceName: String, // Name of the news source (e.g., BBC, CNN)
    val summary: String? = null // AI-generated summary, saved if available (nullable)
)

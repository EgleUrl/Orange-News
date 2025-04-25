package com.example.orangenews

// This defines the UI model for a news article:
// Used in the app to display articles in the news feed, bookmarks, etc.
// Separates the display model from the database (ArticleEntity) or the network models (GNewsArticle).
/**
 * Data model representing a news article used in the UI layer.
 *
 * @param title The headline or title of the article.
 * @param description A short description or snippet of the article (optional).
 * @param content Full or partial content of the article (optional).
 * @param url The direct link to the full article on the news website.
 * @param image URL to the article's image (can be null).
 * @param publishedAt The published date and time in ISO format.
 * @param source An object containing information about the article's publisher.
 */
data class Article(
    val title: String,
    val description: String?,
    val content: String?,
    val url: String,
    val image: String?, // URL of the image associated with the article (optional)
    val publishedAt: String,
    val source: Source
)

/**
 * Data model representing the source of the article (e.g., BBC, CNN).
 *
 * @param name The name of the news source.
 * @param url The website URL of the source.
 */
data class Source(
    val name: String,
    val url: String
)


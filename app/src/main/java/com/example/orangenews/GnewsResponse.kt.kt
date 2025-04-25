package com.example.orangenews

// Represents the entire response returned from the GNews API
data class GNewsResponse(
    val totalArticles: Int, // Total number of articles found
    val articles: List<GNewsArticle> // List of articles returned in the response
)

// Represents a single article object from the GNews API response
data class GNewsArticle(
    val title: String,  // Title of the news article
    val description: String?, // Short description or summary of the article
    val content: String?, // Full or partial content of the article
    val url: String, // URL link to the original article
    val image: String?, // URL link to the article's image (if available)
    val publishedAt: String, // Published date and time (in ISO 8601 format)
    val source: GNewsSource // Information about the news source
)

// Represents the source of a news article (e.g., BBC News, The Guardian)
data class GNewsSource(
    val name: String, // Name of the news outlet
    val url: String // Official URL of the news outlet
)


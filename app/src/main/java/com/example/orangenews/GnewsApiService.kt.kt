package com.example.orangenews

// This interface defines two Retrofit API calls:
// getTopHeadlines() to fetch breaking news by category/topic.
// searchNews() to find articles based on a user-entered search query.
import retrofit2.http.GET
import retrofit2.http.Query

// Retrofit interface for communicating with the GNews API endpoints
interface GNewsApiService {
    /**
     * Fetches the top headlines for a given topic and country from GNews API.
     *
     * @param topic Optional topic (e.g., business, sports, technology).
     * @param country Country code (default is "gb" for United Kingdom).
     * @param language Language code (default is "en" for English).
     * @param apiKey API key for authentication with GNews service.
     * @return A GNewsResponse containing a list of news articles.
     */
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("topic") topic: String? = null,
        @Query("country") country: String = "gb",
        @Query("lang") language: String = "en",
        @Query("token") apiKey: String = Constant.apiKey
    ): GNewsResponse

    /**
     * Searches for news articles matching a specific keyword or phrase.
     *
     * @param query Search keyword or phrase.
     * @param country Country code (default is "gb" for United Kingdom).
     * @param language Language code (default is "en" for English).
     * @param apiKey API key for authentication with GNews service.
     * @return A GNewsResponse containing a list of search result articles.
     */
    @GET("search")
    suspend fun searchNews(
        @Query("q") query: String,
        @Query("country") country: String = "gb",
        @Query("lang") language: String = "en",
        @Query("token") apiKey: String = Constant.apiKey
    ): GNewsResponse
}


package com.example.orangenews

// This file fetches news articles by category or search query.
//Stores fetched articles in a StateFlow.
//Interacts with OpenAI to generate summaries.
//Caches summaries by article URL for better performance.
//Supports both asynchronous and blocking summary generation.
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// ViewModel that handles fetching news articles and generating summaries
class NewsViewModel : ViewModel() {

    // StateFlow to hold the list of fetched articles
    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles: StateFlow<List<Article>> = _articles

    // StateFlow to hold the map of URL -> generated summary
    private val _summaries = MutableStateFlow<Map<String, String>>(emptyMap())
    val summaries: StateFlow<Map<String, String>> = _summaries

    // Initialize the ViewModel by fetching "general" headlines
    init {
        fetchTopHeadlinesByCategory("general")
    }

    // Helper function to clean up headlines (removes website name and separators)
    private fun cleanHeadline(title: String?): String {
        return title?.replace(Regex(" [-|] .*"), "")?.trim() ?: "No Title"
    }

    // Load the user's default category preference and fetch headlines for it
    fun loadDefaultCategory(context: Context) {
        viewModelScope.launch {
            val category = PreferenceManager(context).getDefaultCategory()
            fetchTopHeadlinesByCategory(category)
        }
    }

    // Fetch top headlines from GNews API based on the selected category
    fun fetchTopHeadlinesByCategory(category: String) {
        val topic = when (category.lowercase()) {
            "general" -> "world"
            "uk news" -> "nation"
            "business" -> "business"
            "sports" -> "sports"
            "entertainment" -> "entertainment"
            "technology" -> "technology"
            "science" -> "science"
            else -> "nation"
        }

        viewModelScope.launch {
            try {
                val response = GNewsApiClient.instance.getTopHeadlines(
                    topic = topic,
                    country = "gb",
                    language = "en",
                    apiKey = Constant.apiKey
                )

                // Convert GNews articles to project local Article model
                _articles.value = response.articles.map { gnews ->
                    Article(
                        title = cleanHeadline(gnews.title),
                        description = null,
                        content = gnews.content,
                        url = gnews.url,
                        image = gnews.image,
                        publishedAt = gnews.publishedAt,
                        source = Source(
                            name = gnews.source.name,
                            url = gnews.source.url
                        )
                    )
                }
            } catch (e: Exception) {
                Log.e("GNews", "Failed to fetch category '$category': ${e.message}")
            }
        }
    }

    // Search news articles by a keyword query using the GNews API
    fun fetchEverythingWithQuery(query: String) {
        viewModelScope.launch {
            try {
                val response = GNewsApiClient.instance.searchNews(
                    query = "$query UK",
                    country = "gb",
                    language = "en",
                    apiKey = Constant.apiKey
                )
                _articles.value = response.articles.map { gnews ->
                    Article(
                        title = cleanHeadline(gnews.title),
                        description = null,
                        content = gnews.content,
                        url = gnews.url,
                        image = gnews.image,
                        publishedAt = gnews.publishedAt,
                        source = Source(
                            name = gnews.source.name,
                            url = gnews.source.url
                        )
                    )
                }
            } catch (e: Exception) {
                Log.e("GNews", "Search failed: ${e.message}")
            }
        }
    }

    // Generate a summary for a given article URL (only if it hasn't been summarized yet)
    fun generateSummaryForUrl(url: String, content: String) {
        viewModelScope.launch {
            if (_summaries.value.containsKey(url)) return@launch

            val summary = try {
                val response = OpenAIClient.instance.getSummary(
                    OpenAIRequest(
                        messages = listOf(
                            Message(role = "system", content = "You are a helpful assistant."),
                            Message(
                                role = "user",
                                content = "Summarize this article in 3–5 short sentences. If unable, say 'Summary not available':\n$content"
                            )
                        )
                    )
                )
                response.choices.firstOrNull()?.message?.content ?: "Summary not available"
            } catch (e: Exception) {
                Log.e("OpenAI", "Error generating summary: ${e.message}")
                "Summary not available"
            }

            _summaries.value += (url to summary)
        }
    }

    // Blocking version of summary generation (used when saving bookmarks)
    suspend fun generateSummaryBlocking(content: String): String {
        return try {
            val response = OpenAIClient.instance.getSummary(
                OpenAIRequest(
                    messages = listOf(
                        Message(role = "system", content = "You are a helpful assistant."),
                        Message(
                            role = "user",
                            content = "Summarize this article in 3–5 short sentences. If unable, say 'Summary not available':\n$content"
                        )
                    )
                )
            )
            response.choices.firstOrNull()?.message?.content ?: "Summary not available"
        } catch (e: Exception) {
            Log.e("OpenAI", "Error generating summary: ${e.message}")
            "Summary not available"
        }
    }
}



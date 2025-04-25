package com.example.orangenews

// This file displays a horizontal categories bar and search field.
// Loads articles into a scrollable vertical list.
// Expands articles to fetch and display summaries using OpenAI on demand.
// Supports sharing, bookmarking, and reading full articles inside the app.

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Helper function to format ISO date string into a readable date
fun formatDate(isoDate: String?): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        outputFormat.timeZone = TimeZone.getDefault()
        val date = isoDate?.let { inputFormat.parse(it) }
        date?.let { outputFormat.format(it) } ?: "Unknown Date"
    } catch (e: Exception) {
        "Unknown Date"
    }
}

// Helper function to share an article via external apps
fun shareArticle(context: Context, title: String, url: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, title)
        putExtra(Intent.EXTRA_TEXT, "Check out this article found at 🍊 Orange News app: $title\n\n$url")
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share via"))
}

// Main HomePage Composable showing categories and articles
@Composable
fun HomePage(newsViewModel: NewsViewModel, navController: NavHostController, bookmarkViewModel: BookmarkViewModel) {
    val articles by newsViewModel.articles.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        CategoriesBar(newsViewModel) // Top horizontal scrollable bar
        LazyColumn(modifier = Modifier.fillMaxSize()) { // Vertical scrollable list
            items(articles) { article ->
                ArticleItem(article,navController, bookmarkViewModel, newsViewModel)
            }
        }
    }
}

// Composable for a single news article card
@Composable
fun ArticleItem(
    article: Article,
    navController: NavHostController,
    bookmarkViewModel: BookmarkViewModel,
    newsViewModel: NewsViewModel? = null,
    isFromBookmarkScreen: Boolean = false
) {
    val context = LocalContext.current
    val formattedDate = formatDate(article.publishedAt)

    val bookmarkedArticles by bookmarkViewModel.bookmarkedArticles.collectAsState(initial = emptyList())
    val isBookmarked = bookmarkedArticles.any { it.url == article.url }

    val summaries = newsViewModel?.summaries?.collectAsState()?.value ?: emptyMap()
    val summary = if (isFromBookmarkScreen) article.description else summaries[article.url]

    var isExpanded by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    // Expand the article and fetch summary on demand
    LaunchedEffect(isExpanded) {
        if (isExpanded && summary == null && newsViewModel != null && !isFromBookmarkScreen) {
            isLoading = true
            newsViewModel.generateSummaryForUrl(
                url = article.url,
                content = article.content ?: article.description ?: article.title
            )
            isLoading = false
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            // Article headline
            Text(
                text = article.title,
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Article image
            AsyncImage(
                model = article.image ?: "https://via.placeholder.com/150",
                contentDescription = "Article image", // improves accessibility
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))


                // Summary block
                // Show Summary or Loading Spinner
                if (isFromBookmarkScreen) {
                    Text(
                        text = article.description ?: "No summary saved.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                } else if (isExpanded) {
                    if (summary == null) {
                        // Show spinner while waiting for summary
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        Text(
                            text = summary,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        // if summary is expanded text button text is changed to hide summary
                        TextButton(
                            onClick = { isExpanded = false },
                        ) {
                            Text("Hide Summary")
                        }
                    }
                } else {
                    // if summary is not expanded text button text is changed to show summary
                    TextButton(
                        onClick = { isExpanded = true },
                    ) {
                        Text("Show Summary")
                    }
                }


            Spacer(modifier = Modifier.height(6.dp))

            // Article source and date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = article.source.name,
                    style = MaterialTheme.typography.bodySmall,
                    //color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Share, Bookmark, Read More Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = {
                    shareArticle(context, article.title, article.url)
                }) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = "Share",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        val summaryForBookmark = summary ?: newsViewModel?.generateSummaryBlocking(
                            content = article.content ?: article.description ?: article.title
                        )

                        val articleEntity = ArticleEntity(
                            url = article.url,
                            title = article.title,
                            description = article.description,
                            imageUrl = article.image,
                            publishedAt = article.publishedAt,
                            sourceName = article.source.name,
                            summary = summaryForBookmark
                        )

                        if (isBookmarked) {
                            bookmarkViewModel.removeBookmark(articleEntity)
                        } else {
                            bookmarkViewModel.addBookmark(articleEntity)
                        }
                    }
                }) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                TextButton(onClick = {
                    navController.navigate(NewsArticleScreen(article.url))
                }) {
                    Text("Read More")
                }
            }
        }
    }
}


// Top bar for selecting categories or searching articles component
@Composable
fun CategoriesBar(newsViewModel: NewsViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearchExpanded by remember { mutableStateOf(false) }

    val categories = listOf("General", "UK news", "Business", "Sports", "Entertainment", "Technology", "Science")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Search bar component
        if (isSearchExpanded) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search news...") },
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = {
                        isSearchExpanded = false
                        if (searchQuery.isNotEmpty()) {
                            newsViewModel.fetchEverythingWithQuery(searchQuery)
                        }
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "Search icon")
                    }
                },
                modifier = Modifier
                    .padding(8.dp)
                    .height(50.dp)
                    .border(1.dp, Color.Black, CircleShape)
                    .clip(CircleShape)
            )
        } else {
            IconButton(onClick = { isSearchExpanded = true }) {
                Icon(Icons.Default.Search, contentDescription = "Search icon")
            }
        }

        categories.forEach { category ->
            // Category button
            Button(
                onClick = { newsViewModel.fetchTopHeadlinesByCategory(category.lowercase()) },
                modifier = Modifier.padding(4.dp)
            ) {
                Text(text = category)
            }
        }
    }
}










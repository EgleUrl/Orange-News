package com.example.orangenews

// This screen allows users to:
// View all articles they have bookmarked.
// Reuse the ArticleItem UI for consistency.
// Display a message if no bookmarks exist yet.
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

/**
 * Composable screen to display the list of bookmarked articles.
 *
 * @param navController Navigation controller to navigate between screens.
 * @param viewModel ViewModel providing access to bookmarked articles.
 */
@Composable
fun BookmarkScreen(navController: NavHostController, viewModel: BookmarkViewModel) {
    // Collect the flow of bookmarked articles as Compose state
    val bookmarkedArticles by viewModel.bookmarkedArticles.collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize()) {
        // Title of the screen
        Text(
            text = "Bookmarked Articles",
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .padding(8.dp)
                .padding(start = 30.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(10.dp)
        )

        // If no articles are bookmarked, show a placeholder text
        if (bookmarkedArticles.isEmpty()) {
            Text(
                text = "No saved articles yet!",
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            // Otherwise, display the list of bookmarked articles
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(bookmarkedArticles) { articleEntity ->
                    // Convert ArticleEntity (database model) to Article (UI model)
                    val article = Article(
                        title = articleEntity.title,
                        description = articleEntity.summary ?: articleEntity.description,
                        content = null, // Content is not stored for bookmarks
                        url = articleEntity.url,
                        image = articleEntity.imageUrl,
                        publishedAt = articleEntity.publishedAt,
                        source = Source(
                            name = articleEntity.sourceName,
                            url = ""
                        ),

                    )

                    // Display the article using the shared ArticleItem composable
                    ArticleItem(
                        article = article,
                        navController = navController,
                        bookmarkViewModel = viewModel,
                        isFromBookmarkScreen = true // disables on-demand summary expansion
                    )
                }
            }
        }
    }
}

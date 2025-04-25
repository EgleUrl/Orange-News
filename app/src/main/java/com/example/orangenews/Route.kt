package com.example.orangenews

// This file defines the navigation destinations used with
// Jetpack Compose’s NavHost and composable routes
//Each screen is made @Serializable to allow argument passing
// between screens (e.g., NewsArticleScreen(url)).
import kotlinx.serialization.Serializable

// Represents the route to the home screen of the app
// Displays categories and vertical list of news articles
@Serializable
object HomePageScreen

// Represents the route to a full news article page
// Takes a URL as a parameter to load the article in a WebView
@Serializable
data class NewsArticleScreen(
    val url : String
)
// Represents the route to the bookmarks screen
// Displays articles that the user has saved for offline reading
@Serializable
object BookmarkScreen
// Represents the route to the preferences/settings screen
// Allows users to set their preferred default news category
@Serializable
object PreferencesScreen
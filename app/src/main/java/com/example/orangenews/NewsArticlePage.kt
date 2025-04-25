package com.example.orangenews

// This file provides a screen for viewing full news articles using
// an embedded WebView component inside a Jetpack Compose app
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView

/**
 * A Composable function that displays a full news article inside a WebView.
 *
 * @param url The URL of the article to load and display.
 */
@Composable
fun NewsArticlePage(url: String) {
    // Embeds a traditional Android WebView into Jetpack Compose UI
    AndroidView(factory = { context ->
        WebView(context).apply {
            settings.javaScriptEnabled = true // Enable JavaScript for better website compatibility
            webViewClient = WebViewClient() // Handle navigation inside the WebView instead of opening external browser
            loadUrl(url)                    // Load the provided article URL
        }
    })
}
package com.example.orangenews

// This MainActivity.kt acts as the central controller:
// It initializes all ViewModels needed (news and bookmarks).
// It sets up the navigation graph for Home, Article Details, Bookmarks, and Preferences screens.
// It defines the top bar and floating action buttons for quick navigation.
// It applies app’s custom OrangeNewsTheme.
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.orangenews.ui.theme.OrangeNewsTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.Color
import androidx.activity.viewModels


@OptIn(ExperimentalMaterial3Api::class) // Enables use of experimental Material 3 APIs
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()    // Make content edge-to-edge under the system bars
        // Initialize NewsViewModel (For Fetching Articles)
        val newsViewModel: NewsViewModel by viewModels()

        // Load user's preferred category when app starts
        newsViewModel.loadDefaultCategory(applicationContext)
        // Initialize BookmarkViewModel (For managing bookmarks or saved articles)
        val database = ArticleDatabase.getDatabase(this)
        val repository = ArticleRepository(database.articleDao())
        val bookmarkViewModel = ViewModelProvider(
            this,
            ViewModelFactory(repository)
        )[BookmarkViewModel::class.java]

        // Set the main content of the app
        setContent {

            val navController = rememberNavController()

            OrangeNewsTheme {  // Apply custom app theme Theme.kt
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        // Top bar with app logo and name
                        TopAppBar(
                            title = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.orange),
                                        contentDescription = "Orange News Logo",
                                        modifier = Modifier.size(40.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "Orange News",
                                        style = MaterialTheme.typography.displayLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.Transparent
                            )
                        )
                    },
                    // Floating action buttons for Home, Bookmarks, and Settings
                    floatingActionButton = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp, start = 32.dp, end = 24.dp),
                            horizontalArrangement = Arrangement.Absolute.SpaceBetween
                        ) {
                            Row {
                                // FAB to navigate to Home
                                FloatingActionButton(
                                    onClick = {
                                        navController.navigate(HomePageScreen) {
                                            popUpTo(HomePageScreen) { inclusive = true }
                                        }
                                    },
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Home,
                                        contentDescription = "Go to Home",
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                                // FAB to navigate to Bookmarked Articles
                                FloatingActionButton(
                                    onClick = { navController.navigate(BookmarkScreen) },
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Bookmark,
                                        contentDescription = "View Bookmarked Articles",
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                                // FAB to navigate to Preferences (Settings)
                                FloatingActionButton(
                                    onClick = { navController.navigate(PreferencesScreen) },
                                    containerColor = MaterialTheme.colorScheme.primary
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_settings_24),
                                        contentDescription = "Settings",
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    // Navigation host to handle screen transitions
                    NavHost(
                        navController = navController,
                        startDestination = HomePageScreen,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // HomePage screen showing news headlines
                        composable<HomePageScreen> {
                            HomePage(newsViewModel, navController, bookmarkViewModel)
                        }
                        // NewsArticlePage screen displaying full news in WebView
                        composable<NewsArticleScreen> {
                            val args = it.toRoute<NewsArticleScreen>()
                            NewsArticlePage(args.url)
                        }
                        // BookmarkScreen to display saved articles
                        composable<BookmarkScreen> {
                            BookmarkScreen(navController, bookmarkViewModel)
                        }
                        composable<PreferencesScreen> {
                            PreferenceScreen(onCategorySaved = {
                                // Reload category after saving new preference
                                newsViewModel.loadDefaultCategory(this@MainActivity)
                                navController.popBackStack() // Go back to home
                            })
                        }

                    }
                }
            }
        }
    }
}


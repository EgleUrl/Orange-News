package com.example.orangenews

// This screen provides a UI for the user to select a preferred news category,
// which is then saved using PreferenceManager.
// That preference is used to filter news shown on the home screen.
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

// Composable screen that allows users to choose and save their default news category
@Composable
fun PreferenceScreen(
    onCategorySaved: () -> Unit // Callback invoked after saving preferences
) {
    val context = LocalContext.current
    // Get an instance of PreferenceManager to read/write user preferences
    val preferenceManager = remember { PreferenceManager(context) }

    // List of categories user can choose from
    val availableCategories = listOf(
        "General", "UK news", "Business", "Sports", "Entertainment", "Technology", "Science"
    )

    // Track the currently selected category using state
    var selectedCategory by remember {
        mutableStateOf(preferenceManager.getDefaultCategory())
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Select your default category", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(12.dp))

        // Display each category with a radio button for selection
        availableCategories.forEach { category ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { selectedCategory = category }, // Click selects the category
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = category == selectedCategory,
                    onClick = { selectedCategory = category }
                )
                Text(category, modifier = Modifier.padding(start = 8.dp))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Button to save the selected category to SharedPreferences
        Button(
            onClick = {
                preferenceManager.setDefaultCategory(selectedCategory)
                onCategorySaved() // Notify caller that category was saved (e.g. navigate back or refresh)
            }
        ) {
            Text("Save Preferences")
        }
    }
}


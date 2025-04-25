package com.example.orangenews

import android.content.Context

// A helper class to manage storing and retrieving user preferences using SharedPreferences
class PreferenceManager(context: Context) {
    // Access the shared preferences storage with a file name "user_preferences"
    private val sharedPref = context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
    // Save the user's preferred news category (e.g., "business", "sports")
    fun setDefaultCategory(category: String) {
        sharedPref.edit().putString("default_category", category).apply()
    }
    // Retrieve the saved preferred category, or return "general" as the default
    fun getDefaultCategory(): String {
        return sharedPref.getString("default_category", "general") ?: "general"
    }
}


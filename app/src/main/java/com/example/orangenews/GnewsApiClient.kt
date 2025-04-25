package com.example.orangenews

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Singleton object to provide a ready-to-use Retrofit client for GNews API
object GNewsApiClient {
    // Base URL of the GNews API
    private const val BASE_URL = "https://gnews.io/api/v4/"

    // Lazily initialized Retrofit instance configured for the GNewsApiService
    val instance: GNewsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) // Set the base endpoint for the GNews API
            .addConverterFactory(GsonConverterFactory.create()) // Use Gson to parse JSON responses into Kotlin objects
            .build()
            .create(GNewsApiService::class.java) // Create the implementation of the GNewsApiService interface
    }
}


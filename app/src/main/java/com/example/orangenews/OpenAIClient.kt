package com.example.orangenews

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton client for communicating with the OpenAI API using Retrofit.
 * This sets up the base URL, authorization header, and logging for all requests.
 */
object OpenAIClient {
    // Base URL for all OpenAI API requests
    private const val BASE_URL = "https://api.openai.com/"

    // OkHttpClient with authorization and logging interceptors
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            // Adds the Authorization header with the OpenAI API key
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${com.example.orangenews.Constant.openAiKey}")
                .build()
            chain.proceed(request)
        }
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Logs request and response bodies (for debugging)
        })
        .build()

    /**
     * Lazy-initialized Retrofit instance configured with OpenAI's base URL and our custom client.
     * Creates the implementation of the OpenAIApiService interface.
     */
    val instance: OpenAIApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create()) // Converts JSON into Kotlin data classes
            .build()
            .create(OpenAIApiService::class.java)
    }
}
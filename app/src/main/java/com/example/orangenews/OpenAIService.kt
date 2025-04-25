package com.example.orangenews

// This interface defines a single Retrofit API function to send a summary request to OpenAI.
// It uses a POST call with a request body (OpenAIRequest) and returns
// a structured response (OpenAIResponse).
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

// Retrofit interface for interacting with OpenAI's chat completion API
interface OpenAIApiService {
    // Sets the request content type to JSON
    @Headers("Content-Type: application/json")

    // Sends a POST request to OpenAI's /v1/chat/completions endpoint
    // Used to generate a summary based on the provided request body
    @POST("v1/chat/completions") // OpenAI endpoint
    suspend fun getSummary(@Body request: OpenAIRequest): OpenAIResponse
}
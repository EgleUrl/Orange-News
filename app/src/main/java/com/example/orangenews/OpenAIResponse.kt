package com.example.orangenews

// This file defines the data structures that map to the JSON response from OpenAI’s API.

// Represents the overall response returned by OpenAI's chat completion endpoint
data class OpenAIResponse(
    val choices: List<Choice> // A list of possible generated completions (usually just one)
)

// Represents a single choice from the OpenAI response
data class Choice(
    val message: Message // The generated message content (text)
)
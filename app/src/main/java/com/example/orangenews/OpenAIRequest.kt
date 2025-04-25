package com.example.orangenews

// This file defines the data model for OpenAI requests
/**
 * Represents the request payload sent to OpenAI's chat completion endpoint.
 * This object includes the model, message history, and optional tuning parameters.
 *
 * @param model The model to use (e.g., "gpt-4").
 * @param messages A list of messages representing the conversation history.
 * @param temperature Controls randomness: 0 = deterministic, 1 = creative.
 */
data class OpenAIRequest(
    val model: String = "gpt-4",
    val messages: List<Message>,
    val temperature: Double = 0.7
)

/**
 * Represents a single message in the conversation history.
 * Each message has a role and the actual content text.
 *
 * @param role Defines the sender's role (e.g., "system", "user", "assistant").
 * @param content The text content of the message.
 */
data class Message(
    val role: String, // Who is sending the message: "system", "user", or "assistant"
    val content: String // Actual message text
)
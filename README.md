#  Orange News App

A modern Android app built with **Kotlin** and **Jetpack Compose** to fetch and summarize news articles.  
It features on-demand article summarization powered by OpenAI, bookmarking for offline reading, and category-based browsing.
Article are fetched using GNews API.

![image](https://github.com/user-attachments/assets/d490ca3e-4961-4d29-bbb5-e18e8f52b8df)



---

## How to Build and Run

### Prerequisites
- **Android Studio Ladybug** (2023.3.1) or newer
- **Kotlin 1.8+**
- **Minimum SDK version:** 24 (Android 7.0)

### Setup Instructions

1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/orange-news-app.git
   ```
2. **Open the project in Android Studio.
3. Add your API keys:
- Create or edit the Constant.kt file inside com.example.orangenews.
```bash
object Constant {
    const val apiKey = "your_gnews_api_key_here"
    const val openAiKey = "your_openai_api_key_here"
}
```
- Replace with your GNews API key and OpenAI API key.
4. Sync Gradle:
- Go to File → Sync Project with Gradle Files.
5. Build the project:
- Build → Make Project
6. Run the application:
- Plug in your Android device or start an emulator.
- Press the Run button or use Shift + F10.

## Third-Party Libraries and SDKs Used

| Library            | Purpose                                           |
|--------------------|---------------------------------------------------|
| Retrofit            | REST API client for GNews and OpenAI APIs         |
| Room                | Local database for saving bookmarked articles    |
| OkHttp              | HTTP client for network requests                 |
| Gson                | JSON serialization/deserialization               |
| Coil                | Asynchronous image loading                       |
| Kotlin Coroutines   | Asynchronous programming (background work)       |
| Jetpack Compose     | Declarative UI framework                         |
| Navigation Compose  | In-app navigation between screens                |
--------------------------------------------------------------------------
## Design solutions

### Wireframes

![image](https://github.com/user-attachments/assets/4537d737-5e99-41d8-bb16-3f10b560132e)

### Moodboard

![image](https://github.com/user-attachments/assets/8167494a-1a63-4219-b535-65822175d7b7)



## Known Issues and Limitations

### API Keys Required
The app will not fetch news or summaries unless valid GNews and OpenAI API keys are provided.

### Summary Loading Delay
When expanding a news article for the first time, there may be a small delay while the summary is being generated from OpenAI.

### No Paging/Lazy Loading
Currently, articles are loaded all at once instead of paginated — may cause performance issues with a very large number of articles.

### Network Dependency
The app requires a stable internet connection to fetch news and generate summaries. No offline caching for articles themselves (only bookmarks are offline).

### Language/Region Limitation
Focused mainly on UK news sources via GNews API.

### Settings Limited to Categories
The preferences screen only allows selecting a default news category; additional settings are not available yet.



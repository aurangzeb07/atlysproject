# Movie App Assignment - Atlys

A modern Android movie app built with Jetpack Compose, offline support, and seamless navigation.

## Features

- 🎬 **Trending Movies**: Displays trending movies from TMDB API
- 🔍 **Search Functionality**: Real-time search through cached movie data
- 📱 **Movie Details**: Detailed view with backdrop, overview, ratings, and more
- 💾 **Offline Support**: Caches movie data locally using Room database
- 🎨 **Modern UI**: Glass-morphism design with smooth animations
- ⚡ **MVVM Architecture**: Clean architecture following Android best practices

## Architecture

The app follows **MVVM (Model-View-ViewModel)** architecture pattern:

- **Data Layer**: 
  - `MovieRepository`: Handles data fetching from API and local database
  - `ApiService`: Retrofit interface for TMDB API
  - `MovieDatabase`: Room database for offline caching
  - `MovieDao`: Data access object for database operations

- **Domain Layer**:
  - `MovieListViewModel`: Manages UI state for movie list screen
  - `MovieDetailViewModel`: Manages UI state for movie detail screen

- **UI Layer**:
   - `MovieListViewModel` & `MovieDetailViewModel`: Manage UI state for the screens
  - `MovieListScreen` & `MovieDetailScreen`: Composable screens that display the UI
  - `NavGraph`: Handles navigation between screens

## Setup Instructions

1. **Get TMDB API Key**:
   - Go to [TMDB](https://www.themoviedb.org/)
   - Sign up for an account
   - Navigate to Settings > API
   - Generate an API key

2. **Configure API Key**:
   - Open `local.properties` file in the root of the project.
   - Add your TMDB API key in the following format:
   ```properties
   TMDB_API_KEY="your_actual_api_key_here"
   ```

3. **Build and Run**:
   - Sync the project with Gradle files
   - Build and run on an Android device or emulator (API 24+)

## Technologies Used

- **Kotlin**: Programming language
- **Jetpack Compose**: Modern UI toolkit
- **Navigation Compose**: Navigation framework
- **Retrofit**: HTTP client for API calls
- **Room**: Local database for offline caching
- **Coil**: Image loading library
- **Coroutines & Flow**: Asynchronous programming
- **Material 3**: Modern Material Design components

## Project Structure

```
app/src/main/java/com/atlys/movieatlysassignment/
├── data/
│   ├── api/              # API service and configuration
│   ├── database/         # Room database, DAO, entities
│   ├── model/            # Data models
│   └── repository/       # Repository pattern implementation
|── di/                   # Dependency injection modules
├── ui/
│   ├── components/       # Reusable UI components
│   ├── navigation/       # Navigation setup
│   ├── screen/           # Screen composables
│   ├── theme/            # Theme and styling
│   └── viewmodel/        # ViewModels
├── util/                 # Utility classes and constants
├── MainActivity.kt       # Main activity
└── MovieApp.kt           # Application class
```

## Key Features Implementation

### Offline Support
- Movies are automatically cached when fetched from API
- App works offline by displaying cached data
- Seamless fallback to cache when network is unavailable

### Search Functionality
- Real-time search through movie titles and overviews
- Searches through cached data for instant results
- Clears search to show trending movies again

### Error Handling
- Loading states with progress indicators
- Error states with retry functionality
- Empty states when no data is available

## App Demo

[Add your app demo recording link here]

## Notes

- The app displays up to 20 trending movies (as per requirements)
- No pagination implemented (as per requirements)
- Single module architecture (as per requirements)
- No unit tests (as per requirements)

## License

This project is created for Atlys take-home assignment.



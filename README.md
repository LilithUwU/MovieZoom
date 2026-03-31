# MovieZoom

MovieZoom is a media browsing application built with the TMDB API.

## Compliance with Requirements
As specified in the assignment, this project implements:
- **Language** Kotlin
- **Architecture** MVVM (Model-View-ViewModel)
- **UI Framework** XML Layouts with ViewBinding
- **Screens** scrollable media list and a detailed view for each item.

## Technical Decisions & Tooling
Beyond the mandatory requirements, the following tools and patterns were chosen to ensure code quality and performance:

- **Clean Architecture** the code is organized into Domain, Data, and Presentation layers.
- **Koin** chosen for DI because it's a lightweight and suits well for projects of this size
- **Retrofit & OkHttp** for networking.
- **Coroutines & Flow** for asynchronous operations and reactive state management via `StateFlow`.
- **Glide** for image loading and caching of movie posters.
- **MockK** for unit testing.

## Detail Screen Strategy
Used shared ViewModel to avoid the overhead of serializing data and the 1MB system limit, allowing screens to instantly access the same objects directly from memory.

## Extra Features
The following optional features were implemented:
- **Search** finds movies by title
- **Pagination** when clicking on "Load more" button we get additional movies to our existing list

## Setup
1. Add your `TMDB_API_KEY` to `local.properties`.
2. Build and run the app on a device with **API 31+**.

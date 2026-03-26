package com.example.moviezoom

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviezoom.network.Movie
import com.example.moviezoom.network.movieApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class MovieUiState {
    object Loading : MovieUiState()
    data class Success(val movies: List<Movie>) : MovieUiState()
    data class Error(val message: String) : MovieUiState()
}

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<MovieUiState>(MovieUiState.Loading)
    val uiState: StateFlow<MovieUiState> = _uiState.asStateFlow()

    private val _selectedMovie = MutableStateFlow<Movie?>(null)
    val selectedMovie: StateFlow<Movie?> = _selectedMovie.asStateFlow()

    fun selectMovie(movie: Movie) {
        _selectedMovie.value = movie
        Log.d(TAG, "selectMovie: $movie")
    }

    fun clearSelectedMovie() {
        _selectedMovie.value = null
    }

    fun getMovies() {
        viewModelScope.launch {
            _uiState.value = MovieUiState.Loading
            try {
                val response = movieApi.getTopRatedMovies()
                _uiState.value = MovieUiState.Success(response.results)
                Log.d(TAG, "Movies loaded successfully")
            } catch (e: Exception) {
                _uiState.value = MovieUiState.Error(e.message ?: "Unknown Error")
                Log.e(TAG, "Error: ${e.message}")
            }
        }
    }

    fun search(query: String) {
        viewModelScope.launch {
            _uiState.value = MovieUiState.Loading
            try {
                val response = movieApi.searchMovie(query)
                _uiState.value = MovieUiState.Success(response.results)
                Log.d(TAG, "Movies loaded successfully")
            } catch (e: Exception) {
                _uiState.value = MovieUiState.Error(e.message ?: "Unknown Error")
            }
        }
    }
}

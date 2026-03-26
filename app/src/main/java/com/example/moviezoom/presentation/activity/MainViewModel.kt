package com.example.moviezoom.presentation.activity

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviezoom.data.remote.MovieApi
import com.example.moviezoom.data.repository.MovieRepositoryImpl
import com.example.moviezoom.di.NetworkModule
import com.example.moviezoom.domain.model.Movie
import com.example.moviezoom.domain.repository.MovieRepository
import com.example.moviezoom.domain.usecase.GetTopRatedMoviesUseCase
import com.example.moviezoom.domain.usecase.SearchMovieUseCase
import com.example.moviezoom.presentation.uistate.MovieUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

const val TAG = "MovieZoom"

class MainViewModel : ViewModel() {


    private val movieApi: MovieApi = NetworkModule.provideMovieApi()
    private val movieRepository: MovieRepository = MovieRepositoryImpl(movieApi)
    private val getTopRatedMoviesUseCase = GetTopRatedMoviesUseCase(movieRepository)
    private val searchMovieUseCase = SearchMovieUseCase(movieRepository)

    private val _uiState = MutableStateFlow<MovieUiState>(MovieUiState.Loading)
    val uiState: StateFlow<MovieUiState> = _uiState.asStateFlow()

    private val _selectedMovie = MutableStateFlow<Movie?>(null)
    val selectedMovie: StateFlow<Movie?> = _selectedMovie.asStateFlow()

    var currentPage = 1


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
                val moviePage = getTopRatedMoviesUseCase(1)
                currentPage = 1
                _uiState.value = MovieUiState.Success(moviePage.results)
                Log.d(TAG, "Movies loaded successfully")
            } catch (e: Exception) {
                _uiState.value = MovieUiState.Error(e.message ?: "Unknown Error")
                Log.e(TAG, "Error: ${e.message}")
            }
        }
    }

    fun loadMoreMovies() {
        val currentMovies = (uiState.value as? MovieUiState.Success)?.movies ?: emptyList()
        viewModelScope.launch {
            _uiState.value = MovieUiState.Loading
            try {
                val nextPage = currentPage + 1
                val moviePage = getTopRatedMoviesUseCase(nextPage)
                _uiState.value = MovieUiState.Success(currentMovies + moviePage.results)
                currentPage = nextPage
                Log.d(TAG, "Fetched page $currentPage. Total movies: ${(currentMovies + moviePage.results).size}")
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
                val moviePage = searchMovieUseCase(query, 1)
                _uiState.value = MovieUiState.Success(moviePage.results)
                Log.d(TAG, "Movies loaded successfully")
            } catch (e: Exception) {
                _uiState.value = MovieUiState.Error(e.message ?: "Unknown Error")
            }
        }
    }
}

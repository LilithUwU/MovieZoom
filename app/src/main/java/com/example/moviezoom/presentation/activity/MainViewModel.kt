package com.example.moviezoom.presentation.activity

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviezoom.domain.model.Movie
import com.example.moviezoom.domain.usecase.GetTopRatedMoviesUseCase
import com.example.moviezoom.domain.usecase.SearchMovieUseCase
import com.example.moviezoom.presentation.uistate.ErrorType
import com.example.moviezoom.presentation.uistate.MovieUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException


class MainViewModel(
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val searchMovieUseCase: SearchMovieUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieUiState>(MovieUiState.Loading)
    val uiState: StateFlow<MovieUiState> = _uiState.asStateFlow()

    private val _selectedMovie = MutableStateFlow<Movie?>(null)
    val selectedMovie: StateFlow<Movie?> = _selectedMovie.asStateFlow()

    var currentPage = 1


    fun selectMovie(movie: Movie) {
        _selectedMovie.value = movie
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
            } catch (e: Exception) {
                handleError(e)
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
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun search(query: String) {
        viewModelScope.launch {
            _uiState.value = MovieUiState.Loading
            try {
                val moviePage = searchMovieUseCase(query, 1)
                _uiState.value = MovieUiState.Success(moviePage.results)
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun handleError(e: Exception) {
        val errorType = when (e) {
            is UnknownHostException -> ErrorType.Network
            else -> ErrorType.Unknown(e.message)
        }
        _uiState.value = MovieUiState.Error(errorType)
    }
}

package com.example.moviezoom

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviezoom.network.Movie
import com.example.moviezoom.network.movieApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val _movies = MutableStateFlow<List<Movie>>(
        value = emptyList()
    )
    val movies: StateFlow<List<Movie>> = _movies


    fun getMovies() {
        viewModelScope.launch {
            try {
                val response = movieApi.getTopRatedMovies()
                _movies.value = response.results ?: emptyList()
                Log.d("MainViewModel", "Movies: ${_movies.value}")
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error: ${e.message}")
            }
        }
    }
}

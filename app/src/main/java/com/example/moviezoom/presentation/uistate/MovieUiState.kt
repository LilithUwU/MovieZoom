package com.example.moviezoom.presentation.uistate

import com.example.moviezoom.domain.model.Movie

sealed class MovieUiState {
    object Loading : MovieUiState()
    data class Success(val movies: List<Movie>) : MovieUiState()
    data class Error(val errorType: ErrorType) : MovieUiState()
}

sealed class ErrorType {
    object Network : ErrorType()
    data class Unknown(val message: String?) : ErrorType()
}
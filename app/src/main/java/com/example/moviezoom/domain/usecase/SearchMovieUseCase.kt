package com.example.moviezoom.domain.usecase

import com.example.moviezoom.domain.model.MoviePage
import com.example.moviezoom.domain.repository.MovieRepository

class SearchMovieUseCase(
    private val repository: MovieRepository
) {

    suspend operator fun invoke(query: String, page: Int = 1): MoviePage {
        return repository.searchMovie(query, page)
    }
}
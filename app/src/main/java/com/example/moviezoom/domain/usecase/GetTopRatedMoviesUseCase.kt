package com.example.moviezoom.domain.usecase

import com.example.moviezoom.domain.model.MoviePage
import com.example.moviezoom.domain.repository.MovieRepository

class GetTopRatedMoviesUseCase(
    private val repository: MovieRepository
) {

    suspend operator fun invoke(page: Int): MoviePage {
        return repository.getTopRatedMovies(page)
    }
}
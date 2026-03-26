package com.example.moviezoom.data.repository

import com.example.moviezoom.data.mapper.toDomain
import com.example.moviezoom.data.remote.MovieApi
import com.example.moviezoom.domain.model.MoviePage
import com.example.moviezoom.domain.repository.MovieRepository

class MovieRepositoryImpl(
    private val api: MovieApi
) : MovieRepository {

    override suspend fun getTopRatedMovies(page: Int): MoviePage {
        return api.getTopRatedMovies(page).toDomain()
    }

    override suspend fun searchMovie(
        query: String,
        page: Int
    ): MoviePage {
        return api.searchMovie(query, page).toDomain()
    }
}
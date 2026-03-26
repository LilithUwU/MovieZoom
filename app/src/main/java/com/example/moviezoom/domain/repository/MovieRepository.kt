package com.example.moviezoom.domain.repository

import com.example.moviezoom.domain.model.MoviePage

interface MovieRepository {

    suspend fun getTopRatedMovies(page: Int): MoviePage

    suspend fun searchMovie(
        query: String,
        page: Int
    ): MoviePage
}
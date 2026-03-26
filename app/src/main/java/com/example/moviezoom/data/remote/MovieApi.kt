package com.example.moviezoom.data.remote

import com.example.moviezoom.data.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface MovieApi {
    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int
    ): MovieResponse

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") searchTerm: String,
        @Query("page") page: Int = 1
    ): MovieResponse
}
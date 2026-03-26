package com.example.moviezoom.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String,
    val backdropPath: String,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    val adult: Boolean,
    val popularity: Double
)

data class MoviePage(
    val page: Int,
    val results: List<Movie>
)

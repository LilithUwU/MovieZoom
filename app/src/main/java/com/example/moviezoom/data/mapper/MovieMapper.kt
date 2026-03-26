package com.example.moviezoom.data.mapper

import com.example.moviezoom.data.model.Movie as MovieDto
import com.example.moviezoom.data.model.MovieResponse as MovieResponseDto
import com.example.moviezoom.data.remote.NetworkConstants
import com.example.moviezoom.domain.model.Movie
import com.example.moviezoom.domain.model.MoviePage

fun MovieResponseDto.toDomain(): MoviePage {
    return MoviePage(
        page = this.page,
        results = this.results.map { it.toDomain() }
    )
}

fun MovieDto.toDomain(): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        overview = this.overview,
        posterPath = "${NetworkConstants.IMAGE_BASE_URL}${this.posterPath}",
        backdropPath = "${NetworkConstants.IMAGE_BASE_URL}${this.backdropPath}",
        releaseDate = this.releaseDate,
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
        adult = this.adult,
        popularity = this.popularity
    )
}

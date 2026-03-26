package com.example.moviezoom.network

import com.example.moviezoom.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


const val BASE_URL = "https://api.themoviedb.org/3/"
const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor { chain ->
        val original = chain.request()
        val request = original.newBuilder()
            .header("Authorization", "Bearer ${BuildConfig.MOVIE_API_KEY}")
            .header("accept", "application/json")
            .build()
        chain.proceed(request)
    }
    .addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    })
    .build()

val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val movieApi: MovieApi = retrofit.create(MovieApi::class.java)

interface MovieApi {
    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int
    ): MovieResponse


    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") searchTerm: String,
        @Query("page") page: Int = 1,
    ): MovieResponse
}

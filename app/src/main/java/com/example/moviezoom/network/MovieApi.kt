package com.example.moviezoom.network

import com.example.moviezoom.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object NetworkConstants {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
}

object NetworkModule {

    private val authInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${BuildConfig.MOVIE_API_KEY}")
            .addHeader("accept", "application/json")
            .build()

        chain.proceed(request)
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(NetworkConstants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val movieApi: MovieApi by lazy {
        retrofit.create(MovieApi::class.java)
    }
}

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
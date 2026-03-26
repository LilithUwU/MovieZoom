package com.example.moviezoom.di

import com.example.moviezoom.BuildConfig
import com.example.moviezoom.data.remote.MovieApi
import com.example.moviezoom.data.remote.NetworkConstants
import com.example.moviezoom.data.repository.MovieRepositoryImpl
import com.example.moviezoom.domain.repository.MovieRepository
import com.example.moviezoom.domain.usecase.GetTopRatedMoviesUseCase
import com.example.moviezoom.domain.usecase.SearchMovieUseCase
import com.example.moviezoom.presentation.activity.MainViewModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single<Interceptor> {
        Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${BuildConfig.MOVIE_API_KEY}")
                .addHeader("accept", "application/json")
                .build()
            chain.proceed(request)
        }
    }

    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<Interceptor>())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(NetworkConstants.BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single { get<Retrofit>().create(MovieApi::class.java) }
}

val repositoryModule = module {
    singleOf(::MovieRepositoryImpl) { bind<MovieRepository>() }
}

val useCaseModule = module {
    factoryOf(::GetTopRatedMoviesUseCase)
    factoryOf(::SearchMovieUseCase)
}

val viewModelModule = module {
    viewModelOf(::MainViewModel)
}

val appModule = listOf(networkModule, repositoryModule, useCaseModule, viewModelModule)
